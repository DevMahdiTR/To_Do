package com.ToDo.toDo.model.token;


import com.ToDo.toDo.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "tokens")

public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    private boolean revoked, expired;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
