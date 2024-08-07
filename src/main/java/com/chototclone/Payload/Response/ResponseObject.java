package com.chototclone.Payload.Response;

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
