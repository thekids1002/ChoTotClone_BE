package com.chototclone.Repository;

import com.chototclone.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(long id);

    Optional<Category> findByNameContaining(String name);

    List<Category> findAll();

}
