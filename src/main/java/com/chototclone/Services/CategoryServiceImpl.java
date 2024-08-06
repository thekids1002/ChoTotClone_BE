package com.chototclone.Services;

import com.chototclone.Constant.DefaultConst;
import com.chototclone.Entities.Category;
import com.chototclone.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getAllByParentCategoryId(long id) {
        return List.of();
    }

    @Override
    public boolean create(Category createCategory) {
        Category category = new Category();
        category.setName(createCategory.getName());
        category.setParentCategory(createCategory.getParentCategory());
        category.setCreatedAt(new Date(System.currentTimeMillis()));
        category.setUpdatedAt(new Date(System.currentTimeMillis()));
        return categoryRepository.save(category).getId() != null;
    }

    @Override
    public boolean update(Category updatedCategory) {
        if (updatedCategory == null || updatedCategory.getId() == null) {
            return false;
        }

        Optional<Category> existingCategoryOpt = categoryRepository.findById(updatedCategory.getId());
        if (existingCategoryOpt.isEmpty()) {
            return false;
        }

        Category existingCategory = existingCategoryOpt.get();
        existingCategory.setName(updatedCategory.getName());
        existingCategory.setParentCategory(updatedCategory.getParentCategory());
        existingCategory.setUpdatedAt(new Date(System.currentTimeMillis()));

        try {
            categoryRepository.save(existingCategory);
            return true;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(long id) {
        Optional<Category> existingCategoryOpt = categoryRepository.findById(id);
        if (existingCategoryOpt.isEmpty()) {
            return false;
        }

        Category existingCategory = existingCategoryOpt.get();
        existingCategory.setDelFlag(DefaultConst.VALID_DEL_FLAG);
        existingCategory.setUpdatedAt(new Date(System.currentTimeMillis()));

        try {
            categoryRepository.save(existingCategory);
            return true;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}
