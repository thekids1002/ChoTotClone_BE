package com.chototclone.Payload.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject {
    private String message;
    private int statusCode;
    private Object data;
}
