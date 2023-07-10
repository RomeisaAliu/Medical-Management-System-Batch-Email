package com.example.email.mapper;

import com.example.email.dto.UserDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailSenderMapper implements org.springframework.jdbc.core.RowMapper<com.example.email.dto.UserDto> {
    @Override
    public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserDto.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .fullName(rs.getString("fullName"))
                .build();
    }
}
