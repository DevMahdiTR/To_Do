package com.ToDo.toDo.controller.role;

import com.ToDo.toDo.model.role.Role;
import com.ToDo.toDo.service.role.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/role")
public class RoleController {
    private final RoleService roleService;
    @Autowired
    public RoleController(RoleService roleService)
    {
        this.roleService  = roleService;
    }


    @PostMapping("/add")
    public ResponseEntity<String> createRole(@RequestBody @Valid Role role)
    {
        return roleService.createRole(role);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id")int  id)
    {
        return roleService.fetchRoleById(id);
    }


    @PutMapping("/update/id/{id}")
    public ResponseEntity<String> updateRoleById(@PathVariable("id") int id,@RequestBody @Valid Role roleDetails)
    {
        return roleService.updateRoleById(id,roleDetails);
    }
    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<String> deleteRoleById(@PathVariable("id") int id)
    {
        return roleService.deleteRoleById(id);
    }
}
