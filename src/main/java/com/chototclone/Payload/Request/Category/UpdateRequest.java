package com.chototclone.Payload.Request.Category;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class UpdateRequest extends CreateRequest {
    private long id;
}
