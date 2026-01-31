package com.devtiro.blog.services.impl;

import com.devtiro.blog.dto.categories.CategoryDto;
import com.devtiro.blog.dto.categories.CreateCategoryRequest;
import com.devtiro.blog.entities.Category;
import com.devtiro.blog.mappers.CategoryMapper;
import com.devtiro.blog.repositories.CategoryRepo;
import com.devtiro.blog.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> listCategories() {
        List<Category> categories = categoryRepo.findAllWithPostCount();

        return categories.stream()
                .map(categoryMapper::toDto)
                .toList();
    }


    @Override
    @Transactional
    public CategoryDto createCategory(CreateCategoryRequest createCategoryRequest) {

        if (categoryRepo.existsCategoryByNameIgnoreCase(createCategoryRequest.getName())){
            throw new IllegalArgumentException("Category already exists with name" + createCategoryRequest.getName());
        }

        Category category = categoryMapper.toEntity(createCategoryRequest);
        categoryRepo.save(category);

        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        Optional<Category> category = categoryRepo.findById(id);
        if(category.isPresent()) {
            if(!category.get().getPosts().isEmpty()) {
                throw new IllegalStateException("Category has posts associated with it");
            }
            categoryRepo.deleteById(id);
        }
    }
}
