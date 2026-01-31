package com.devtiro.blog.mappers;

import com.devtiro.blog.dto.posts.AuthorDto;
import com.devtiro.blog.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorMapper {

    AuthorDto toDto(User user);
}
