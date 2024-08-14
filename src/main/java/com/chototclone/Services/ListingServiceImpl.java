package com.chototclone.Services;

import com.chototclone.Constant.DefaultConst;
import com.chototclone.Entities.*;
import com.chototclone.Payload.Request.Listing.CreateRequest;
import com.chototclone.Payload.Request.Listing.UpdateRequest;
import com.chototclone.Repository.BranhRepository;
import com.chototclone.Repository.CategoryRepository;
import com.chototclone.Repository.ImageRepository;
import com.chototclone.Repository.ListingRepository;
import com.chototclone.Utils.DateUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ListingServiceImpl implements ListingService {

    private static final Logger logger = LoggerFactory.getLogger(ListingServiceImpl.class);
    private static final String UPLOADED_FOLDER = "/uploads";

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BranhRepository brandRepository;

    @Autowired
    private ImageRepository imageRepository;

    /**
     * Retrieves a listing by its ID.
     *
     * @param id the ID of the listing
     * @return an {@link Optional} containing the listing if found, or empty if not
     */
    @Override
    public Optional<Listing> findById(Long id) {
        return listingRepository.findById(id);
    }

    /**
     * Retrieves all listings.
     *
     * @return a {@link List} of all listings
     */
    @Override
    public List<Listing> findAll() {
        return listingRepository.findAll();
    }

    /**
     * Creates a new listing from the provided CreateRequest.
     * <p>
     * This method validates the input, fetches related Category and Brand, creates and saves the Listing,
     * and associates images with the listing. If any issue occurs during the process, it logs the error and returns null.
     *
     * @param listingRequest the CreateRequest containing the listing details
     * @return the created Listing or null if creation fails
     */
    @Transactional
    @Override
    public Listing create(CreateRequest listingRequest) {
        try {
            // Check if the listing request is null
            if (listingRequest == null) {
                throw new IllegalArgumentException("Listing request cannot be null");
            }

            // Fetch the Category by ID, throwing an exception if not found
            Category category = categoryRepository.findById(listingRequest.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + listingRequest.getCategoryId()));

            // Fetch the Brand by ID, throwing an exception if not found
            Brand brand = brandRepository.findById(listingRequest.getBrandId())
                    .orElseThrow(() -> new EntityNotFoundException("Brand not found with ID: " + listingRequest.getBrandId()));

            // Create a new Listing and set its properties
            Listing newListing = new Listing();
            initializeListing(true, category, brand, newListing, listingRequest.getTitle(), listingRequest.getDescription(), listingRequest.getPrice(), listingRequest.getLocation());

            // Save the new Listing
            newListing = listingRepository.save(newListing);

            // Build and save the images associated with the listing
            List<Image> images = buildImages(newListing.getId(), listingRequest.getImagesList());
            imageRepository.saveAll(images);

            // Add the images to the listing
            newListing.getImages().addAll(images);

            return newListing;
        } catch (Exception e) {
            // Log the error and return null if creation fails
            logger.error("Failed to create listing :", e.getMessage(), e);
            return null;
        }
    }

    @Override
    @Transactional
    public Listing update(MultipartFile[] files, UpdateRequest updateRequest) {

        Listing updateListing = listingRepository.findById(updateRequest.getListingId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + updateRequest.getCategoryId()));
        ;

        if (updateListing.getImages().size() >= 6) {
            return null;
        }
        // Fetch the Category by ID, throwing an exception if not found
        Category category = categoryRepository.findById(updateRequest.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + updateRequest.getCategoryId()));

        // Fetch the Brand by ID, throwing an exception if not found
        Brand brand = brandRepository.findById(updateRequest.getBrandId())
                .orElseThrow(() -> new EntityNotFoundException("Brand not found with ID: " + updateRequest.getBrandId()));

        // Create a new Listing and set its properties

        initializeListing(false, category, brand, updateListing, updateRequest.getTitle(), updateRequest.getDescription(), updateRequest.getPrice(), updateRequest.getLocation());

        List<String> imagesNew = new ArrayList<>();
        long currentMillis = System.currentTimeMillis();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            try {
                // Lưu file vào thư mục
                String fileName = file.getOriginalFilename() + currentMillis;
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + fileName);
                Files.write(path, bytes);
                imagesNew.add(fileName);
            } catch (Exception e) {

            }
        }
        // update image to database
        List<Image> imageList = buildImages(updateListing.getId(), imagesNew);
        imageRepository.saveAll(imageList);
        updateListing.getImages().addAll(imageList);
        // Save the updated listing to the database

        return listingRepository.save(updateListing);
    }

    /**
     * Builds a list of {@link Image} objects from image names and a listing ID.
     *
     * @param listingId  the ID of the listing
     * @param imageNames a list of image URLs
     * @return a list of {@link Image} objects
     */
    private List<Image> buildImages(Long listingId, List<String> imageNames) {
        return imageNames.stream()
                .map(imageName -> Image.builder()
                        .id(listingId)
                        .imageUrl(imageName)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing listing in the database.
     *
     * @return the updated listing
     */


    private void initializeListing(
            boolean isCreate,
            Category category,
            Brand brand,
            Listing newListing,
            String title,
            String description,
            BigDecimal price,
            String location
    ) {
        newListing.setCategory(category);
        newListing.setBrand(brand);
        newListing.setTitle(title);
        newListing.setDescription(description);
        newListing.setPrice(price);
        newListing.setLocation(location);
        if (isCreate) {
            newListing.setExpiryDate(DateUtil.addDays(DefaultConst.EXPRIRED_DATE_LISTING));
        }
        newListing.setStatus(Status.ACTIVE);
    }

    /**
     * Marks the listing with the given ID as deleted by setting the delFlag to the valid delete flag.
     *
     * @param id the ID of the listing to delete
     */
    @Override
    public void delete(Long id) {
        // Retrieve the listing from the repository by ID
        Listing listing = listingRepository.getById(id);

        // Set the delFlag to the valid delete flag to mark the listing as deleted
        listing.setDelFlag(DefaultConst.VALID_DEL_FLAG);

        // Save the updated listing to the repository
        listingRepository.save(listing);
    }

}
