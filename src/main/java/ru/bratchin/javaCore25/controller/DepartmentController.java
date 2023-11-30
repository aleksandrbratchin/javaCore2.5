package ru.bratchin.javaCore25.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bratchin.javaCore25.exception.DepartmentIsNullException;
import ru.bratchin.javaCore25.service.api.DepartmentServiceApi;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentServiceApi service;

    public DepartmentController(@Qualifier("departmentService") DepartmentServiceApi service) {
        this.service = service;
    }

    @GetMapping("/{id}/max")
    public ResponseEntity<?> maxSalary(
            @PathVariable("id") String departmentId
    ) {
        return ResponseEntity.ok(service.maxSalary(departmentId));
    }

    @GetMapping("/{id}/min")
    public ResponseEntity<?> minSalary(
            @PathVariable("id") String departmentId
    ) {
        return ResponseEntity.ok(service.minSalary(departmentId));
    }

    @GetMapping("/{id}/sum")
    public ResponseEntity<?> sum(
            @PathVariable("id") String departmentId
    ) {
        return ResponseEntity.ok(service.sum(departmentId));
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<?> filterByDepartment(
            @PathVariable("id") String departmentId
    ) {
        return ResponseEntity.ok(service.findByDepartment(departmentId));
    }

    @GetMapping("")
    public ResponseEntity<?> filterByDepartment() {
        return ResponseEntity.ok(service.all());
    }

    /***
     * Исправление ошибок спринга
     */
    @ExceptionHandler(DepartmentIsNullException.class)
    public ResponseEntity<?> errorDepartment(DepartmentIsNullException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
    }

}
