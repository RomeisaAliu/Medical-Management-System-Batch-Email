package com.example.email.repository;

import com.example.email.dto.UserDto;
import com.example.email.model.User;
import com.example.email.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<UserDto> findByRolesUserRole(UserRole role);

}