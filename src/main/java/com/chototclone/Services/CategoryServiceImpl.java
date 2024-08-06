package com.chototclone.Services;

import com.chototclone.Entities.Category;
import com.chototclone.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Optional<Category> getById(long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> getByNameContains(String name) {
        return Optional.empty();
    }

    @Override
    public List<Category> getAll() {
        return List.of();
    }

    @Override
    public List<Category> getAllByParentCategoryId(String name) {
        return List.of();
    }

    @Override
    public boolean create(Category category) {
        return false;
    }

    @Override
    public boolean update(Category category) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
