package com.ToDo.toDo.service.token;

import com.ToDo.toDo.repository.TokenRepository;
import com.ToDo.toDo.security.jwt.JWTService;
import com.ToDo.toDo.security.utility.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenService {


    private final TokenRepository tokenRepository;
    private final JWTService jwtService;

    @Autowired
    public TokenService (TokenRepository tokenRepository,JWTService jwtService)
    {
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
    }


    public ResponseEntity<Boolean> isTokenValidAndExist(@NotNull final String token)
    {
        try{
            if(!jwtService.isTokenExpired(token))
            {
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            if(!tokenRepository.isTokenValidAndExist(token))
            {
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            return new ResponseEntity<>(false, HttpStatus.OK);
        }catch (Exception e)
        {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

    }
}
