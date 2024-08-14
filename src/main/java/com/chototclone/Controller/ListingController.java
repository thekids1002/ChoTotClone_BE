package com.chototclone.Controller;

import com.chototclone.Entities.Listing;
import com.chototclone.Payload.Request.Listing.CreateRequest;
import com.chototclone.Payload.Request.Listing.UpdateRequest;
import com.chototclone.Payload.Response.ResponseObject;
import com.chototclone.Services.ListingService;
import com.chototclone.Utils.Message;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/listing")
public class ListingController {

    private static final String UPLOADED_FOLDER = "uploads/";

    private static final Logger logger = LoggerFactory.getLogger(ListingController.class);

    @Autowired
    private ListingService listingService;

    @RequestMapping("/getAll")
    public ResponseEntity<?> getAll() {
        return null;
    }

    @RequestMapping("/getById")
    public ResponseEntity<?> getById() {
        return null;
    }

    @RequestMapping("/create")
    public ResponseEntity<ResponseObject> create(
            @RequestParam("files") MultipartFile[] files,
            @Valid @RequestParam("form") CreateRequest createRequest
    ) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        String message;

        ResponseObject responseObject = ResponseObject.builder().build();

        if (files.length > 6) {
            logger.error("The number of files does not exceed 6.");
            responseObject.setMessage("The number of files does not exceed 6.");
            responseObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
        }

        // save file
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
                createRequest.getImagesList().add(fileName);
            } catch (IOException e) {
                responseObject.setMessage(e.getMessage());
                responseObject.setStatusCode(httpStatus.value());
                return new ResponseEntity<>(responseObject, httpStatus);
            }
        }

        Listing listing = listingService.create(createRequest);
        boolean isCreated = Objects.nonNull(listing);
        httpStatus = isCreated ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;

        message = isCreated ? Message.SUCCESS : Message.INTERNAL_SERVER_ERROR;

        responseObject = ResponseObject
                .builder()
                .message(message)
                .statusCode(httpStatus.value())
                .data(listing)
                .build();
        return new ResponseEntity<>(responseObject, httpStatus);
    }

    @RequestMapping("/update")
    public ResponseEntity<?> update(
            @RequestParam("files") MultipartFile[] files,
            @Valid @RequestParam("form") UpdateRequest updateRequest
    ) {

        Listing listing = listingService.update(files, updateRequest);
        boolean isUpdated = Objects.nonNull(listing);
        HttpStatus httpStatus = isUpdated ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
        String message = isUpdated ? Message.SUCCESS : Message.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(ResponseObject
                .builder()
                .message(message)
                .statusCode(httpStatus.value())
                .data(listing)
                .build(), httpStatus);
    }

    @RequestMapping("/delete")
    public ResponseEntity<?> delete() {
        return null;
    }
}
