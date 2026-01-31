package com.devtiro.blog.repositories;

import com.devtiro.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {

    Optional<User> findUserByEmail(String email);

}
