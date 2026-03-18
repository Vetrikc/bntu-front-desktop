package com.bntu.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileRequest {
    private String action;
    private String token;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String birthDate;
    private String bio;
}