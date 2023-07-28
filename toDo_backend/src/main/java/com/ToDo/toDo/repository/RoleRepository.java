package com.ToDo.toDo.repository;

import com.ToDo.toDo.model.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    @Query(value = "SELECT R FROM Role R WHERE R.name = :name")
    Optional<Role> fetchRoleByName(String name);

    @Query(value = "SELECT R FROM Role R WHERE R.id = :id")
    Optional<Role> fetchRoleById(long id);
    @Query(value = "DELETE FROM Role R WHERE R.id = :id")
    void deleteRoleById(long id);


    @Modifying
    @Query(value = "DELETE FROM Role R WHERE R.name = :name")
    void deleteRoleByName(String name);

}
