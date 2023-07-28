package com.ToDo.toDo.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoginDto {

    private String username;
    @NotBlank
    private String password;


    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

}
