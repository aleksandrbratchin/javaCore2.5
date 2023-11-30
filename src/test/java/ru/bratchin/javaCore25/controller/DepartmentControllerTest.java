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
import ru.bratchin.javaCore25.exception.DepartmentIsNullException;
import ru.bratchin.javaCore25.exception.SalaryIsNullException;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.service.impl.DepartmentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService service;


    @Nested
    class Success {

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

        @Test
        void sum() throws Exception {
            Mockito.when(service.sum(anyString()))
                    .thenReturn(60000.0);

            mockMvc.perform(get("/department/{id}/sum", "2")
                            .param("departmentId", "2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(60000.0));
        }

        @Test
        void all() throws Exception {
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

            mockMvc.perform(get("/department")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(4)));
        }


        @Nested
        class EmployeesIsEmpty {

            @Test
            void maxSalary() throws Exception {
                Mockito.when(service.maxSalary(anyString()))
                        .thenReturn(null);

                mockMvc.perform(get("/department/{id}/max", "2")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(""));

            }

            @Test
            void minSalary() throws Exception {
                Mockito.when(service.minSalary(anyString()))
                        .thenReturn(null);

                mockMvc.perform(get("/department/{id}/min", "2")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string(""));

            }

            @Test
            void sum() throws Exception {
                Mockito.when(service.sum(anyString()))
                        .thenReturn(0.0);

                mockMvc.perform(get("/department/{id}/sum", "2")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().string("0.0"));

            }

            @Test
            void findByDepartment() throws Exception {
                Mockito.when(service.findByDepartment(anyString()))
                        .thenReturn(new ArrayList<>());

                mockMvc.perform(get("/department/{id}/employees", "2")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(0)));
            }

            @Test
            void all() throws Exception {
                Mockito.when(service.all())
                        .thenReturn(new HashMap<>());

                mockMvc.perform(get("/department")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.*", hasSize(0)));
            }
        }
    }

    @Nested
    class Error {

        @Nested
        class DepartmentIsNull {

            @Test
            void maxSalary() throws Exception {
                Mockito.when(service.maxSalary(anyString()))
                        .thenThrow(DepartmentIsNullException.class);

                mockMvc.perform(get("/department/{id}/max", "2")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

            @Test
            void minSalary() throws Exception {
                Mockito.when(service.minSalary(anyString()))
                        .thenThrow(DepartmentIsNullException.class);

                mockMvc.perform(get("/department/{id}/min", "2")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

            @Test
            void filterByDepartment() throws Exception {
                Mockito.when(service.findByDepartment(anyString()))
                        .thenThrow(DepartmentIsNullException.class);

                mockMvc.perform(get("/department/{id}/employees", "2")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

            @Test
            void sum() throws Exception {
                Mockito.when(service.sum(anyString()))
                        .thenThrow(DepartmentIsNullException.class);

                mockMvc.perform(get("/department/{id}/sum", "2")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

            @Test
            void all() throws Exception {
                Mockito.when(service.all())
                        .thenThrow(DepartmentIsNullException.class);

                mockMvc.perform(get("/department")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

        }

        @Nested
        class SalaryIsNull {

            @Test
            void maxSalary() throws Exception {
                Mockito.when(service.maxSalary(anyString()))
                        .thenThrow(SalaryIsNullException.class);

                mockMvc.perform(get("/department/{id}/max", "2")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

            @Test
            void minSalary() throws Exception {
                Mockito.when(service.minSalary(anyString()))
                        .thenThrow(SalaryIsNullException.class);

                mockMvc.perform(get("/department/{id}/min", "2")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

            @Test
            void sum() throws Exception {
                Mockito.when(service.sum(anyString()))
                        .thenThrow(SalaryIsNullException.class);

                mockMvc.perform(get("/department/{id}/sum", "2")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

        }


    }

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