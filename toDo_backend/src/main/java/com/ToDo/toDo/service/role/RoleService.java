package com.ToDo.toDo.service.role;

import com.ToDo.toDo.exceptions.ResourceNotFoundException;
import com.ToDo.toDo.model.role.Role;
import com.ToDo.toDo.repository.RoleRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }


    public ResponseEntity<String> createRole(final Role role)
    {
        roleRepository.save(role);
        final String successMessage = String.format("The role : %s has been successfully created.",role.getName());
        return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
    }

    public ResponseEntity<Role> fetchRoleById(long roleId)
    {
        final Role role = getRoleById(roleId);
        return new ResponseEntity<>(role,HttpStatus.OK);
    }
    public ResponseEntity<Role> fetchRoleByName(String roleName)
    {
        final Role role = roleRepository.fetchRoleByName(roleName).orElseThrow(()-> new ResourceNotFoundException(String.format("Role with ID %s could not be found.", roleName)));
        return new ResponseEntity<>(role,HttpStatus.OK);
    }

    public ResponseEntity<String> updateRoleById(final long roleId, final @NotNull Role roleDetails)
    {
        final Role role = getRoleById(roleId);

        role.setName(roleDetails.getName());
        final Role updatedRole = roleRepository.save(role);

        final String successMessage = java.lang.String.format("Role with name : %s is updated to : %s",role.getName(),updatedRole.getName());
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }



    @Transactional
    public ResponseEntity<String> deleteRoleById(long roleId) {
        final Role role = getRoleById(roleId);

        roleRepository.deleteRoleById(roleId);
        final String successMessage = String.format("The role with ID %d has been successfully deleted", roleId);
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

    private Role getRoleById(final long roleId)
    {
        return roleRepository.fetchRoleById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Role with ID %d could not be found.", roleId)));
    }

    public Role getRoleByName(final String roleName)
    {
        return roleRepository.fetchRoleByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Role with NAME %s could not be found.", roleName)));
    }

}
