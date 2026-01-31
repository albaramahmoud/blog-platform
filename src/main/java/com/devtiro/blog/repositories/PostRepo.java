package com.devtiro.blog.repositories;

import com.devtiro.blog.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PostRepo extends JpaRepository<Post, UUID> {
    Collection<Object> findAllByCategoryAndTags(Category category, Set<Tag> tags);

    List<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus status, Category category, Tag tag);
    List<Post> findAllByStatusAndTagsContaining(PostStatus status, Tag tag);
    List<Post> findAllByStatusAndCategory(PostStatus status, Category category);
    List<Post> findAllByStatus(PostStatus status);

    List<Post> findAllByStatusAndAuthor(PostStatus postStatus, User user);
}
