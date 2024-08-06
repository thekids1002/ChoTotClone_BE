package com.chototclone.Controller;

import com.chototclone.Entities.Category;
import com.chototclone.Payload.Response.ReponseObject;
import com.chototclone.Services.CategoryService;
import com.chototclone.Utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/test")
    String test() {
        return "Ok";
    }

    @GetMapping({"", "/"})
    public ResponseEntity<ReponseObject> getAll() {
        List<Category> category = categoryService.getAll();

        HttpStatus status = HttpStatus.OK;
        String message = Message.SUCCESS;

        ReponseObject responseObject = ReponseObject.builder()
                .message(message)
                .statusCode(status.value())
                .data(category)
                .build();

        return new ResponseEntity<>(responseObject, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReponseObject> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getById(id);

        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = Message.FAIL;
        Object data = null;

        if (category.isPresent()) {
            status = HttpStatus.OK;
            message = Message.SUCCESS;
            data = category;
        }

        ReponseObject responseObject = ReponseObject.builder()
                .message(message)
                .statusCode(status.value())
                .data(data)
                .build();

        return new ResponseEntity<>(responseObject, status);
    }

}
