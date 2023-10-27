package ru.bratchin.javaCore25.specification.employee;

import ru.bratchin.javaCore25.exception.SalaryIsNullException;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.specification.MySpecification;

import java.util.Optional;

public class EmployeeSalaryMoreThanSpecification extends MySpecification<Employee> {
    private Double salary;

    public EmployeeSalaryMoreThanSpecification(Double salary) {
        this.salary = salary;
    }

    @Override
    public boolean test(Employee employee) {
        return Optional.ofNullable(employee.getSalary())
                .orElseThrow(
                        () -> new SalaryIsNullException(
                                employee.getSurname() + " " + employee.getName()
                        )
                ) < salary;
    }
}
