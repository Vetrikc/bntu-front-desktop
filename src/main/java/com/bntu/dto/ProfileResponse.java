package com.bntu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileResponse {
    private boolean success;
    private String message;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String birthDate;
    private String bio;
    private String createdAt;
    private String updatedAt;
}