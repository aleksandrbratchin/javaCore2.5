package ru.bratchin.javaCore25.specification.employee;


import ru.bratchin.javaCore25.exception.DepartmentIsNullException;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.specification.MySpecification;

import java.util.Optional;

public class EmployeeEqualsDepartmentSpecification extends MySpecification<Employee> {
    private String department;

    public EmployeeEqualsDepartmentSpecification(String department) {
        this.department = department;
    }

    @Override
    public boolean test(Employee employee) {
        return Optional.ofNullable(employee.getDepartment())
                .orElseThrow(
                        () -> new DepartmentIsNullException(
                                employee.getSurname() + " " + employee.getName()
                        )
                ).equals(department);
    }
}