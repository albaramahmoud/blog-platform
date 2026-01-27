package com.devtiro.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepo extends JpaRepository<, UUID> {
}
