package com.streetarts.backend.dto;

import com.streetarts.backend.User;

public class AuthResponse {
    public Integer id;
    public String username;
    public String email;
    public String role;

    public AuthResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().name();
    }
}