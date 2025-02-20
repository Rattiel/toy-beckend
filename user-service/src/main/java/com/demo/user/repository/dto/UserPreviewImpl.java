package com.demo.user.repository.dto;

import com.demo.user.repository.entity.MfaVerificationMethod;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPreviewImpl implements UserPreview {
    private String username;

    private String password;

    private String email;

    private String phone;
    
    private String firstName;

    private String lastName;

    private Boolean mfaEnabled;

    private MfaVerificationMethod mfaVerificationMethod;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
