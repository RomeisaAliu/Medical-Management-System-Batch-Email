package com.example.email.repository;

import com.example.email.model.User;
import com.example.email.model.UserRole;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        List<User> findByRolesUserRole(UserRole role, Sort sort);
        @Query("SELECT u.email FROM User u WHERE u.Id = :doctorId")
        String findDoctorEmailById(Long doctorId);
}