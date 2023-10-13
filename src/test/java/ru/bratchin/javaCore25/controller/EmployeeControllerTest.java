package ru.bratchin.javaCore25.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.service.impl.EmployeeMaxSizeTenService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerTest {

    @Autowired
    private EmployeeController controller;

    @Autowired
    private EmployeeMaxSizeTenService service;

    @Autowired
    private TestRestTemplate restTemplate;

    private static Field fieldEmployees;


    @Nested
    class TestAddDeleteFind {
        @BeforeAll
        public static void setup() throws NoSuchFieldException {
            fieldEmployees = EmployeeMaxSizeTenService.class.getDeclaredField("employees");
            fieldEmployees.setAccessible(true);
        }

        @BeforeEach
        public void initEach() throws IllegalAccessException {
            var testEmployees = new ArrayList<>(
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
        class AllSuccess {
            @Test
            void add() throws IllegalAccessException {
                Employee testEmployee = new Employee("Белякова", "Антонина");
                Map<String, String> uriVariables = new HashMap<>();
                uriVariables.put("name", testEmployee.getName());
                uriVariables.put("surname", testEmployee.getSurname());

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/add?name={name}&surname={surname}", Employee.class, uriVariables);
                var employees = (List<Employee>) fieldEmployees.get(service);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody().getSurname()).isEqualTo(uriVariables.get("surname"));
                assertThat(response.getBody().getName()).isEqualTo(uriVariables.get("name"));
                assertThat(employees.size()).isEqualTo(10);
                assertThat(employees).contains(testEmployee);
            }

            @Test
            void delete() throws IllegalAccessException {
                Employee testEmployee = new Employee("Кудрявцев", "Лев");
                Map<String, String> uriVariables = new HashMap<>();
                uriVariables.put("name", testEmployee.getName());
                uriVariables.put("surname", testEmployee.getSurname());

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/delete?name={name}&surname={surname}", Employee.class, uriVariables);
                var employees = (List<Employee>) fieldEmployees.get(service);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody().getSurname()).isEqualTo(uriVariables.get("surname"));
                assertThat(response.getBody().getName()).isEqualTo(uriVariables.get("name"));
                assertThat(employees.size()).isEqualTo(8);
                assertThat(employees).doesNotContain(testEmployee);
            }

            @Test
            void find() throws IllegalAccessException {
                Employee testEmployee = new Employee("Кудрявцев", "Лев");
                Map<String, String> uriVariables = new HashMap<>();
                uriVariables.put("name", testEmployee.getName());
                uriVariables.put("surname", testEmployee.getSurname());

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/find?name={name}&surname={surname}", Employee.class, uriVariables);
                var employees = (List<Employee>) fieldEmployees.get(service);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody().getSurname()).isEqualTo(uriVariables.get("surname"));
                assertThat(response.getBody().getName()).isEqualTo(uriVariables.get("name"));
                assertThat(employees).contains(testEmployee);
                assertThat(employees.size()).isEqualTo(9);
            }

            @Test
            void findAll() {
                ResponseEntity<List<Employee>> response
                        = restTemplate.exchange("/employee/findAll", HttpMethod.GET, null, new ParameterizedTypeReference<List<Employee>>(){});

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody().size()).isEqualTo(9);
            }

        }

        @Nested
        class AllError {
            @Test
            void addStorageIsFull() throws IllegalAccessException {
                Employee testEmployee = new Employee("Иванов", "Иван");
                var testEmployees = new ArrayList<>(
                        List.of(
                                new Employee("Малышева", "Амалия"),
                                new Employee("Козловский", "Денис"),
                                new Employee("Соловьева", "Серафима"),
                                new Employee("Макарова", "Дарья"),
                                new Employee("Лебедева", "Таисия"),
                                new Employee("Романов", "Артём"),
                                new Employee("Широков", "Павел"),
                                new Employee("Кудрявцев", "Лев"),
                                new Employee("Белякова", "Антонина"),
                                new Employee("Филиппова", "Алиса")
                        )
                );
                fieldEmployees.set(service, testEmployees);
                Map<String, String> uriVariables = new HashMap<>();
                uriVariables.put("name", testEmployee.getName());
                uriVariables.put("surname", testEmployee.getSurname());

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/add?name={name}&surname={surname}", Employee.class, uriVariables);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void addAlreadyAdded() {
                Employee testEmployee = new Employee("Кудрявцев", "Лев");
                Map<String, String> uriVariables = new HashMap<>();
                uriVariables.put("name", testEmployee.getName());
                uriVariables.put("surname", testEmployee.getSurname());

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/add?name={name}&surname={surname}", Employee.class, uriVariables);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void deleteNotFound() {
                Map<String, String> uriVariables = new HashMap<>();
                uriVariables.put("name", "Антонина");
                uriVariables.put("surname", "Белякова");

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/delete?name={name}&surname={surname}", Employee.class, uriVariables);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            }

            @Test
            void findNotFound() {
                Employee testEmployee = new Employee("Иванов", "Иван");
                Map<String, String> uriVariables = new HashMap<>();
                uriVariables.put("name", testEmployee.getName());
                uriVariables.put("surname", testEmployee.getSurname());

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/find?name={name}&surname={surname}", Employee.class, uriVariables);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            }
        }

    }


    @Nested
    class ParametersError {
        @Nested
        class NoParameters {
            @Test
            void addNotFound() {
                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/add", Employee.class);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void findNotFound() {
                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/find", Employee.class);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void deleteNotFound() {
                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/delete", Employee.class);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

        }

        @Nested
        class ParametersIsEmpty {
            private static Map<String, String> uriVariables;

            @BeforeAll
            public static void setup() {
                uriVariables = new HashMap<>();
                uriVariables.put("name", null);
                uriVariables.put("surname", null);
            }

            @Test
            void add() {

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/add?name={name}&surname={surname}", Employee.class, uriVariables);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void delete() {

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/delete?name={name}&surname={surname}", Employee.class, uriVariables);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }

            @Test
            void find() {

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/find?name={name}&surname={surname}", Employee.class, uriVariables);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            }
        }

    }


}