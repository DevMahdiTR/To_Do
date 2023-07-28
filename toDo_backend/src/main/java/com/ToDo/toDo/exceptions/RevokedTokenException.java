package com.ToDo.toDo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RevokedTokenException extends  RuntimeException{
    public RevokedTokenException(String message) {super(message);}
}
