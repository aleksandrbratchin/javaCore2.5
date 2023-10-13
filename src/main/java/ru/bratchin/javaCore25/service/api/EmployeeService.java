package ru.bratchin.javaCore25.service.api;

import ru.bratchin.javaCore25.model.entity.Employee;

public interface EmployeeService {

    Employee add(Employee employee);
    Employee delete(Employee employee);
    Employee find(Employee employee);

}
