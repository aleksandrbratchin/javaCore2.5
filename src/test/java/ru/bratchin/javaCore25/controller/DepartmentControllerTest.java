package ru.bratchin.javaCore25.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.service.impl.DepartmentService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService service;


    @Nested
    class AllSuccess {

        @Test
        void maxSalary() throws Exception {
            Employee employee = new Employee("Кудрявцев", "Лев", "2", 89845.70);
            Mockito.when(service.maxSalary(anyString()))
                    .thenReturn(employee);

            mockMvc.perform(get("/department/{id}/max", "2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.salary").value(employee.getSalary()));
        }

        @Test
        void minSalary() throws Exception {
            Employee employee = new Employee("Романов", "Артём", "2", 62761.97);
            Mockito.when(service.minSalary(anyString()))
                    .thenReturn(employee);

            mockMvc.perform(get("/department/{id}/min", "2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.salary").value(employee.getSalary()));
        }

        @Test
        void filterByDepartment() throws Exception {
            Mockito.when(service.findByDepartment(anyString()))
                    .thenReturn(List.of(
                            new Employee("Малышева", "Амалия", "2", 83166.43),
                            new Employee("Романов", "Артём", "2", 62761.97),
                            new Employee("Кудрявцев", "Лев", "2", 89845.70)
                    ));

            mockMvc.perform(get("/department/{id}/employees", "2")
                            .param("departmentId", "2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)));
        }

/*        @Test
        void sum() throws Exception {
            Mockito.when(service.sum(anyString()))
                    .thenReturn(List.of(
                            new Employee("Малышева", "Амалия", "2", 60000.0),
                            new Employee("Романов", "Артём", "2", 70000.0),
                            new Employee("Кудрявцев", "Лев", "2", 80000.0)
                    ));

            mockMvc.perform(get("/department/{id}/sum","2")
                            .param("departmentId", "2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(21000));
        }*/

/*        @Test
        void getAll() throws Exception {
            Map<String, List<Employee>> testEmployees = new HashMap<>(
                    Map.of(
                            "1", List.of(
                                    new Employee("Малышева", "Амалия", "1", 83166.43),
                                    new Employee("Козловский", "Денис", "1", 60250.60),
                                    new Employee("Макарова", "Дарья", "1", 82042.89)
                            ),
                            "2", List.of(
                                    new Employee("Кудрявцев", "Лев", "2", 89845.70),
                                    new Employee("Романов", "Артём", "2", 62761.97)
                            ),
                            "4", List.of(
                                    new Employee("Широков", "Павел", "4", 97159.11)
                            ),
                            "5", List.of(
                                    new Employee("Лебедева", "Таисия", "5", 72881.88),
                                    new Employee("Филиппова", "Алиса", "5", 79209.12)
                            )
            ));
            Mockito.when(service.all())
                    .thenReturn(testEmployees);

            mockMvc.perform(get("/employee/department/all")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(4)));
        }*/

    }

    /*@Nested
    class AllError {

        @Nested
        class DepartmentIsNull {
            @BeforeEach
            public void initEach() throws IllegalAccessException {
                Map<String, Employee> testEmployees = new HashMap<>(
                        Map.of("Малышева Амалия", new Employee("Малышева", "Амалия", null, 83166.43),
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

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(response.getBody().getSalary()).isNull();
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

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(response.getBody().getSalary()).isNull();
            }

            @Test
            void filterByDepartment() {
                Map<String, String> uriVariables = new HashMap<>();
                uriVariables.put("departmentId", "2");

                ResponseEntity<DepartmentIsNullException> response
                        = restTemplate.exchange(
                        "/employee/department/all?departmentId={departmentId}",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {},
                        uriVariables
                );

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(response.getBody().getMessage()).contains("Малышева");
            }

            @Test
            void getAll() {

                ResponseEntity<DepartmentIsNullException> response
                        = restTemplate.exchange(
                        "/employee/department/all",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(response.getBody().getMessage()).contains("Малышева");
            }

        }

        @Nested
        class SalaryIsNull {
            @BeforeEach
            public void initEach() throws IllegalAccessException {
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
                Map<String, String> uriVariables = new HashMap<>();
                uriVariables.put("departmentId", "2");

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/department/max-salary?departmentId={departmentId}", Employee.class, uriVariables);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(response.getBody().getSalary()).isNull();
            }

            @Test
            void minSalary() {
                Map<String, String> uriVariables = new HashMap<>();
                uriVariables.put("departmentId", "2");

                ResponseEntity<Employee> response
                        = restTemplate.getForEntity("/employee/department/min-salary?departmentId={departmentId}", Employee.class, uriVariables);

                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                assertThat(response.getBody().getSalary()).isNull();
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

                ResponseEntity<List<Employee>> response
                        = restTemplate.exchange(
                        "/employee/department/all?departmentId={departmentId}",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
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

    }*/

    @Nested
    class ParametersError {

        @Nested
        class ParametersIsEmpty {

            @Test
            void maxSalary() throws Exception {
                mockMvc.perform(get("/department/{id}/max", "")
                                .param("departmentId", "")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

            @Test
            void minSalary() throws Exception {
                mockMvc.perform(get("/department/{id}/min", "")
                                .param("departmentId", "")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

            @Test
            void sum() throws Exception {
                mockMvc.perform(get("/department/{id}/sum", "")
                                .param("departmentId", "")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

        }

    }

}