package com.streetarts.backend.dto;

import com.streetarts.backend.User;

public class AuthResponse {
    public Integer id;
    public String name;
    public String surname;
    public String birthDate;
    public String phone;
    public String username;
    public String email;
    public String role;

    public AuthResponse(User user) {
        this.id = user.getUserId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.phone = user.getPhone();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().name();

        this.birthDate = user.getBirthDate() != null
                ? user.getBirthDate().toString()
                : "";
    }
}