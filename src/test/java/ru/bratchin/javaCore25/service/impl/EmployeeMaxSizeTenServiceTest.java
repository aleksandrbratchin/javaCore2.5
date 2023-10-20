package ru.bratchin.javaCore25.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.bratchin.javaCore25.exception.EmployeeAlreadyAddedException;
import ru.bratchin.javaCore25.exception.EmployeeNotFoundException;
import ru.bratchin.javaCore25.exception.EmployeeStorageIsFullException;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.repository.impl.EmployeeRepository;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class EmployeeMaxSizeTenServiceTest {

    private EmployeeMaxSizeTenService service;
    private EmployeeRepository repository;
    private static Field fieldEmployees;

    @BeforeAll
    public static void setup() throws NoSuchFieldException {
        fieldEmployees = EmployeeRepository.class.getDeclaredField("employees");
        fieldEmployees.setAccessible(true);
    }

    @BeforeEach
    public void initEach() throws IllegalAccessException {
        repository = new EmployeeRepository();
        service = new EmployeeMaxSizeTenService(repository);

        Map<String, Employee> testEmployees = new HashMap<>(
                Map.of("Малышева Амалия", new Employee("Малышева", "Амалия"),
                        "Козловский Денис", new Employee("Козловский", "Денис"),
                        "Соловьева Серафима", new Employee("Соловьева", "Серафима"),
                        "Макарова Дарья", new Employee("Макарова", "Дарья"),
                        "Лебедева Таисия", new Employee("Лебедева", "Таисия"),
                        "Романов Артём", new Employee("Романов", "Артём"),
                        "Широков Павел", new Employee("Широков", "Павел"),
                        "Кудрявцев Лев", new Employee("Кудрявцев", "Лев"),
                        "Филиппова Алиса", new Employee("Филиппова", "Алиса")
                ));

        fieldEmployees.set(repository, testEmployees);
    }

    @Nested
    class AllSuccess{
        @Test
        void add() throws IllegalAccessException {
            Employee employee = new Employee("Белякова", "Антонина");

            Employee newEmployee = service.add(employee);
            var employees = (Map<String, Employee>) fieldEmployees.get(repository);

            assertThat(employee).isEqualTo(newEmployee);
            assertThat(employees.size()).isEqualTo(10);
            assertThat(employees).containsValue(employee);
        }

        @Test
        void delete() throws IllegalAccessException {
            Employee employee = new Employee("Филиппова", "Алиса");

            Employee newEmployee = service.delete(employee);
            var employees = (Map<String, Employee>) fieldEmployees.get(repository);

            assertThat(employee).isEqualTo(newEmployee);
            assertThat(employees.size()).isEqualTo(8);
            assertThat(employees).doesNotContainValue(employee);
        }

        @Test
        void find() throws IllegalAccessException {
            Employee employee = new Employee("Филиппова", "Алиса");

            Employee newEmployee = service.find(employee);
            var employees = (Map<String, Employee>) fieldEmployees.get(repository);

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