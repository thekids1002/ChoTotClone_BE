package com.chototclone.Controller;

import com.chototclone.Payload.Response.ResponseObject;
import com.chototclone.Services.ImageService;
import com.chototclone.Utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @DeleteMapping("/images/{id}")
    public ResponseEntity<?> deleteImageIdInListing(@PathVariable Long id) {
        boolean isDeleted = imageService.deleteImageIdInListing(id);
        HttpStatus httpStatus = isDeleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        String message = isDeleted ? Message.SUCCESS : Message.INTERNAL_SERVER_ERROR;
        ResponseObject responseObject = ResponseObject.builder().message(message).statusCode(httpStatus.value()).build();
        return new ResponseEntity<>(responseObject, httpStatus);
    }
}
