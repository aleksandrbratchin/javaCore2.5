package ru.bratchin.javaCore25.exception.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NameValidException extends RuntimeException{
    public NameValidException(String message) {
        super(message);
    }
}
