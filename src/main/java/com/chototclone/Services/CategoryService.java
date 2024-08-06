package com.chototclone.Services;


import com.chototclone.Entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {


    Optional<Category> getById(long id);

    Optional<Category> getByNameContains(String name);

    List<Category> getAll();

    List<Category> getAllByParentCategoryId(String name);

    boolean create(Category category);

    boolean update(Category category);

    boolean delete(long id);
}
