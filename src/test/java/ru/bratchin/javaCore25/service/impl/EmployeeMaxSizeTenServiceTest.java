package ru.bratchin.javaCore25.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bratchin.javaCore25.exception.EmployeeAlreadyAddedException;
import ru.bratchin.javaCore25.exception.EmployeeNotFoundException;
import ru.bratchin.javaCore25.exception.EmployeeStorageIsFullException;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.repository.impl.EmployeeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class EmployeeMaxSizeTenServiceTest {

    private EmployeeMaxSizeTenService service;
    @Mock
    private EmployeeRepository repository;

    @BeforeEach
    void setUp() {
        service = new EmployeeMaxSizeTenService(repository);
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


    @Nested
    class Success {
        @Test
        void add() {
            Employee employee = new Employee("Белякова", "Антонина");
            Mockito.when(repository.findAll())
                    .thenReturn(correctEmployees);
            Mockito.when(repository.create(any(Employee.class)))
                    .thenReturn(employee);

            Employee newEmployee = service.add(employee);

            assertThat(employee).isEqualTo(newEmployee);
        }

        @Test
        void delete() {
            Employee employee = new Employee("Филиппова", "Алиса");
            Mockito.when(repository.delete(any(Employee.class)))
                    .thenReturn(employee);

            Employee newEmployee = service.delete(employee);

            assertThat(employee).isEqualTo(newEmployee);
        }

        @Test
        void find() {
            Employee employee = new Employee("Филиппова", "Алиса");
            Mockito.when(repository.findAll())
                    .thenReturn(correctEmployees);

            Employee newEmployee = service.find(employee);

            assertThat(employee.getName()).isEqualTo(newEmployee.getName());
            assertThat(employee.getSurname()).isEqualTo(newEmployee.getSurname());
        }

        @Test
        void findAll() {
            Mockito.when(repository.findAll())
                    .thenReturn(correctEmployees);

            List<Employee> all = service.findAll();

            assertThat(all.size()).isEqualTo(9);
        }
    }

    @Nested
    class AllError{
        @Test
        void addStorageIsFull() {
            Map<String, Employee> Employees = new HashMap<>(
                    Map.of("Малышева Амалия", new Employee("Малышева", "Амалия", "2", 83166.43),
                            "Козловский Денис", new Employee("Козловский", "Денис", "1", 60250.60),
                            "Соловьева Серафима", new Employee("Соловьева", "Серафима", "3", 59343.29),
                            "Макарова Дарья", new Employee("Макарова", "Дарья", "1", 82042.89),
                            "Лебедева Таисия", new Employee("Лебедева", "Таисия", "5", 72881.88),
                            "Романов Артём", new Employee("Романов", "Артём", "2", 62761.97),
                            "Широков Павел", new Employee("Широков", "Павел", "4", 97159.11),
                            "Кудрявцев Лев", new Employee("Кудрявцев", "Лев", "2", 89845.70),
                            "Филиппова Алиса", new Employee("Филиппова", "Алиса", "5", 79209.12),
                            "Горбачёва Елена", new Employee("Горбачёва", "Елена", "5", 60000.0)
                    ));
            Mockito.when(repository.findAll())
                    .thenReturn(Employees);

            Throwable thrown = catchThrowable(() -> service.add(new Employee("Иванов", "Иван")));

            assertThat(thrown).isInstanceOf(EmployeeStorageIsFullException.class);
        }

        @Test
        void addAlreadyAdded() {
            Mockito.when(repository.findAll())
                    .thenReturn(correctEmployees);
            Mockito.when(repository.create(any(Employee.class)))
                    .thenThrow(EmployeeAlreadyAddedException.class);

            Throwable thrown = catchThrowable(() -> service.add(new Employee("Филиппова", "Алиса")));

            assertThat(thrown).isInstanceOf(EmployeeAlreadyAddedException.class);
        }

        @Test
        void deleteNotFound() {
            Mockito.when(repository.delete(any(Employee.class)))
                    .thenThrow(EmployeeNotFoundException.class);

            Throwable thrown = catchThrowable(() -> service.delete(new Employee("Белякова", "Антонина")));

            assertThat(thrown).isInstanceOf(EmployeeNotFoundException.class);
        }

        @Test
        void findNotFound() {
            Mockito.when(repository.findAll())
                    .thenReturn(correctEmployees);

            Throwable thrown = catchThrowable(() -> service.find(new Employee("Белякова", "Антонина")));

            assertThat(thrown).isInstanceOf(EmployeeNotFoundException.class);
        }

    }
}