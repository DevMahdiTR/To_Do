package com.ToDo.toDo.service.task;

import com.ToDo.toDo.dto.task.TaskDto;
import com.ToDo.toDo.exceptions.ResourceNotFoundException;
import com.ToDo.toDo.exceptions.UnauthorizedActionException;
import com.ToDo.toDo.model.task.Task;
import com.ToDo.toDo.model.user.UserEntity;
import com.ToDo.toDo.repository.TaskRepository;
import com.ToDo.toDo.service.user.UserEntityService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class TaskService {


    private final TaskRepository taskRepository;
    private final UserEntityService userEntityService;

    @Autowired
    public TaskService(TaskRepository taskRepository,UserEntityService userEntityService)
    {
        this.taskRepository  = taskRepository;
        this.userEntityService =  userEntityService;
    }

    @Transactional
    public ResponseEntity<String> finishTaskById (@NotNull UserDetails userDetails , final long taskId )
    {
        final Task currentTask = getTaskById(taskId);
        final UserEntity currentUser = userEntityService.getUserEntityByUsername(userDetails.getUsername());
        if(currentUser !=  currentTask.getUserEntity())
        {
            throw new UnauthorizedActionException("This user is unauthorized. Access denied.");
        }
        currentTask.setDone(true);
        taskRepository.save(currentTask);
        final String successResponse ="the task is updated successfully.";
        return new ResponseEntity<>(successResponse, HttpStatus.OK);

    }
    public ResponseEntity<TaskDto> createTask(@NotNull UserDetails userDetails , @NotNull Task task )
    {
        final UserEntity currentUser = userEntityService.getUserEntityByUsername(userDetails.getUsername());
        task.setUserEntity(currentUser);
        final Task savedTask = taskRepository.save(task);


        return new ResponseEntity<>(new TaskDto(savedTask), HttpStatus.OK);
    }
    public ResponseEntity<Task> fetchTaskById( @NotNull UserDetails userDetails, final long taskId)
    {
        final Task currentTask = getTaskById(taskId);
        final UserEntity currentUser = userEntityService.getUserEntityByUsername(userDetails.getUsername());
        if(currentUser !=  currentTask.getUserEntity())
        {
            throw new UnauthorizedActionException("This user is unauthorized. Access denied.");
        }
        return  new ResponseEntity<Task>(currentTask, HttpStatus.OK);
    }

    public ResponseEntity<List<TaskDto>> fetchAllTaskOfUser(@NotNull UserDetails userDetails)
    {
        final UserEntity currentUser = userEntityService.getUserEntityByUsername(userDetails.getUsername());
        final List<TaskDto> taskDtoList = taskRepository.fetchUserTasksById(currentUser.getId()).stream().map(TaskDto::new).toList();

        return new ResponseEntity<>(taskDtoList,HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deleteTaskById(@NotNull UserDetails userDetails , final long taskId)
    {
        final UserEntity currentUser = userEntityService.getUserEntityByUsername(userDetails.getUsername());
        final Task currentTask = getTaskById(taskId);
        if(currentUser != currentTask.getUserEntity())
        {
            throw new UnauthorizedActionException("This user is unauthorized. Access denied.");
        }
        taskRepository.deleteTaskById(taskId);
        final String successResponse = String.format("The task with ID : %d deleted successfully.",taskId);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    public ResponseEntity<String> modifyTaskById(@NotNull UserDetails userDetails, final long taskId,@NotNull Task taskDetails)
    {
        final UserEntity currentUser = userEntityService.getUserEntityByUsername(userDetails.getUsername());
        final Task currentTask = getTaskById(taskId);
        if(currentUser != currentTask.getUserEntity())
        {
            throw new UnauthorizedActionException("This user is unauthorized. Access denied.");
        }
        currentTask.setTask(taskDetails.getTask());
        currentTask.setDone(taskDetails.isDone());
        taskRepository.save(currentTask);
        final String successResponse = String.format("The task with ID : %d updated successfully.",taskId);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
    public Task getTaskById(final long taskId)
    {
        return taskRepository.fetchTaskById(taskId).orElseThrow(()->new ResourceNotFoundException(String.format("The task with ID : %d could not be found.",taskId)));
    }

}
