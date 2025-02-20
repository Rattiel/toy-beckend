package com.demo.user.repository;

import com.demo.user.repository.dto.UserPreview;

import java.util.Optional;

public interface UserRepository {
    Optional<UserPreview> findPreviewByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
