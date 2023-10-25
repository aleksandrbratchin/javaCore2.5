package ru.bratchin.javaCore25.specification.employee;

import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.specification.MySpecification;

public class EmployeeSalaryMoreThanSpecification extends MySpecification<Employee> {
    private Double salary;

    public EmployeeSalaryMoreThanSpecification(Double salary) {
        this.salary = salary;
    }

    @Override
    public boolean test(Employee employee) {
        return employee.getSalary() > salary;
    }
}
