package ru.bratchin.javaCore25.service.impl;

import org.springframework.stereotype.Service;
import ru.bratchin.javaCore25.exception.DepartmentIsNullException;
import ru.bratchin.javaCore25.exception.SalaryIsNullException;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.repository.api.MyRepository;
import ru.bratchin.javaCore25.repository.impl.EmployeeRepository;
import ru.bratchin.javaCore25.service.api.DepartmentServiceApi;
import ru.bratchin.javaCore25.specification.employee.EmployeeEqualsDepartmentSpecification;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService implements DepartmentServiceApi {

    private final MyRepository<Employee, String> repository;

    public DepartmentService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employee maxSalary(String department) {
        return maxSalary(
                allEmployeesInTheDepartment(department)
        );
    }

    @Override
    public Employee minSalary(String department) {
        return minSalary(
                allEmployeesInTheDepartment(department)
        );
    }

    private List<Employee> allEmployeesInTheDepartment(String department) {
        return repository
                .findAll(new EmployeeEqualsDepartmentSpecification(department))
                .values()
                .stream()
                .toList();
    }

    private Employee maxSalary(List<Employee> employees) {
        return employees.stream().max(
                Comparator.comparingDouble(
                        value -> Optional.ofNullable(value.getSalary())
                                .orElseThrow(
                                        () -> new SalaryIsNullException(value.getSurname() + " " + value.getName())
                                )
                )
        ).orElse(null);
    }

    private Employee minSalary(List<Employee> employees) {
        return employees.stream().min(
                Comparator.comparingDouble(
                        value -> Optional.ofNullable(value.getSalary())
                                .orElseThrow(
                                        () -> new SalaryIsNullException(value.getSurname() + " " + value.getName())
                                )
                )
        ).orElse(null);
    }

    @Override
    public Map<String, List<Employee>> findByDepartment(String department) {
        return repository
                .findAll(new EmployeeEqualsDepartmentSpecification(department))
                .values()
                .stream()
                .collect(Collectors.groupingBy(
                        employee -> Optional.ofNullable(employee.getDepartment()).orElseThrow(
                                () -> new DepartmentIsNullException(
                                        employee.getSurname() + " " + employee.getName()
                                )
                        )
                ));
    }

    @Override
    public Map<String, List<Employee>> all() {
        return repository.findAll()
                .values()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                employee -> Optional.ofNullable(employee.getDepartment())
                                        .orElseThrow(
                                                () -> new DepartmentIsNullException(
                                                        employee.getSurname() + " " + employee.getName()
                                                )
                                        )
                        )
                );
    }
}
