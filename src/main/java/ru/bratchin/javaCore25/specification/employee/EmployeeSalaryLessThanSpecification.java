package ru.bratchin.javaCore25.specification.employee;


import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.specification.MySpecification;

public class EmployeeSalaryLessThanSpecification extends MySpecification<Employee> {
    private Double salary;

    public EmployeeSalaryLessThanSpecification(Double salary) {
        this.salary = salary;
    }

    @Override
    public boolean test(Employee employee) {
        return employee.getSalary() < salary;
    }
}
