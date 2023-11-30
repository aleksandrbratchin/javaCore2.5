package ru.bratchin.javaCore25.service.api;

import ru.bratchin.javaCore25.model.entity.Employee;

import java.util.List;
import java.util.Map;

public interface DepartmentServiceApi {

    Employee maxSalary(String department);

    Employee minSalary(String department);

    List<Employee> findByDepartment(String department);

    Map<String, List<Employee>> all();

    Double sum(String department);
}
