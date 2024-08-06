package com.chototclone.Repository;

import com.chototclone.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Finds a category by its unique identifier.
     *
     * @param id The ID of the category to be retrieved.
     * @return An Optional containing the Category if found, otherwise an empty Optional.
     */
    Optional<Category> findById(long id);

    /**
     * Finds a category where the name contains the specified substring.
     *
     * @param name The substring to search for within the category names.
     * @return An Optional containing the Category if found, otherwise an empty Optional.
     */
    Optional<Category> findByNameContaining(String name);

    /**
     * Retrieves all categories.
     *
     * @return A list of all categories.
     */
    List<Category> findAll();

    /**
     * Finds all categories with the specified parent category ID.
     *
     * @param parentCategoryId The ID of the parent category.
     * @return A list of categories that have the specified parent category.
     */
    List<Category> findAllByParentCategoryId(long parentCategoryId);
}
