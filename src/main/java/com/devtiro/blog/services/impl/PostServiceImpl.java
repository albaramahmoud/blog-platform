package com.devtiro.blog.services.impl;

import com.devtiro.blog.dto.posts.*;
import com.devtiro.blog.entities.*;
import com.devtiro.blog.mappers.PostMapper;
import com.devtiro.blog.mappers.TagMapper;
import com.devtiro.blog.repositories.CategoryRepo;
import com.devtiro.blog.repositories.PostRepo;
import com.devtiro.blog.repositories.TagRepo;
import com.devtiro.blog.repositories.UserRepo;
import com.devtiro.blog.services.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final PostRepo postRepo;
    private final CategoryRepo categoryRepo;
    private final TagRepo tagRepo;
    private final UserRepo userRepo;
    private final TagMapper tagMapper;

    private static final int CHARS_PER_MINUTE = 2000;
    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getPosts(UUID categoryId, UUID tagId) {

        Optional<Category> category = (categoryId != null) ? categoryRepo.findById(categoryId) : Optional.empty();
        Optional<Tag> tag = (tagId != null) ? tagRepo.findById(tagId) : Optional.empty();

        if (categoryId != null && category.isEmpty()) {
            throw new EntityNotFoundException("Category with id " + categoryId + " not found");
        }
        if (tagId != null && tag.isEmpty()) {
            throw new EntityNotFoundException("Tag with id " + tagId + " not found");
        }

        if (category.isPresent() && tag.isPresent()) {
            return postRepo.findAllByStatusAndCategoryAndTagsContaining(
                            PostStatus.PUBLISHED, category.get(), tag.get())
                    .stream()
                    .map(postMapper::toDto)
                    .toList();
        } else if (category.isPresent()) {
            return postRepo.findAllByStatusAndCategory(
                            PostStatus.PUBLISHED, category.get())
                    .stream()
                    .map(postMapper::toDto)
                    .toList();
        } else if (tag.isPresent()) {
            return postRepo.findAllByStatusAndTagsContaining(
                            PostStatus.PUBLISHED, tag.get())
                    .stream()
                    .map(postMapper::toDto)
                    .toList();
        } else {
            return postRepo.findAllByStatus(PostStatus.PUBLISHED)
                    .stream()
                    .map(postMapper::toDto)
                    .toList();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public PostDto getPost(UUID id) {
        return postMapper.toDto(postRepo.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Post with id " + id + " not found")
        ));
    }

    @Override
    @Transactional
    public PostDto createPost(UUID userId, CreatePostRequestDto createPostRequestDto) {
        User author = userRepo.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id " + userId + " not found")
        );

        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);


        Category category = categoryRepo.findById(createPostRequest.getCategoryId()).orElseThrow(
                () -> new EntityNotFoundException("Category with id " + createPostRequest.getCategoryId() + " not found")
        );
        List<Tag> tagList = tagRepo.findAllById(createPostRequest.getTagIds());
        Set<Tag> tags = Set.copyOf(tagList);

        Post post = Post.builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .status(createPostRequest.getStatus())
                .readingTime((int) Math.ceil((double) createPostRequest.getContent().length() / CHARS_PER_MINUTE))
                .author(author)
                .category(category)
                .tags(tags)
                .build();


        return postMapper.toDto(postRepo.save(post));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getDraftPosts(UUID userId) {
        User author = userRepo.findById(userId).orElseThrow( () -> new EntityNotFoundException("User with id " + userId + " not found"));
        List<Post> posts = postRepo.findAllByStatusAndAuthor(PostStatus.DRAFT, author);
        return posts.stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public PostDto updatePost(UUID id, UpdatePostRequestDto updatePostRequestDto) {
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post post =  postRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Post with id " + id + " not found"));

        Category category = categoryRepo.findById(updatePostRequest.getCategoryId()).orElseThrow(
                () -> new EntityNotFoundException("Category with id " + updatePostRequest.getCategoryId() + " not found")
        );
        Set<Tag> tags = new HashSet<>(tagRepo.findAllById(updatePostRequest.getTagIds()));
        post.setTitle(updatePostRequest.getTitle());
        post.setTags(tags);
        post.setCategory(category);
        post.setContent(updatePostRequest.getContent());
        post.setStatus(updatePostRequest.getStatus());

        return postMapper.toDto(postRepo.save(post));
    }

    @Override
    public void deletePost(UUID id) {
        Optional<Post> post = postRepo.findById(id);
        if(post.isEmpty()){
            return;
        }

        Category category = post.get().getCategory();
        Set<Tag> tags = post.get().getTags();
        category.deletePost(post.get());
        tags.forEach(tag -> tag.deletePost(post.get()));
        postRepo.deleteById(id);
    }
}
