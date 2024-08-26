package com.chototclone.Services;


import com.chototclone.Entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {


    /**
     * Retrieves a category by its unique identifier.
     *
     * @param id The ID of the category to be retrieved.
     * @return An Optional containing the Category if found, otherwise an empty Optional.
     */
    Optional<Category> getById(long id);

    /**
     * Retrieves a category where the name contains the specified substring.
     *
     * @param name The substring to search for within the category names.
     * @return An Optional containing the Category if found, otherwise an empty Optional.
     */
    Optional<Category> getByNameContains(String name);

    /**
     * Retrieves all categories.
     *
     * @return A list of all categories.
     */
    List<Category> getAll();

    /**
     * Retrieves all categories that are children of the specified parent category.
     *
     * @param id The ID of the parent category.
     * @return A list of categories that have the specified parent category.
     */
    List<Category> getAllByParentCategoryId(long id);

    /**
     * Creates a new category.
     *
     * @param category The Category object to be created.
     * @return true if the category was created successfully, otherwise false.
     */
    boolean create(Category category);

    /**
     * Updates an existing category.
     *
     * @param category The Category object containing updated information.
     * @return true if the category was updated successfully, otherwise false.
     */
    boolean update(Category category);

    /**
     * Deletes a category by its unique identifier.
     *
     * @param id The ID of the category to be deleted.
     * @return true if the category was deleted successfully, otherwise false.
     */
    boolean delete(Category category);
}
