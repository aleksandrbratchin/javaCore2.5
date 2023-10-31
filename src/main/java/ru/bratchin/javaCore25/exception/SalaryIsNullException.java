package ru.bratchin.javaCore25.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SalaryIsNullException extends NullPointerException {

    public SalaryIsNullException() {
        super();
    }

    public SalaryIsNullException(String fio) {
        super("У сотрудника " + fio + " не указана заработная плата");
    }
}
