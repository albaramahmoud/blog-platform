package com.devtiro.blog.mappers;

import com.devtiro.blog.dto.tags.TagResponse;
import com.devtiro.blog.entities.Post;
import com.devtiro.blog.entities.PostStatus;
import com.devtiro.blog.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagResponse toDto(Tag tag);

    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<Post> posts){
        if (null == posts){
            return 0;
        } else{
            return (int) posts.stream()
                    .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                    .count();
        }
    }
}
