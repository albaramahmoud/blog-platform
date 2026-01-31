package com.devtiro.blog.repositories;

import com.devtiro.blog.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TagRepo extends JpaRepository<Tag, UUID> {
    List<Tag> findByNameIn(Collection<String> names);
}
