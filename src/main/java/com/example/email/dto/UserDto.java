package com.example.email.dto;

import com.example.email.model.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String email;

    private String fullName;

    private List<UserRole> roles;

    private List<String> specialities;

}