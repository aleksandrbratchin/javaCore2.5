package ru.bratchin.javaCore25.service.impl;

import org.springframework.stereotype.Service;
import ru.bratchin.javaCore25.exception.EmployeeAlreadyAddedException;
import ru.bratchin.javaCore25.exception.EmployeeNotFoundException;
import ru.bratchin.javaCore25.exception.EmployeeStorageIsFullException;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.service.api.EmployeeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeMaxSizeTenService implements EmployeeService {

    private final int maxSize = 10;

    private Map<String, Employee> employees = new HashMap<>(
            Map.of("Малышева Амалия", new Employee("Малышева", "Амалия"),
                    "Козловский Денис", new Employee("Козловский", "Денис"),
                    "Соловьева Серафима", new Employee("Соловьева", "Серафима"),
                    "Макарова Дарья", new Employee("Макарова", "Дарья"),
                    "Лебедева Таисия", new Employee("Лебедева", "Таисия"),
                    "Романов Артём", new Employee("Романов", "Артём"),
                    "Широков Павел", new Employee("Широков", "Павел"),
                    "Кудрявцев Лев", new Employee("Кудрявцев", "Лев")
            ));

    @Override
    public Employee add(Employee employee) {
        if (maxSize <= employees.size()) {
            throw new EmployeeStorageIsFullException();
        }
        var key = getKey(employee);
        if (employees.containsKey(key)) {
            throw new EmployeeAlreadyAddedException();
        } else {
            employees.put(key, employee);
            return employee;
        }

    }

    @Override
    public Employee delete(Employee employee) {
        var key = getKey(employee);
        if (employees.containsKey(key)) {
            employees.remove(key, employee);
            return employee;
        } else {
            throw new EmployeeNotFoundException();
        }
    }

    @Override
    public Employee find(Employee employee) {
        var key = getKey(employee);
        if (employees.containsKey(key)) {
            return employees.get(key);
        } else {
            throw new EmployeeNotFoundException();
        }
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }


    private String getKey(Employee employee) {
        return employee.getSurname() + " " + employee.getName();
    }

}
