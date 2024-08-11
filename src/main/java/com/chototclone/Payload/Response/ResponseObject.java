package com.chototclone.Payload.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
