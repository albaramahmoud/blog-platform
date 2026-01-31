package com.devtiro.blog.repositories;

import com.devtiro.blog.dto.tags.TagResponse;
import com.devtiro.blog.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TagRepo extends JpaRepository<Tag, UUID> {
    List<Tag> findByNameIn(Collection<String> names);

    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.posts")
    List<Tag> findAllWithPostCount();

    @Query("SELECT new com.devtiro.blog.dto.tags.TagResponse(t.id, t.name, CAST(SIZE(t.posts) as int)) FROM Tag t")
    List<TagResponse> findAllTagsWithCount();

}
