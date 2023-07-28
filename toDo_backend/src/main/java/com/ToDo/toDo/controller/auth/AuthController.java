package com.ToDo.toDo.controller.auth;

import com.ToDo.toDo.dto.auth.AuthResponseDto;
import com.ToDo.toDo.dto.auth.LoginDto;
import com.ToDo.toDo.dto.auth.RegisterDto;
import com.ToDo.toDo.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {


    private final AuthService authService;


    public AuthController (AuthService authService)
    {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterDto registerDto)
    {
        return authService.register(registerDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto loginDto)
    {
        return authService.login(loginDto);
    }

}