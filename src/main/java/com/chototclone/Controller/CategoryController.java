package com.chototclone.Controller;

import com.chototclone.Constant.DefaultConst;
import com.chototclone.Entities.Category;
import com.chototclone.Payload.Request.Category.CreateRequest;
import com.chototclone.Payload.Request.Category.UpdateRequest;
import com.chototclone.Payload.Response.Category.CategoryResponse;
import com.chototclone.Payload.Response.ReponseObject;
import com.chototclone.Services.CategoryService;
import com.chototclone.Utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Retrieves all categories and returns a response containing the list of category details.
     *
     * @return ResponseEntity containing the response object with a list of all categories and a success message.
     */
    @GetMapping({"", "/"})
    public ResponseEntity<ReponseObject> getAll() {
        List<Category> categories = categoryService.getAll();
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(this::convertToResponse)
                .toList();

        HttpStatus status = HttpStatus.OK;
        String message = Message.SUCCESS;

        ReponseObject responseObject = ReponseObject.builder()
                .message(message)
                .statusCode(status.value())
                .data(categoryResponses)
                .build();

        return new ResponseEntity<>(responseObject, status);
    }


    /**
     * Retrieves a category by its ID and returns a response indicating the result of the operation.
     *
     * @param id The ID of the category to be retrieved.
     * @return ResponseEntity containing the response object with the category details or an error message.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReponseObject> getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryOpt = categoryService.getById(id);
        CategoryResponse categoryResponse;

        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = Message.FAIL;
        Object data = null;

        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            categoryResponse = convertToResponse(category);

            status = HttpStatus.OK;
            message = Message.SUCCESS;
            data = categoryResponse;
        }

        ReponseObject responseObject = ReponseObject.builder()
                .message(message)
                .statusCode(status.value())
                .data(data)
                .build();

        return new ResponseEntity<>(responseObject, status);
    }

    /**
     * Creates a new category based on the provided request details.
     *
     * @param createRequest The request body containing the details of the category to be created.
     * @return ResponseEntity containing the response object with the result of the creation operation.
     */
    @PostMapping("/create")
    public ResponseEntity<ReponseObject> createCategory(@RequestBody CreateRequest createRequest) {
        Optional<Category> parentCategory = Optional.empty();
        if (createRequest.getParentCategoryId() != DefaultConst.DEFAULT_PARENT_CATEGORY) {
            parentCategory = categoryService.getById(createRequest.getParentCategoryId());
        }

        Category category = new Category();
        category.setName(createRequest.getName());
        category.setParentCategory(parentCategory.orElse(null));

        boolean isCreated = categoryService.create(category);
        return getReponseObjectResponseEntity(createRequest, isCreated);
    }

    /**
     * Updates a category based on the provided update request.
     *
     * @param updateRequest The request body containing the details of the category to be updated.
     * @return ResponseEntity containing the response object with the result of the update operation.
     */
    @PostMapping("/update")
    public ResponseEntity<ReponseObject> updateCategory(@RequestBody UpdateRequest updateRequest) {
        Optional<Category> parentCategory = Optional.empty();
        if (updateRequest.getParentCategoryId() != DefaultConst.DEFAULT_PARENT_CATEGORY) {
            long id = updateRequest.getParentCategoryId();
            parentCategory = categoryService.getById(id);
        }

        Category category = new Category();
        category.setId(updateRequest.getId());
        category.setName(updateRequest.getName());
        category.setParentCategory(parentCategory.orElse(null));
        boolean isCreated = categoryService.update(category);
        return getReponseObjectResponseEntity(updateRequest, isCreated);
    }

    /**
     * Deletes a category by its ID and returns a response indicating the result of the operation.
     *
     * @param id The ID of the category to be deleted.
     * @return ResponseEntity containing the response with a message, HTTP status code, and data.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ReponseObject> deleteCategory(@PathVariable Long id) {
        boolean isDeleted = categoryService.delete(id);
        HttpStatus httpStatus = isDeleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        String message = isDeleted ? Message.SUCCESS : Message.FAIL;

        ReponseObject responseObject = ReponseObject.builder()
                .message(message)
                .statusCode(httpStatus.value())
                .data(null)
                .build();

        return new ResponseEntity<>(responseObject, httpStatus);
    }


    /**
     * Constructs a ResponseEntity containing a ReponseObject based on the result of a creation or update operation.
     *
     * @param request            The request object used for logging and response details.
     * @param isCreatedOrUpdated Indicates whether the creation or update operation was successful.
     * @return ResponseEntity containing the constructed ReponseObject with the result of the operation.
     */
    private ResponseEntity<ReponseObject> getReponseObjectResponseEntity(@RequestBody CreateRequest request, boolean isCreatedOrUpdated) {
        HttpStatus status = isCreatedOrUpdated ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR;
        String message = isCreatedOrUpdated ? Message.SUCCESS : Message.FAIL;
        Object data = isCreatedOrUpdated ? request : null;

        ReponseObject responseObject = ReponseObject.builder()
                .message(message)
                .statusCode(status.value())
                .data(data)
                .build();

        return new ResponseEntity<>(responseObject, status);
    }

    /**
     * Converts a Category entity into a CategoryResponse DTO.
     *
     * @param category The Category entity to be converted.
     * @return CategoryResponse DTO containing the category details.
     */
    private CategoryResponse convertToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setParentCategoryId(category.getParentCategory() != null ? category.getParentCategory().getId() : null);
        return response;
    }

    /**
     * Handles the request to retrieve all categories that have a specific parent category ID.
     * Builds a response object with the list of categories or an error message if an exception occurs.
     *
     * @param parentCategoryId The ID of the parent category to filter categories by.
     * @return ResponseEntity containing the ReponseObject with status, message, and data.
     */

    @GetMapping("/getAllCategory/{parentCategoryId}")
    public ResponseEntity<ReponseObject> getAllByParentCategoryId(@PathVariable Long parentCategoryId) {
        HttpStatus status = HttpStatus.CREATED;
        String message = Message.SUCCESS;
        ReponseObject reponseObject = ReponseObject.builder()
                .statusCode(status.value())
                .message(message)
                .data(null)
                .build();
        try {
            List<Category> categoryList = this.categoryService.getAllByParentCategoryId(parentCategoryId);
            List<CategoryResponse> categoryResponses = categoryList.stream()
                    .map(this::convertToResponse)
                    .toList();
            reponseObject.setData(categoryResponses);
            return new ResponseEntity<>(reponseObject, status);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(reponseObject, status);
        }
    }
}
