package com.ToDo.toDo.model.user;


import com.ToDo.toDo.model.role.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name ="users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "id",unique = true,nullable = false)
    private UUID id;


    @Column(name ="username" , unique = true , nullable = true)
    private String username;

    @Column(name = "password" , nullable = false)
    private String password;


    @ManyToMany(fetch = FetchType.EAGER , cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles" , joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name  = "role_id" , referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
