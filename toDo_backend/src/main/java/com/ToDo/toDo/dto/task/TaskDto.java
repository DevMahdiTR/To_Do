package com.ToDo.toDo.dto.task;


import com.ToDo.toDo.model.task.Task;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class TaskDto {

    private long id;
     private String task;
     private boolean isDone;


     public TaskDto(@NotNull Task task)
     {
         this.id = task.getId();
         this.task = task.getTask();
         this.isDone = task.isDone();
     }
}
