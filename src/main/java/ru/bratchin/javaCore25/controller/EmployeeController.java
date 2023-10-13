package ru.bratchin.javaCore25.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.service.api.EmployeeService;
import ru.bratchin.javaCore25.service.impl.EmployeeMaxSizeTenService;

@RestController
@RequestMapping("/employee")
@Validated
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeMaxSizeTenService service) {
        this.service = service;
    }

    @GetMapping("/add")
    public ResponseEntity<?> add(
            @RequestParam @NotBlank String name,
            @RequestParam @NotBlank String surname
    ) {
        Employee employee = new Employee(surname, name);
        return ResponseEntity.ok(service.add(employee));
    }

    @GetMapping("/delete")
    public ResponseEntity<?> delete(
            @RequestParam @NotBlank String name,
            @RequestParam @NotBlank String surname
    ) {
        Employee employee = new Employee(surname, name);
        return ResponseEntity.ok(service.delete(employee));
    }

    @GetMapping("/find")
    public ResponseEntity<?> find(
            @RequestParam @NotBlank String name,
            @RequestParam @NotBlank String surname
    ) {
        Employee employee = new Employee(surname, name);
        return ResponseEntity.ok(service.find(employee));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void errorParam() {
        System.out.println("sd");
    }

}
