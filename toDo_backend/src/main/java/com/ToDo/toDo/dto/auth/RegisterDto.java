package com.ToDo.toDo.dto.auth;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class RegisterDto {

    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Invalid password. Passwords must be at least 8 characters long and include at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character.")
    private String password;

    public RegisterDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}


