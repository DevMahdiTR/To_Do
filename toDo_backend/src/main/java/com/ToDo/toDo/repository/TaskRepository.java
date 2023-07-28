package com.ToDo.toDo.repository;

import com.ToDo.toDo.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository  extends JpaRepository<Task, Integer> {

    @Query(value = "SELECT T FROM Task  T  WHERE T.id = :taskId")
    Optional<Task> fetchTaskById(@Param("taskId") final long taskId);


    @Query(value = "SELECT T FROM Task T WHERE T.userEntity.id = :userId")
    List<Task> fetchUserTasksById(@Param("userId") final UUID userId);

    @Modifying
    @Query(value = "DELETE FROM Task T WHERE T.id = :taskId")
    void deleteTaskById(@Param("taskId") final long taskId);
}
