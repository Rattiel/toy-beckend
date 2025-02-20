package com.demo.user.repository.dto;

import java.time.LocalDateTime;

public interface UserPreview {
    String getUsername();

    String getPassword();

    String getEmail();

    String getFirstName();

    String getLastName();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
