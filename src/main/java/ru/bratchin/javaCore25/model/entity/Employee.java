package ru.bratchin.javaCore25.model.entity;

import java.util.Objects;

public class Employee {

    private final String surname;
    private final String name;
    private String department;
    private Double salary;

    public Employee(String surname, String name, String department, Double salary) {
        this.surname = surname;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public Employee(String surname, String name) {
        this.surname = surname;
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Сотрудник " + surname + " " + name + "\n" +
                "\tОтдел = '" + department + "',\n" +
                "\tЗарплата = " + salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(surname, employee.surname) && Objects.equals(name, employee.name) && Objects.equals(department, employee.department) && Objects.equals(salary, employee.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, department, salary);
    }
}
