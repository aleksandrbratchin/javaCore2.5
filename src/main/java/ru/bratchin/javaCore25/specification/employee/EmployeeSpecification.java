package ru.bratchin.javaCore25.specification.employee;

import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.specification.MySpecification;

public class EmployeeSpecification extends MySpecification<Employee> {
    private final Employee employee;

    public EmployeeSpecification(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean test(Employee employee) {
        return this.employee.getName().equals(employee.getName()) && this.employee.getSurname().equals(employee.getSurname());
    }
}
