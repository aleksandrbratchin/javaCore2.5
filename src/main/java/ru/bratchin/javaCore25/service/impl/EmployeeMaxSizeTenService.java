package ru.bratchin.javaCore25.service.impl;

import org.springframework.stereotype.Service;
import ru.bratchin.javaCore25.exception.EmployeeNotFoundException;
import ru.bratchin.javaCore25.exception.EmployeeStorageIsFullException;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.repository.api.MyRepository;
import ru.bratchin.javaCore25.repository.impl.EmployeeRepository;
import ru.bratchin.javaCore25.service.api.EmployeeService;

import java.util.List;

@Service
public class EmployeeMaxSizeTenService implements EmployeeService {

    private final int maxSize = 10;

    private final MyRepository<Employee, String> repository;

    public EmployeeMaxSizeTenService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Employee add(Employee employee) {
        if (maxSize <= repository.findAll().size()) {
            throw new EmployeeStorageIsFullException();
        }
        return repository.create(employee);
    }

    @Override
    public Employee delete(Employee employee) {
        repository.delete(employee);
        return employee;
    }

    @Override
    public Employee find(Employee employee) {
        var key = getKey(employee);
        var allEmployee = repository.findAll();
        if (repository.findAll().containsKey(key)) {
            return allEmployee.get(key);
        } else {
            throw new EmployeeNotFoundException();
        }
    }

    @Override
    public List<Employee> findAll() {
        return repository.findAll().values().stream().toList();
    }


    private String getKey(Employee employee) {
        return employee.getSurname() + " " + employee.getName();
    }

}
