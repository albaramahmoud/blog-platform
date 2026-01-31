package com.devtiro.blog.repositories;

import com.devtiro.blog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepo extends JpaRepository<Category, UUID> {


    // if we use findAll() method there is well be N+1 problem
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPostCount();

    boolean existsCategoryByNameIgnoreCase(String name);

    boolean existsCategoryById(UUID id);

}
