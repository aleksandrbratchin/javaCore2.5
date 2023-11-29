package ru.bratchin.javaCore25.repository.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bratchin.javaCore25.model.entity.Employee;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class EmployeeRepositoryTest {
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
        Map<String, Employee> testEmployees = new HashMap<>(
                Map.of("Малышева Амалия", new Employee("Малышева", "Амалия", "2", 83166.43),
                        "Козловский Денис", new Employee("Козловский", "Денис", "1", 60250.60),
                        "Соловьева Серафима", new Employee("Соловьева", "Серафима", "3", 59343.29),
                        "Макарова Дарья", new Employee("Макарова", "Дарья", "1", 82042.89),
                        "Лебедева Таисия", new Employee("Лебедева", "Таисия", "5", 72881.88),
                        "Романов Артём", new Employee("Романов", "Артём", "2", 62761.97),
                        "Широков Павел", new Employee("Широков", "Павел", "4", 97159.11),
                        "Кудрявцев Лев", new Employee("Кудрявцев", "Лев", "2", 89845.70),
                        "Белякова Антонина", new Employee("Белякова", "Антонина", "5", 79209.12),
                        "Филиппова Алиса", new Employee("Филиппова", "Алиса", "1", 87333.51)
                ));
        fieldEmployees.set(repository, testEmployees);
    }

    @Test
    void create() throws IllegalAccessException {

        repository.create(new Employee("Иванов", "Иван", "1", 60250.60));
        var employees = (Map<String, Employee>) fieldEmployees.get(repository);

        assertThat(employees.size()).isEqualTo(11);
    }

    @Test
    void createList() throws IllegalAccessException {

        repository.create(
                List.of(
                        new Employee("Сидоров", "Семен", "2", 83166.43),
                        new Employee("Петров", "Петр", "1", 60250.60)
                )
        );
        var employees = (Map<String, Employee>) fieldEmployees.get(repository);

        assertThat(employees.size()).isEqualTo(12);
    }

    @Test
    void findAll() throws IllegalAccessException {

        var employees = repository.findAll();

        assertThat(employees.size()).isEqualTo(10);
    }

/*    @Test
    void findAllByDepartment() throws IllegalAccessException {
        fieldEmployees.set(repository, testEmployees);

        List<Employee> employees = repository.findAll(new EmployeeEqualsDepartmentSpecification("5"));

        assertThat(employees.size()).isEqualTo(2);
    }*/

/*    @Test
    void findOneById() throws IllegalAccessException {
        int id = 2;
        fieldEmployees.set(repository, testEmployees);

        Employee employees = repository.findOne(new EmployeeEqualsIdSpecification(id)).get();

        assertThat(employees.getPatronymic()).isEqualTo(testEmployees.get(id - 1).getPatronymic());
    }*/

/*    @Test
    void update() throws IllegalAccessException {
        fieldEmployees.set(repository, testEmployees);
        var employees = (Map<String, Employee>) fieldEmployees.get(repository);
        Employee employee = employees.get(3);
        employee.setSalary(100000D);
        employee.setDepartment("10");

        repository.update(employee);

        employees = (List<Employee>) fieldEmployees.get(repository);
        assertThat(employees).contains(employee);
    }*/

/*    @Test
    void updateError() {
        var test = new Employee(-1, "Тест", "Тест", "Тест", "1", 0D);
        Throwable thrown = catchThrowable(() -> repository.update(test));

        assertThat(thrown).isInstanceOf(NoSuchElementException.class)
                .hasMessage("Попытка найти несуществующий элемент");
    }*/

/*    @Test
    void updateList() throws IllegalAccessException {
        fieldEmployees.set(repository, testEmployees);
        var employees = (Map<String, Employee>) fieldEmployees.get(repository);
        List<Employee> employeesUpd = employees.subList(2, 5);
        for (Employee employee : employeesUpd) {
            employee.setSalary(100000D);
            employee.setDepartment("10");
        }

        repository.update(employeesUpd);

        employees = (Map<String, Employee>) fieldEmployees.get(repository);
        assertThat(employees).containsAll(employeesUpd);
    }*/

    @Test
    void delete() throws IllegalAccessException {
        var employee = new Employee("Малышева", "Амалия", "2", .0);

        repository.delete(employee);
        var employees = (Map<String, Employee>) fieldEmployees.get(repository);

        assertThat(employees.size()).isEqualTo(9);
        assertThat(employees).doesNotContainValue(employee);
    }

    @Test
    void deleteError() {
        var test = new Employee("Тест", "Тест", "1", 0D);

        Throwable thrown = catchThrowable(() -> repository.delete(test));

        assertThat(thrown).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void deleteAll() throws IllegalAccessException {

        repository.deleteAll();
        var employees = (Map<String, Employee>) fieldEmployees.get(repository);

        assertThat(employees.size()).isEqualTo(0);
    }
}