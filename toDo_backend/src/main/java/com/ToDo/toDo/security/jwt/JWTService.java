package com.ToDo.toDo.security.jwt;

import com.ToDo.toDo.model.user.UserEntity;
import com.ToDo.toDo.security.utility.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import io.jsonwebtoken.Jwts;

@Component
public class JWTService {

    public String generateToken(@NotNull UserEntity userEntity)
    {
        String email = userEntity.getUsername();
        Date currentData = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + SecurityConstants.JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentData)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256,getSignInKey())
                .compact();
    }
    public boolean validateToken(String token) throws ExpiredJwtException
    {
        Claims claims = extractAllClaims(token);
        if(isTokenExpired(token)) throw new ExpiredJwtException(null, claims, "Token has expired. Please log in again.");
        return true;
    }

    public boolean isTokenExpired(String token)
    {
        return extractExpirationDate(token).before(new Date());
    }
    public boolean isTokenValid(String token, @NotNull @org.jetbrains.annotations.NotNull UserEntity userEntity)
    {
        final String email = extractUsernameFromJwt(token);
        return email.equals(userEntity.getUsername()) && !isTokenExpired(token);
    }
    public <T> T extractClaim(String token , @NotNull Function<Claims,T> claimResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    public Claims extractAllClaims(String token)
    {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "Token has expired. Please log in again.");
        }
    }

    public Date extractExpirationDate(String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }

    public Date extractIssuedAtDate(String token)
    {
        return extractClaim(token , Claims::getIssuedAt);
    }
    public String extractUsernameFromJwt(String token)
    {
        return extractClaim(token , Claims::getSubject);
    }


    private Key getSignInKey() {
        byte [] keyBytes = Decoders.BASE64.decode(SecurityConstants.JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
