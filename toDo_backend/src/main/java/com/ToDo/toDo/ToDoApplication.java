package com.ToDo.toDo;

import com.ToDo.toDo.model.role.Role;
import com.ToDo.toDo.service.role.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@RequiredArgsConstructor
@SpringBootApplication
public class ToDoApplication {

	private final RoleService roleService;

	public static void main(String[] args) {
		SpringApplication.run(ToDoApplication.class, args);
	}

	@PostConstruct
	private void initProject()
	{
		initRoles();

	}

	private void initRoles()
	{
		roleService.createRole(new Role("ADMIN"));
		roleService.createRole(new Role("USER"));
	}

}
