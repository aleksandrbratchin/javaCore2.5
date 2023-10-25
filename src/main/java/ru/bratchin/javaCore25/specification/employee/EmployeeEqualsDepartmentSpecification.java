package ru.bratchin.javaCore25.specification.employee;


import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.specification.MySpecification;

public class EmployeeEqualsDepartmentSpecification extends MySpecification<Employee> {
    private String department;

    public EmployeeEqualsDepartmentSpecification(String department) {
        this.department = department;
    }

    @Override
    public boolean test(Employee employee) {
        return employee.getDepartment().equals(department);
    }
}
