package com.chototclone.Services;

import com.chototclone.Entities.Listing;
import com.chototclone.Payload.Request.Listing.CreateRequest;

import java.util.List;
import java.util.Optional;

public interface ListingService {
    Optional<Listing> getById(Long id);

    List<Listing> getAll();

    Listing create(CreateRequest listing);

    Listing update(Listing listing);

    void delete(Long id);


}
