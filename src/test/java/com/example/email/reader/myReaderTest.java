package com.example.email.reader;

import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.Role;
import com.example.medicalmanagement.model.Speciality;
import com.example.medicalmanagement.model.User;
import com.example.medicalmanagement.model.UserRole;
import com.example.medicalmanagement.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class myReaderTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private myReader myReader;
//
//    @Test
//    public void testRead() {
//        // Create some test users with roles
//        User user1 = new User();
//        user1.setId(1L);
//        user1.setEmail("user1@example.com");
//        user1.setFullName("User One");
//        user1.setRoles(Arrays.asList(new Role(UserRole.DOCTOR)));
//
//        User user2 = new User();
//        user2.setId(2L);
//        user2.setEmail("user2@example.com");
//        user2.setFullName("User Two");
//        user2.setRoles(Arrays.asList(new Role(UserRole.DOCTOR)));
//
//        Speciality speciality1 = new Speciality();
//        speciality1.setId(1L);
//        speciality1.setName("General");
//
//        Speciality speciality2 = new Speciality();
//        speciality2.setId(2L);
//        speciality2.setName("General");
//
//
//        List<Speciality>specialities1=new ArrayList<>();
//        List<Speciality>specialities2=new ArrayList<>();
//
//        specialities1.add(speciality1);
//        specialities2.add(speciality2);
//
//        user1.setSpecialities(specialities1);
//        user2.setSpecialities(specialities2);
//
//        List<User> users = Arrays.asList(user1, user2);
//        Sort sort = Sort.by(Sort.Direction.ASC, "fullName");
//
//
//        when(userRepository.findByRolesUserRole(UserRole.DOCTOR, sort)).thenReturn(users);
//
//        UserDto userDto1 = myReader.read();
//        UserDto userDto2 = myReader.read();
//
//        assertNotNull(userDto1);
//        assertEquals(1L, userDto1.getId());
//        assertEquals("user1@example.com", userDto1.getEmail());
//        assertEquals("User One", userDto1.getFullName());
//        assertEquals(Collections.singletonList(UserRole.DOCTOR), userDto1.getRoles());
//
//        assertNotNull(userDto2);
//        assertEquals(2L, userDto2.getId());
//        assertEquals("user2@example.com", userDto2.getEmail());
//        assertEquals("User Two", userDto2.getFullName());
//        assertEquals(Collections.singletonList(UserRole.DOCTOR), userDto2.getRoles());
//
//        assertNull(myReader.read());
//    }}
