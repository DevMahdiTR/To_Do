package com.ToDo.toDo.controller.token;

import com.ToDo.toDo.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/token")
public class TokenController {


    private final TokenService tokenService;

    @Autowired
    public TokenController (TokenService tokenService)
    {
        this.tokenService = tokenService;
    }

    @GetMapping("/validate/{token}")
    public ResponseEntity<Boolean> validateToken(@PathVariable("token") final String token)
    {
        return tokenService.isTokenValidAndExist(token);
    }
}
