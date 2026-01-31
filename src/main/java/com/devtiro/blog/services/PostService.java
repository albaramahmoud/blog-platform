package com.devtiro.blog.services;

import com.devtiro.blog.dto.posts.*;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<PostDto> getPosts(UUID categoryId, UUID tagId);

    PostDto getPost(UUID id);

    PostDto createPost(UUID userId,CreatePostRequestDto createPostRequestDto);

    List<PostDto> getDraftPosts(UUID userId);
    PostDto updatePost(UUID id, UpdatePostRequestDto updatePostRequestDto);
    void deletePost(UUID id);
}
