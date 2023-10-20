package ru.bratchin.javaCore25.service.impl;

import org.springframework.stereotype.Service;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.repository.api.MyRepository;
import ru.bratchin.javaCore25.repository.impl.EmployeeRepository;
import ru.bratchin.javaCore25.service.api.DepartmentServiceApi;
import ru.bratchin.javaCore25.specification.employee.EmployeeEqualsDepartmentSpecification;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
                getAllEmployee(department)
        );
    }

    @Override
    public Employee minSalary(String department) {
        return minSalary(
                getAllEmployee(department)
        );
    }

    private List<Employee> getAllEmployee(String department) {
        return repository
                .findAll(new EmployeeEqualsDepartmentSpecification(department))
                .values()
                .stream()
                .toList();
    }

    private Employee maxSalary(List<Employee> employees) {
        return employees.stream().max(
                Comparator.comparingDouble(Employee::getSalary)
        ).orElseThrow(() -> new RuntimeException("Ошибка при поиске максимального числа")); //todo
    }

    private Employee minSalary(List<Employee> employees) {
        return employees.stream().min(
                Comparator.comparingDouble(Employee::getSalary)
        ).orElseThrow(() -> new RuntimeException("Ошибка при поиске минимального числа")); //todo
    }

    @Override
    public Map<String, List<Employee>> findByDepartment(String department) {
        return repository
                .findAll(new EmployeeEqualsDepartmentSpecification(department))
                .values()
                .stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }

    @Override
    public Map<String, List<Employee>> all() {
        return repository.findAll()
                .values()
                .stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }
}
