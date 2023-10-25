package ru.bratchin.javaCore25.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bratchin.javaCore25.service.api.DepartmentServiceApi;

@RestController
@RequestMapping("/employee/department")
@Validated
public class DepartmentController {

    private final DepartmentServiceApi service;

    public DepartmentController(@Qualifier("departmentService") DepartmentServiceApi service) {
        this.service = service;
    }

    @GetMapping("/max-salary")
    public ResponseEntity<?> maxSalary(
            @RequestParam(required = false) @NotBlank String departmentId
    ) {
        return ResponseEntity.ok(service.maxSalary(departmentId));
    }

    @GetMapping("/min-salary")
    public ResponseEntity<?> minSalary(
            @RequestParam(required = false) @NotBlank String departmentId
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
