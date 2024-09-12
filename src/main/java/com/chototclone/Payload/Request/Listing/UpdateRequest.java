package com.chototclone.Payload.Request.Listing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Data
public class UpdateRequest {
    @NotNull(message = "Listing ID is required")
    private Long listingId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Brand ID is required")
    private Long brandId;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be at most 100 characters long")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must be at most 255 characters long")
    private String description;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotBlank(message = "Location is required")
    @Size(max = 255, message = "Location must be at most 255 characters long")
    private String location;

    private List<String> imagesList = new ArrayList<>();
}
