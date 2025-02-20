package com.demo.user.repository.dto;

import com.demo.user.repository.entity.MfaMethod;

import java.time.LocalDateTime;

public interface UserPreview {
    String getUsername();

    String getPassword();

    String getEmail();

    String getPhone();

    String getFirstName();

    String getLastName();

    Boolean getMfaEnabled();

    MfaMethod getMfaMethod();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
