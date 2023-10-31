package ru.bratchin.javaCore25.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class DepartmentIsNullException extends NullPointerException {

    public DepartmentIsNullException() {
        super();
    }

    public DepartmentIsNullException(String fio) {
        super("У сотрудника " + fio + " не указан отдел");
    }
}
