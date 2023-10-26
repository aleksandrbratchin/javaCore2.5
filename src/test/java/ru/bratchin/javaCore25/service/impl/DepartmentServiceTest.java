package ru.bratchin.javaCore25.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.repository.impl.EmployeeRepository;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class DepartmentServiceTest {

    private DepartmentService service;
    private EmployeeRepository repository;
    private static Field fieldEmployees;

    @BeforeAll
    public static void setup() throws NoSuchFieldException {
        fieldEmployees = EmployeeRepository.class.getDeclaredField("employees");
        fieldEmployees.setAccessible(true);
    }

    @Nested
    class AllSuccess {
        @BeforeEach
        public void initEach() throws IllegalAccessException {
            repository = new EmployeeRepository();
            service = new DepartmentService(repository);

            Map<String, Employee> testEmployees = new HashMap<>(
                    Map.of("Малышева Амалия", new Employee("Малышева", "Амалия", "2", 83166.43),
                            "Козловский Денис", new Employee("Козловский", "Денис", "1", 60250.60),
                            "Соловьева Серафима", new Employee("Соловьева", "Серафима", "3", 59343.29),
                            "Макарова Дарья", new Employee("Макарова", "Дарья", "1", 82042.89),
                            "Лебедева Таисия", new Employee("Лебедева", "Таисия", "5", 72881.88),
                            "Романов Артём", new Employee("Романов", "Артём", "2", 62761.97),
                            "Широков Павел", new Employee("Широков", "Павел", "4", 97159.11),
                            "Кудрявцев Лев", new Employee("Кудрявцев", "Лев", "2", 89845.70),
                            "Филиппова Алиса", new Employee("Филиппова", "Алиса", "5", 79209.12)
                    ));

            fieldEmployees.set(repository, testEmployees);
        }

        @Test
        void maxSalary() {

            Employee maxSalaryEmployee = service.maxSalary("2");

            assertThat(maxSalaryEmployee.getSalary()).isEqualTo(89845.70);
        }

        @Test
        void minSalary() {

            Employee minSalaryEmployee = service.minSalary("2");

            assertThat(minSalaryEmployee.getSalary()).isEqualTo(62761.97);
        }

        @Test
        void findByDepartment() {
            Map<String, List<Employee>> all = service.findByDepartment("2");

            assertThat(all.size()).isEqualTo(1);
            assertThat(all.get("2").size()).isEqualTo(3);
        }

        @Test
        void all() {

            Map<String, List<Employee>> all = service.all();

            assertThat(all.size()).isEqualTo(5);
        }
    }

    @Nested
    class AllError {

        @Nested
        class SalaryIsNull {
            @BeforeEach
            public void initEach() throws IllegalAccessException {
                repository = new EmployeeRepository();
                service = new DepartmentService(repository);

                Map<String, Employee> testEmployees = new HashMap<>(
                        Map.of("Малышева Амалия", new Employee("Малышева", "Амалия", "2", null),
                                "Козловский Денис", new Employee("Козловский", "Денис", "1", 60250.60),
                                "Соловьева Серафима", new Employee("Соловьева", "Серафима", "3", 59343.29),
                                "Макарова Дарья", new Employee("Макарова", "Дарья", "1", 82042.89),
                                "Лебедева Таисия", new Employee("Лебедева", "Таисия", "5", 72881.88),
                                "Романов Артём", new Employee("Романов", "Артём", "2", 62761.97),
                                "Широков Павел", new Employee("Широков", "Павел", "4", 97159.11),
                                "Кудрявцев Лев", new Employee("Кудрявцев", "Лев", "2", 89845.70),
                                "Филиппова Алиса", new Employee("Филиппова", "Алиса", "5", 79209.12)
                        ));

                fieldEmployees.set(repository, testEmployees);
            }

            @Test
            void maxSalary() {

                Throwable thrown = catchThrowable(() -> service.maxSalary("2"));

                assertThat(thrown).isInstanceOf(NullPointerException.class);
            }

            @Test
            void minSalary() {

                Throwable thrown = catchThrowable(() -> service.minSalary("2"));

                assertThat(thrown).isInstanceOf(NullPointerException.class);
            }

        }

        @Nested
        class EmployeesIsEmpty {
            @BeforeEach
            public void initEach() throws IllegalAccessException {
                repository = new EmployeeRepository();
                service = new DepartmentService(repository);

                Map<String, Employee> testEmployees = new HashMap<>();

                fieldEmployees.set(repository, testEmployees);
            }

            @Test
            void maxSalary() {

                Employee maxSalaryEmployee = service.maxSalary("2");

                assertThat(maxSalaryEmployee).isNull();
            }

            @Test
            void minSalary() {

                Employee minSalaryEmployee = service.minSalary("2");

                assertThat(minSalaryEmployee).isNull();
            }

            @Test
            void findByDepartment() {
                Map<String, List<Employee>> all = service.findByDepartment("2");

                assertThat(all.size()).isEqualTo(0);
                assertThat(all.get("2")).isNull();
            }

            @Test
            void all() {

                Map<String, List<Employee>> all = service.all();

                assertThat(all.size()).isEqualTo(0);
            }
        }

    }


}