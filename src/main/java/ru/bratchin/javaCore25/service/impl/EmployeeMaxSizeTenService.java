package ru.bratchin.javaCore25.service.impl;

import org.springframework.stereotype.Service;
import ru.bratchin.javaCore25.exception.EmployeeAlreadyAddedException;
import ru.bratchin.javaCore25.exception.EmployeeNotFoundException;
import ru.bratchin.javaCore25.exception.EmployeeStorageIsFullException;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.service.api.EmployeeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeMaxSizeTenService implements EmployeeService {

    private final int maxSize = 10;

    private List<Employee> employees = new ArrayList<>(
            List.of(new Employee("Малышева", "Амалия"),
                    new Employee("Козловский", "Денис"),
                    new Employee("Соловьева", "Серафима"),
                    new Employee("Макарова", "Дарья"),
                    new Employee("Лебедева", "Таисия"),
                    new Employee("Романов", "Артём"),
                    new Employee("Широков", "Павел"),
                    new Employee("Кудрявцев", "Лев")
            ));

    @Override
    public Employee add(Employee employee) {
        if (maxSize <= employees.size()) {
            throw new EmployeeStorageIsFullException();
        }
        filter(employee)
                .ifPresentOrElse(
                        employee1 -> {
                            throw new EmployeeAlreadyAddedException();
                        },
                        () -> employees.add(employee)
                );
        return employee;
    }

    @Override
    public Employee delete(Employee employee) {
        filter(employee)
                .ifPresentOrElse(
                        employee1 ->
                                employees.remove(employee1)
                        ,
                        () -> {
                            throw new EmployeeNotFoundException();
                        }
                );
        return employee;
    }

    @Override
    public Employee find(Employee employee) {
        return filter(employee)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    private Optional<Employee> filter(Employee em) {
        return employees.stream()
                .filter(employee -> employee.getName().equals(em.getName()) && employee.getSurname().equals(em.getSurname()))
                .findFirst();
    }

}
