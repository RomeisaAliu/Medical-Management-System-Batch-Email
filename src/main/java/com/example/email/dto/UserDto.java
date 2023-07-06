package com.example.email.dto;

import com.example.email.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String email;

    private String fullName;

    private List<UserRole> roles;

    private List<String> specialities;

    private boolean emailSent;
}