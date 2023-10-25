package ru.bratchin.javaCore25.repository.impl;

import org.springframework.stereotype.Repository;
import ru.bratchin.javaCore25.exception.EmployeeAlreadyAddedException;
import ru.bratchin.javaCore25.exception.EmployeeNotFoundException;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.repository.api.MyRepository;
import ru.bratchin.javaCore25.specification.MySpecification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository implements MyRepository<Employee, String> {

    private Map<String, Employee> employees = new HashMap<>();

    @Override
    public Map<String, Employee> findAll(MySpecification<Employee> specification) {
        return employees.entrySet()
                .stream()
                .filter(entry -> specification.test(entry.getValue()))
                .collect(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                );
    }

    @Override
    public Map<String, Employee> findAll() {
        return employees;
    }

    @Override
    public Optional<Employee> findOne(MySpecification<Employee> specification) {
        return employees.values().stream().filter(specification).findFirst();
    }

    @Override
    public Employee create(Employee employee) {
        var key = getKey(employee);
        Optional.ofNullable(employees.put(key, employee))
                .ifPresent(employee1 -> {
                    throw new EmployeeAlreadyAddedException();
                });
        return employees.get(key);
    }

    @Override
    public void create(List<Employee> employees) {
        employees.forEach(this::create);
    }

    @Override
    public Employee delete(Employee employee) {
        var key = getKey(employee);
        return Optional.ofNullable(employees.remove(key)).orElseThrow(EmployeeNotFoundException::new);

    }

    @Override
    public void deleteAll() {
        employees = new HashMap<>();
    }

    private String getKey(Employee employee) {
        return employee.getSurname() + " " + employee.getName();
    }

}
