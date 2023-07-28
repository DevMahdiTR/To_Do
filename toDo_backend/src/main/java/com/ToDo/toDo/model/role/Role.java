package com.ToDo.toDo.model.role;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Entity
@Data
@Table(name = "roles")
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name =  "id", unique = true, nullable = false)
    private long id;


    @Column(name = "role" , unique = true, nullable = false)
    private String name;

    public Role(String name)
    {
        this.name = name;
    }
    public Role(){}
}
