package com.ToDo.toDo.controller.task;


import com.ToDo.toDo.dto.task.TaskDto;
import com.ToDo.toDo.model.task.Task;
import com.ToDo.toDo.service.task.TaskService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/task")
public class TaskController {


    private final TaskService taskService;


    @Autowired
    public TaskController(TaskService taskService)
    {
        this.taskService = taskService;

    }


    @PostMapping("/create_task")
    public ResponseEntity<TaskDto> createTask(@AuthenticationPrincipal UserDetails userDetails,@Valid @RequestBody Task task)
    {
        return  taskService.createTask(userDetails,task);
    }

    @PutMapping("/modify/task_id/{taskId}")
    public ResponseEntity<String> modifyTaskById(@AuthenticationPrincipal UserDetails userDetails , @PathVariable("taskId") final long taskId, @Valid @NotNull @RequestBody Task taskDetails)
    {
        return taskService.modifyTaskById(userDetails,taskId,taskDetails);
    }

    @PutMapping("/modify/finish/task_id/{taskId}")
    public ResponseEntity<String> finishTaskById(@AuthenticationPrincipal UserDetails userDetails , @PathVariable("taskId") final long taskId)
    {
        return taskService.finishTaskById(userDetails,taskId);
    }



    @GetMapping("/get/all")
    public ResponseEntity<List<TaskDto>> fetchAllTaskOfUser(@AuthenticationPrincipal UserDetails userDetails)
    {
        return taskService.fetchAllTaskOfUser(userDetails);
    }

    @DeleteMapping("/delete/task_id/{taskId}")
    public ResponseEntity<String> deleteTaskById(@AuthenticationPrincipal UserDetails userDetails ,@PathVariable("taskId") final long taskId)
    {
        return taskService.deleteTaskById(userDetails , taskId);
    }

}
