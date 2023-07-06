package com.example.email.repository;

import com.example.email.model.Speciality;
import com.example.email.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialityRepository extends JpaRepository<User, Long> {

    List<Speciality> findByEmail(String email);
}
