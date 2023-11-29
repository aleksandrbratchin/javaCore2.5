package ru.bratchin.javaCore25.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bratchin.javaCore25.exception.DepartmentIsNullException;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.repository.impl.EmployeeRepository;
import ru.bratchin.javaCore25.specification.employee.EmployeeEqualsDepartmentSpecification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    private DepartmentService service;

    @Mock
    private EmployeeRepository repository;

    @BeforeEach
    void setUp() {
        service = new DepartmentService(repository);
    }

    private final Map<String, Employee> correctEmployees = new HashMap<>(
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

    private final Map<String, Employee> departmentTwo = new HashMap<>(
            Map.of("Малышева Амалия", new Employee("Малышева", "Амалия", "2", 83166.43),
                    "Романов Артём", new Employee("Романов", "Артём", "2", 62761.97),
                    "Кудрявцев Лев", new Employee("Кудрявцев", "Лев", "2", 89845.70)
            ));

    @Nested
    class Success {

        @Test
        void maxSalary() {
            Mockito.when(repository.findAll(any(EmployeeEqualsDepartmentSpecification.class)))
                    .thenReturn(departmentTwo);

            Employee maxSalaryEmployee = service.maxSalary("2");

            assertThat(maxSalaryEmployee.getSalary()).isEqualTo(89845.70);
        }

        @Test
        void minSalary() {
            Mockito.when(repository.findAll(any(EmployeeEqualsDepartmentSpecification.class)))
                    .thenReturn(departmentTwo);

            Employee minSalaryEmployee = service.minSalary("2");

            assertThat(minSalaryEmployee.getSalary()).isEqualTo(62761.97);
        }

        @Test
        void findByDepartment() {
            Mockito.when(repository.findAll(any(EmployeeEqualsDepartmentSpecification.class)))
                    .thenReturn(departmentTwo);

            List<Employee> all = service.findByDepartment("2");

            assertThat(all.size()).isEqualTo(3);
        }

        @Test
        void all() {
            Mockito.when(repository.findAll())
                    .thenReturn(correctEmployees);

            Map<String, List<Employee>> all = service.all();

            assertThat(all.size()).isEqualTo(5);
        }

        @Test
        void sum() {
            Mockito.when(repository.findAll(any(EmployeeEqualsDepartmentSpecification.class)))
                    .thenReturn(departmentTwo);

            Double sum = service.sum("2");

            assertThat(sum.toString()).startsWith("235774.09");
        }
    }


    @Nested
    class Error {


        @Nested
        class DepartmentIsNull {

            private final Map<String, Employee> departments = Map.of("Малышева Амалия", new Employee("Малышева", "Амалия", null, 83166.43),
                    "Козловский Денис", new Employee("Козловский", "Денис", "1", 60250.60),
                    "Соловьева Серафима", new Employee("Соловьева", "Серафима", "3", 59343.29),
                    "Макарова Дарья", new Employee("Макарова", "Дарья", "1", 82042.89),
                    "Лебедева Таисия", new Employee("Лебедева", "Таисия", "5", 72881.88),
                    "Романов Артём", new Employee("Романов", "Артём", "2", 62761.97),
                    "Широков Павел", new Employee("Широков", "Павел", "4", 97159.11),
                    "Кудрявцев Лев", new Employee("Кудрявцев", "Лев", "2", 89845.70),
                    "Филиппова Алиса", new Employee("Филиппова", "Алиса", "5", 79209.12)
            );

            @Test
            void all() {
                Mockito.when(repository.findAll())
                        .thenReturn(departments);

                Throwable thrown = catchThrowable(() -> service.all());

                assertThat(thrown).isInstanceOf(DepartmentIsNullException.class)
                        .hasMessageContaining("Малышева");
            }

        }

        @Nested
        class EmployeesIsEmpty {

            @Test
            void maxSalary() {
                Mockito.when(repository.findAll(any()))
                        .thenReturn(new HashMap<>());

                Employee maxSalaryEmployee = service.maxSalary("2");

                assertThat(maxSalaryEmployee).isNull();
            }

            @Test
            void minSalary() {
                Mockito.when(repository.findAll(any()))
                        .thenReturn(new HashMap<>());

                Employee minSalaryEmployee = service.minSalary("2");

                assertThat(minSalaryEmployee).isNull();
            }

            @Test
            void findByDepartment() {
                Mockito.when(repository.findAll(any()))
                        .thenReturn(new HashMap<>());

                List<Employee> all = service.findByDepartment("2");

                assertThat(all.size()).isEqualTo(0);
            }

            @Test
            void all() {
                Mockito.when(repository.findAll())
                        .thenReturn(new HashMap<>());

                Map<String, List<Employee>> all = service.all();

                assertThat(all.size()).isEqualTo(0);
            }

            @Test
            void sum() {
                Mockito.when(repository.findAll(any()))
                        .thenReturn(new HashMap<>());

                Double sum = service.sum("2");

                assertThat(sum).isEqualTo(0);
            }
        }

    }


}