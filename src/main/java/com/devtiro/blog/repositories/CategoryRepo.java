package com.devtiro.blog.repositories;

import com.devtiro.blog.dto.categories.CategoryDto;
import com.devtiro.blog.dto.tags.TagResponse;
import com.devtiro.blog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepo extends JpaRepository<Category, UUID> {


    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPostCount();

    boolean existsCategoryByNameIgnoreCase(String name);

    boolean existsCategoryById(UUID id);

    @Query("SELECT new com.devtiro.blog.dto.categories.CategoryDto(c.id, c.name, CAST(SIZE(c.posts) as int)) FROM Category c")
    List<CategoryDto> findAllCategoriesWithCount();
}
