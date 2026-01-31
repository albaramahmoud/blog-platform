package com.devtiro.blog.mappers;

import com.devtiro.blog.dto.categories.CategoryDto;
import com.devtiro.blog.dto.categories.CreateCategoryRequest;
import com.devtiro.blog.entities.Category;
import com.devtiro.blog.entities.Post;
import com.devtiro.blog.entities.PostStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);


    Category toEntity(CreateCategoryRequest createCategoryRequest);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts){
        if (null == posts){
            return 0;
        } else {
            return posts.stream()
                    .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                    .count();
        }
    }



}
