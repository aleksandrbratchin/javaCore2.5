package ru.bratchin.javaCore25.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.service.api.EmployeeService;
import ru.bratchin.javaCore25.service.impl.EmployeeMaxSizeTenService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeMaxSizeTenService service) {
        this.service = service;
    }

    @GetMapping("/add")
    public ResponseEntity<?> add(
            @RequestParam String name,
            @RequestParam String surname
    ) {
        Employee employee = new Employee(surname, name);
        return ResponseEntity.ok(service.add(employee));
    }

    @GetMapping("/delete")
    public ResponseEntity<?> delete(
            @RequestParam String name,
            @RequestParam String surname
    ) {
        Employee employee = new Employee(surname, name);
        return ResponseEntity.ok(service.delete(employee));
    }

    @GetMapping("/find")
    public ResponseEntity<?> find(
            @RequestParam String name,
            @RequestParam String surname
    ) {
        Employee employee = new Employee(surname, name);
        return ResponseEntity.ok(service.find(employee));
    }

}
