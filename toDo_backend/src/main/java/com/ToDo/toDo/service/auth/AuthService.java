package com.ToDo.toDo.service.auth;

import com.ToDo.toDo.dto.auth.AuthResponseDto;
import com.ToDo.toDo.dto.auth.LoginDto;
import com.ToDo.toDo.dto.auth.RegisterDto;
import com.ToDo.toDo.exceptions.ResourceNotFoundException;
import com.ToDo.toDo.model.role.Role;
import com.ToDo.toDo.model.token.Token;
import com.ToDo.toDo.model.token.TokenType;
import com.ToDo.toDo.model.user.UserEntity;
import com.ToDo.toDo.repository.RoleRepository;
import com.ToDo.toDo.repository.TokenRepository;
import com.ToDo.toDo.repository.UserEntityRepository;
import com.ToDo.toDo.security.jwt.JWTService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import java.util.Collections;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserEntityRepository userEntityRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final Validator validator;


    @Autowired
    public AuthService(
            AuthenticationManager authenticationManager,
            UserEntityRepository userEntityRepository,
            RoleRepository repository,
            PasswordEncoder passwordEncoder,
            JWTService jwtService,
            TokenRepository tokenRepository,
            Validator validator) {
        this.authenticationManager = authenticationManager;
        this.userEntityRepository = userEntityRepository;
        this.roleRepository = repository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.validator = validator;
    }

    public ResponseEntity<AuthResponseDto> register(@NotNull RegisterDto registerDto) {

        if (userEntityRepository.isUsernameRegistered(registerDto.getUsername())) {
            throw new IllegalArgumentException("Sorry, that username is already taken. Please choose a different one.");
        }

        Role role = roleRepository.fetchRoleByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found [USER]."));

        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singletonList(role));

        UserEntity savedUser = userEntityRepository.save(user);
        String jwtToken = jwtService.generateToken(user);

        saveUserToken(savedUser, jwtToken);

        AuthResponseDto authResponseDto =
                AuthResponseDto.builder()
                        .token(jwtToken)
                        .build();
        return new ResponseEntity<AuthResponseDto>(authResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<AuthResponseDto> login(@NotNull LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = userEntityRepository.fetchUserWithUsername(loginDto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("The email address specified was not found in our system."));

        String jwtToken = generateAndSaveToken(user);

        AuthResponseDto authResponseDto =
                AuthResponseDto.builder()
                        .token(jwtToken)
                        .build();

        return ResponseEntity.ok(authResponseDto);
    }

    private String generateAndSaveToken(UserEntity user) {
        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return jwtToken;
    }

    private void saveUserToken(@NotNull UserEntity userEntity, @NotNull String jwtToken) {
        var token = Token.builder()
                .userEntity(userEntity)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(@NotNull UserEntity userEntity) {
        var validUserTokens = tokenRepository.fetchAllValidTokenByUserId(userEntity.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
