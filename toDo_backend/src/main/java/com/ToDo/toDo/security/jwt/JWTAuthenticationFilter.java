package com.ToDo.toDo.security.jwt;

import com.ToDo.toDo.exceptions.ExpiredTokenException;
import com.ToDo.toDo.exceptions.InvalidTokenException;
import com.ToDo.toDo.exceptions.ResourceNotFoundException;
import com.ToDo.toDo.exceptions.RevokedTokenException;
import com.ToDo.toDo.model.user.UserEntity;
import com.ToDo.toDo.repository.TokenRepository;
import com.ToDo.toDo.security.utility.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        final String jwtToken = authHeader.substring(7);

        if(!jwtService.validateToken(jwtToken))  {

            filterChain.doFilter(request,response) ;
            return;
        }

        String username = jwtService.extractUsernameFromJwt(jwtToken);

        if(username == null || SecurityContextHolder.getContext().getAuthentication() != null )  {filterChain.doFilter(request,response);return;}

        UserEntity userEntity = (UserEntity) this.customUserDetailsService.loadUserByUsername(username);

        var isTokenValid = tokenRepository.findByToken(jwtToken).map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
        var tokenSaved = tokenRepository.findByToken(jwtToken).orElse(null);
        if(!isTokenValid)
        {
            throw new ResourceNotFoundException("Token not found.");
        }

        if(jwtService.isTokenExpired(jwtToken))
        {
            throw new ExpiredTokenException("Token has expired.");
        }
        if(tokenSaved.isRevoked())
        {
            throw new RevokedTokenException("Token has been revoked");
        }
        if(!jwtService.isTokenValid(jwtToken, userEntity))
        {
            throw new InvalidTokenException("Invalid token");
        }



        if(!jwtService.isTokenValid(jwtToken, userEntity) || !isTokenValid )  {filterChain.doFilter(request,response);return;}

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEntity,null,userEntity.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request,response);
    }
}
