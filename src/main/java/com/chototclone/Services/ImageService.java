package com.chototclone.Services;

import com.chototclone.Entities.Image;
import com.chototclone.Entities.Listing;
import com.chototclone.Repository.ImageRepository;
import com.chototclone.Repository.ListingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    @Autowired
    ListingRepository listingRepository;

    @Autowired
    ImageRepository imageRepository;

    @Transactional
    public boolean deleteImageIdInListing(Long id) {
        try {
            Listing listing = listingRepository.findById(id).orElse(null);
            if (listing == null) {
                return false;
            }
            Image image = imageRepository.findById(id).get();
            imageRepository.delete(image);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
