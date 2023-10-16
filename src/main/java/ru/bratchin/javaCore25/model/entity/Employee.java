package ru.bratchin.javaCore25.model.entity;

import java.util.Objects;

public class Employee {

    private final String surname;
    private final String name;

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public Employee(String surname, String name) {
        this.surname = surname;
        this.name = name;
    }

    @Override
    public String toString() {
        return surname + " " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(surname, employee.surname) && Objects.equals(name, employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name);
    }
}
