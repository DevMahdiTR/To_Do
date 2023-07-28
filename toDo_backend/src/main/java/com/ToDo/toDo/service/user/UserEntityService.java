package com.ToDo.toDo.service.user;


import com.ToDo.toDo.exceptions.ResourceNotFoundException;
import com.ToDo.toDo.model.user.UserEntity;
import com.ToDo.toDo.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserEntityService {


    private final UserEntityRepository userEntityRepository;

     @Autowired
    public UserEntityService(UserEntityRepository userEntityRepository)
     {
         this.userEntityRepository = userEntityRepository;
     }


     public UserEntity getUserEntityByUsername(final String username)
     {
         return userEntityRepository.fetchUserWithUsername(username)
                 .orElseThrow(()-> new ResourceNotFoundException(String.format("The user with username : %s could not be found.", username)));
     }
}
