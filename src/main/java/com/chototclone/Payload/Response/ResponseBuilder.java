package com.chototclone.Payload.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    /**
     * Creates a response with no data.
     *
     * @param status  The HTTP status of the response.
     * @param message The message for the response.
     * @return A ResponseEntity with the given status and message.
     */
    public static ResponseEntity<ResponseObject> createEmptyResponse(HttpStatus status, String message) {
        ResponseObject responseObject = ResponseObject.builder()
                .data(null)
                .statusCode(status.value())
                .message(message)
                .build();
        return new ResponseEntity<>(responseObject, status);
    }

    /**
     * Creates a response with data.
     *
     * @param status  The HTTP status of the response.
     * @param data    The data to include in the response.
     * @param message The message for the response.
     * @return A ResponseEntity with the given status, data, and message.
     */
    public static ResponseEntity<ResponseObject> createResponseWithData(HttpStatus status, Object data, String message) {
        ResponseObject responseObject = ResponseObject.builder()
                .data(data)
                .statusCode(status.value())
                .message(message)
                .build();
        return new ResponseEntity<>(responseObject, status);
    }

    /**
     * Creates a successful response with data.
     *
     * @param data The data to include in the response.
     * @return A ResponseEntity with status OK and the provided data.
     */
    public static ResponseEntity<ResponseObject> createSuccessResponse(Object data) {
        return createResponseWithData(HttpStatus.OK, data, "Request was successful");
    }

    /**
     * Creates an error response with an error message.
     *
     * @param errorMessage The error message for the response.
     * @return A ResponseEntity with status INTERNAL_SERVER_ERROR and the provided error message.
     */
    public static ResponseEntity<ResponseObject> createErrorResponse(String errorMessage) {
        return createResponseWithData(HttpStatus.INTERNAL_SERVER_ERROR, null, errorMessage);
    }

    /**
     * Creates a not found response with a message.
     *
     * @param message The message for the response.
     * @return A ResponseEntity with status NOT_FOUND and the provided message.
     */
    public static ResponseEntity<ResponseObject> createNotFoundResponse(String message) {
        return createResponseWithData(HttpStatus.NOT_FOUND, null, message);
    }

    /**
     * Creates a bad request response with a message.
     *
     * @param message The message for the response.
     * @return A ResponseEntity with status BAD_REQUEST and the provided message.
     */
    public static ResponseEntity<ResponseObject> createBadRequestResponse(String message) {
        return createResponseWithData(HttpStatus.BAD_REQUEST, null, message);
    }
}

