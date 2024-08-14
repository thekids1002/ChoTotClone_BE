package com.chototclone.Exception;

import com.chototclone.Payload.Response.ResponseObject;
import com.chototclone.Utils.Message;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ResponseObject responseObject = ResponseObject.builder()
                .message(Message.VALIDATION_ERRORS)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .data(errors)
                .build();

        return ResponseEntity.badRequest().body(responseObject);
    }


    /**
     * Handle entity not found exceptions.
     *
     * @param ex the entity not found exception
     * @return a {@link ResponseEntity} with a {@link ResponseObject} containing the error message
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseObject> handleEntityNotFoundException(EntityNotFoundException ex) {
        ResponseObject responseObject = ResponseObject.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);
    }

    /**
     * Handle generic exceptions.
     *
     * @param ex the exception
     * @return a {@link ResponseEntity} with a {@link ResponseObject} containing a generic error message
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseObject> handleGlobalException(Exception ex) {
        ResponseObject responseObject = ResponseObject.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseObject);
    }

}
