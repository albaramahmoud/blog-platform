package com.devtiro.blog.services;

import com.devtiro.blog.dto.categories.CategoryDto;
import com.devtiro.blog.dto.categories.CreateCategoryRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CategoryService {
    public List<CategoryDto> listCategories() ;

        public CategoryDto createCategory(CreateCategoryRequest createCategoryRequest) ;

    void deleteCategory(UUID id);
}
