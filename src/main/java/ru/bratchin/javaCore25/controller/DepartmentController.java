package ru.bratchin.javaCore25.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bratchin.javaCore25.service.api.DepartmentServiceApi;
import ru.bratchin.javaCore25.service.impl.DepartmentService;

@RestController
@RequestMapping("/employee/department")
@Validated
public class DepartmentController {

    private final DepartmentServiceApi service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @GetMapping("/max-salary")
    public ResponseEntity<?> maxSalary(
            @RequestParam @NotBlank String departmentId
    ) {
        return ResponseEntity.ok(service.maxSalary(departmentId));
    }

    @GetMapping("/min-salary")
    public ResponseEntity<?> minSalary(
            @RequestParam @NotBlank String departmentId
    ) {
        return ResponseEntity.ok(service.minSalary(departmentId));
    }

    @GetMapping("/all")
    public ResponseEntity<?> filterByDepartment(
            @RequestParam(required = false) String departmentId
    ) {
        if (departmentId != null) {
            return ResponseEntity.ok(service.findByDepartment(departmentId));
        } else {
            return ResponseEntity.ok(service.all());
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void errorParam() {
    }

}
