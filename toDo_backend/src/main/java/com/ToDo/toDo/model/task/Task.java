package com.ToDo.toDo.model.task;


import com.ToDo.toDo.model.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Entity
@Table(name = "tasks")
public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id" , unique = true , nullable = false)
    private long id;


    @Column(name = "task" , nullable = false)
    private String task;

    @Column(name = "done",nullable = false)
    @JsonProperty("isDone")
    private boolean isDone = false;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public Task() {

    }

    public Task (final String task , boolean isDone)
    {
        this.task = task;
        this.isDone = isDone;
    }
    public Task(final String task)
    {
        this.task = task;
        this.isDone = false;
    }
}
