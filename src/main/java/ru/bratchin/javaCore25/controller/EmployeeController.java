package ru.bratchin.javaCore25.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.service.api.EmployeeService;
import ru.bratchin.javaCore25.util.StringFormat;
import ru.bratchin.javaCore25.util.validation.StringValid;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(@Qualifier("employeeMaxSizeTenService") EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/add")
    public ResponseEntity<?> add(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Double salary
    ) {
        StringValid.nameValid(name);
        StringValid.surnameValid(surname);
        name = StringFormat.nameFormat(name);
        surname = StringFormat.nameFormat(surname);
        Employee employee = new Employee(surname, name, department, salary);
        return ResponseEntity.ok(service.add(employee));
    }

    @GetMapping("/delete")
    public ResponseEntity<?> delete(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname
    ) {
        StringValid.nameValid(name);
        StringValid.surnameValid(surname);
        name = StringFormat.nameFormat(name);
        surname = StringFormat.nameFormat(surname);
        Employee employee = new Employee(surname, name);
        return ResponseEntity.ok(service.delete(employee));
    }

    @GetMapping("/find")
    public ResponseEntity<?> find(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname
    ) {
        StringValid.nameValid(name);
        StringValid.surnameValid(surname);
        name = StringFormat.nameFormat(name);
        surname = StringFormat.nameFormat(surname);
        Employee employee = new Employee(surname, name);
        return ResponseEntity.ok(service.find(employee));
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

}
