package com.ToDo.toDo.repository;

import com.ToDo.toDo.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity , UUID> {



    @Query(value = "SELECT U FROM UserEntity  U WHERE U.username = :username")
    Optional<UserEntity> fetchUserWithUsername(@Param("username") final String username);

    @Query(value = "SELECT EXISTS(SELECT U FROM UserEntity U WHERE  U.username = :username) AS RESULT")
    Boolean isUsernameRegistered(@Param("username")String username);
}
