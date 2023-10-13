package ru.bratchin.javaCore25.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.bratchin.javaCore25.exception.EmployeeAlreadyAddedException;
import ru.bratchin.javaCore25.exception.EmployeeNotFoundException;
import ru.bratchin.javaCore25.exception.EmployeeStorageIsFullException;
import ru.bratchin.javaCore25.model.entity.Employee;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class EmployeeMaxSizeTenServiceTest {

    private EmployeeMaxSizeTenService service;

    private static Field fieldEmployees;

    @BeforeAll
    public static void setup() throws NoSuchFieldException {
        fieldEmployees = EmployeeMaxSizeTenService.class.getDeclaredField("employees");
        fieldEmployees.setAccessible(true);
    }

    @BeforeEach
    public void initEach() throws IllegalAccessException {
        service = new EmployeeMaxSizeTenService();
        List<Employee> testEmployees = new ArrayList<>(
                List.of(
                        new Employee("Малышева", "Амалия"),
                        new Employee("Козловский", "Денис"),
                        new Employee("Соловьева", "Серафима"),
                        new Employee("Макарова", "Дарья"),
                        new Employee("Лебедева", "Таисия"),
                        new Employee("Романов", "Артём"),
                        new Employee("Широков", "Павел"),
                        new Employee("Кудрявцев", "Лев"),
                        new Employee("Филиппова", "Алиса")
                )
        );
        fieldEmployees.set(service, testEmployees);
    }

    @Nested
    class AllSuccess{
        @Test
        void add() throws IllegalAccessException {
            Employee employee = new Employee("Белякова", "Антонина");

            Employee newEmployee = service.add(employee);
            var employees = (List<Employee>) fieldEmployees.get(service);

            assertThat(employee).isEqualTo(newEmployee);
            assertThat(employees.size()).isEqualTo(10);
            assertThat(employees).contains(employee);
        }

        @Test
        void delete() throws IllegalAccessException {
            Employee employee = new Employee("Филиппова", "Алиса");

            Employee newEmployee = service.delete(employee);
            var employees = (List<Employee>) fieldEmployees.get(service);

            assertThat(employee).isEqualTo(newEmployee);
            assertThat(employees.size()).isEqualTo(8);
            assertThat(employees).doesNotContain(employee);
        }

        @Test
        void find() throws IllegalAccessException {
            Employee employee = new Employee("Филиппова", "Алиса");

            Employee newEmployee = service.find(employee);
            var employees = (List<Employee>) fieldEmployees.get(service);

            assertThat(employee).isEqualTo(newEmployee);
            assertThat(employees.size()).isEqualTo(9);
        }
    }

    @Nested
    class AllError{
        @Test
        void addStorageIsFull() {
            service.add(new Employee("Белякова", "Антонина"));
            Throwable thrown = catchThrowable(() -> service.add(new Employee("Иванов", "Иван")));

            assertThat(thrown).isInstanceOf(EmployeeStorageIsFullException.class);
        }

        @Test
        void addAlreadyAdded() {
            Throwable thrown = catchThrowable(() -> service.add(new Employee("Филиппова", "Алиса")));

            assertThat(thrown).isInstanceOf(EmployeeAlreadyAddedException.class);
        }

        @Test
        void deleteNotFound() {
            Throwable thrown = catchThrowable(() -> service.delete(new Employee("Белякова", "Антонина")));

            assertThat(thrown).isInstanceOf(EmployeeNotFoundException.class);
        }

        @Test
        void findNotFound() {
            Throwable thrown = catchThrowable(() -> service.find(new Employee("Белякова", "Антонина")));

            assertThat(thrown).isInstanceOf(EmployeeNotFoundException.class);
        }

    }
}