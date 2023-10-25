package ru.bratchin.javaCore25.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.repository.impl.EmployeeRepository;
import ru.bratchin.javaCore25.service.impl.DepartmentService;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DepartmentControllerTest {

    @Autowired
    private DepartmentController controller;

    @Autowired
    private DepartmentService service;

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

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
            Map<String, String> uriVariables = new HashMap<>();
            uriVariables.put("departmentId", "2");

            ResponseEntity<Employee> response
                    = restTemplate.getForEntity("/employee/department/max-salary?departmentId={departmentId}", Employee.class, uriVariables);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getSalary()).isEqualTo(89845.70);
        }

        @Test
        void minSalary() {
            Map<String, String> uriVariables = new HashMap<>();
            uriVariables.put("departmentId", "2");

            ResponseEntity<Employee> response = restTemplate.getForEntity(
                    "/employee/department/min-salary?departmentId={departmentId}",
                    Employee.class,
                    uriVariables
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getSalary()).isEqualTo(62761.97);
        }

        @Test
        void filterByDepartment() {
            Map<String, String> uriVariables = new HashMap<>();
            uriVariables.put("departmentId", "2");

            ResponseEntity<Map<String, List<Employee>>> response
                    = restTemplate.exchange(
                    "/employee/department/all?departmentId={departmentId}",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, List<Employee>>>() {
                    },
                    uriVariables
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().size()).isEqualTo(1);
            assertThat(response.getBody().get("2").size()).isEqualTo(3);
        }

        @Test
        void getAll() {

            ResponseEntity<Map<String, List<Employee>>> response
                    = restTemplate.exchange(
                    "/employee/department/all",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, List<Employee>>>() {
                    });

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().size()).isEqualTo(5);
        }

    }

    @Nested
    class AllError {

        @BeforeEach
        public void initEach() throws IllegalAccessException {
            repository = new EmployeeRepository();
            service = new DepartmentService(repository);

            Map<String, Employee> testEmployees = new HashMap<>();

            fieldEmployees.set(repository, testEmployees);
        }

        @Test
        void maxSalary() {
            Map<String, String> uriVariables = new HashMap<>();
            uriVariables.put("departmentId", "2");

            ResponseEntity<Employee> response
                    = restTemplate.getForEntity("/employee/department/max-salary?departmentId={departmentId}", Employee.class, uriVariables);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNull();
        }

        @Test
        void minSalary() {
            Map<String, String> uriVariables = new HashMap<>();
            uriVariables.put("departmentId", "2");

            ResponseEntity<Employee> response
                    = restTemplate.getForEntity("/employee/department/min-salary?departmentId={departmentId}", Employee.class, uriVariables);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNull();
        }

        @Test
        void findByDepartment() {
            Map<String, String> uriVariables = new HashMap<>();
            uriVariables.put("departmentId", "2");

            ResponseEntity<Map<String, List<Employee>>> response
                    = restTemplate.exchange(
                    "/employee/department/all?departmentId={departmentId}",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, List<Employee>>>() {
                    },
                    uriVariables
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().size()).isEqualTo(0);
        }

        @Test
        void all() {
            ResponseEntity<Map<String, List<Employee>>> response
                    = restTemplate.exchange(
                    "/employee/department/all",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, List<Employee>>>() {
                    });

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().size()).isEqualTo(0);
        }
    }

    @Nested
    class ParametersError {

        @Nested
        class ParametersIsEmpty {
            private static Map<String, String> uriVariables;

            @BeforeAll
            public static void setup() {
                uriVariables = new HashMap<>();
                uriVariables.put("departmentId", null);
            }

            @Test
            void maxSalary() {

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/department/max-salary?departmentId={departmentId}", Employee.class, uriVariables);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void minSalary() {

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/department/min-salary?departmentId={departmentId}", Employee.class, uriVariables);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

        }

    }

}