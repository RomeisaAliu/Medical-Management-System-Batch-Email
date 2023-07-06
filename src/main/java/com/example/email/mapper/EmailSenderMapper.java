package com.example.email.mapper;

import com.example.email.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailSenderMapper implements org.springframework.jdbc.core.RowMapper<com.example.email.model.User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return mapRow(rs, rowNum);
    }
}
