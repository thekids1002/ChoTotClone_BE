package com.chototclone.Payload.Response.Category;

import lombok.Data;

@Data
public class CategoryResponse {
    private Long id;

    private String name;

    private Long parentCategoryId;
}
