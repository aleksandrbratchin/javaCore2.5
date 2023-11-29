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
import ru.bratchin.javaCore25.exception.EmployeeAlreadyAddedException;
import ru.bratchin.javaCore25.exception.EmployeeNotFoundException;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.service.api.EmployeeService;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService service;

    @Nested
    class TestAddDeleteFind {

        @Nested
        class AllSuccess {
            @Test
            void add() throws Exception {
                Employee testEmployee = new Employee("Белякова", "Антонина");
                Mockito.when(service.add(any(Employee.class)))
                        .thenReturn(testEmployee);

                mockMvc.perform(get("/employee/add")
                                .param("name", testEmployee.getName())
                                .param("surname", testEmployee.getSurname())
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value("Антонина"))
                        .andExpect(jsonPath("$.surname").value("Белякова"));
            }

            @Test
            void delete() throws Exception {
                Employee testEmployee = new Employee("Кудрявцев", "Лев");
                Mockito.when(service.delete(any(Employee.class)))
                        .thenReturn(testEmployee);

                mockMvc.perform(get("/employee/delete")
                                .param("name", testEmployee.getName())
                                .param("surname", testEmployee.getSurname())
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value("Лев"))
                        .andExpect(jsonPath("$.surname").value("Кудрявцев"));
            }

            @Test
            void find() throws Exception {
                Employee testEmployee = new Employee("Кудрявцев", "Лев");
                Mockito.when(service.find(any(Employee.class)))
                        .thenReturn(testEmployee);

                mockMvc.perform(get("/employee/find")
                                .param("name", testEmployee.getName())
                                .param("surname", testEmployee.getSurname())
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value("Лев"))
                        .andExpect(jsonPath("$.surname").value("Кудрявцев"));
            }

            @Test
            void findAll() throws Exception {
                Map<String, Employee> correctEmployees = new HashMap<>(
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
                Mockito.when(service.findAll())
                        .thenReturn(correctEmployees.values()
                                .stream().toList());

                mockMvc.perform(get("/employee/findAll")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(9)))
                        .andExpect(jsonPath("$.*", hasSize(9)));
            }

        }

        @Nested
        class AllError {
            @Test
            void addStorageIsFull() throws Exception {
                Mockito.when(service.add(any(Employee.class)))
                        .thenThrow(EmployeeAlreadyAddedException.class);

                mockMvc.perform(get("/employee/add")
                                .param("name", "Иван")
                                .param("surname", "Иванов")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            void addAlreadyAdded() throws Exception {
                Mockito.when(service.add(any(Employee.class)))
                        .thenThrow(EmployeeAlreadyAddedException.class);

                mockMvc.perform(get("/employee/add")
                                .param("name", "Иван")
                                .param("surname", "Иванов")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            void deleteNotFound() throws Exception {
                Mockito.when(service.delete(any(Employee.class)))
                        .thenThrow(EmployeeNotFoundException.class);

                mockMvc.perform(get("/employee/delete")
                                .param("name", "Иван")
                                .param("surname", "Иванов")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

            @Test
            void findNotFound() throws Exception {
                Mockito.when(service.find(any(Employee.class)))
                        .thenThrow(EmployeeNotFoundException.class);

                mockMvc.perform(get("/employee/find")
                                .param("name", "Иван")
                                .param("surname", "Иванов")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }

    }


    @Nested
    class ParametersError {
        @Nested
        class NoParameters {
            @Test
            void add() throws Exception {
                mockMvc.perform(get("/employee/add")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            void find() throws Exception {
                mockMvc.perform(get("/employee/find")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            void delete() throws Exception {
                mockMvc.perform(get("/employee/delete")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

        }

        @Nested
        class ParametersIsEmpty {

            @Test
            void add() throws Exception {
                mockMvc.perform(get("/employee/add")
                                .param("name", "")
                                .param("surname", "")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            void delete() throws Exception {
                mockMvc.perform(get("/employee/delete")
                                .param("name", "")
                                .param("surname", "")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            void find() throws Exception {
                mockMvc.perform(get("/employee/find")
                                .param("name", "")
                                .param("surname", "")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

        }

    }


}