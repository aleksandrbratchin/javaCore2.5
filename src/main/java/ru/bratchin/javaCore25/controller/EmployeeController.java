package ru.bratchin.javaCore25.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.service.api.EmployeeService;

@RestController
@RequestMapping("/employee")
@Validated
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(@Qualifier("employeeMaxSizeTenService") EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/add")
    public ResponseEntity<?> add(
            @RequestParam(required = false) @NotBlank String name,
            @RequestParam(required = false) @NotBlank String surname,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Double salary
    ) {
        Employee employee = new Employee(surname, name, department, salary);
        return ResponseEntity.ok(service.add(employee));
    }

    @GetMapping("/delete")
    public ResponseEntity<?> delete(
            @RequestParam(required = false) @NotBlank String name,
            @RequestParam(required = false) @NotBlank String surname
    ) {
        Employee employee = new Employee(surname, name);
        return ResponseEntity.ok(service.delete(employee));
    }

    @GetMapping("/find")
    public ResponseEntity<?> find(
            @RequestParam(required = false) @NotBlank String name,
            @RequestParam(required = false) @NotBlank String surname
    ) {
        Employee employee = new Employee(surname, name);
        return ResponseEntity.ok(service.find(employee));
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void errorParam() {
    }

}
