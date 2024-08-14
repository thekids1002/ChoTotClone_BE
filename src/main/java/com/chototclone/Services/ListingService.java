package com.chototclone.Services;

import com.chototclone.Entities.Listing;
import com.chototclone.Payload.Request.Listing.CreateRequest;
import com.chototclone.Payload.Request.Listing.UpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ListingService {
    Optional<Listing> findById(Long id);

    List<Listing> findAll();

    Listing create(CreateRequest listing);

    Listing update(MultipartFile[] files,UpdateRequest listing);

    void delete(Long id);


}
