package ru.bratchin.javaCore25.repository.impl;

import org.springframework.stereotype.Repository;
import ru.bratchin.javaCore25.exception.EmployeeAlreadyAddedException;
import ru.bratchin.javaCore25.exception.EmployeeNotFoundException;
import ru.bratchin.javaCore25.model.entity.Employee;
import ru.bratchin.javaCore25.repository.api.MyRepository;
import ru.bratchin.javaCore25.specification.MySpecification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository implements MyRepository<Employee, String> {
    /*    private Map<String, Employee> employees = new HashMap<>(
                Map.of("Малышева Амалия", new Employee("Малышева", "Амалия", "2", 83166.43),
                        "Козловский Денис", new Employee("Козловский", "Денис", "1", 60250.60),
                        "Соловьева Серафима", new Employee("Соловьева", "Серафима", "3", 59343.29),
                        "Макарова Дарья", new Employee("Макарова", "Дарья", "1", 82042.89),
                        "Лебедева Таисия", new Employee("Лебедева", "Таисия", "5", 72881.88),
                        "Романов Артём", new Employee("Романов", "Артём", "2", 62761.97),
                        "Широков Павел", new Employee("Широков", "Павел", "4", 97159.11),
                        "Кудрявцев Лев", new Employee("Кудрявцев", "Лев", "2", 89845.70)
                ));*/
    private Map<String, Employee> employees = new HashMap<>();

    @Override
    public Map<String, Employee> findAll(MySpecification<Employee> specification) {
        return employees.entrySet()
                .stream()
                .filter(entry -> specification.test(entry.getValue()))
                .collect(
                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                );
    }

    @Override
    public Map<String, Employee> findAll() {
        return employees;
    }

    @Override
    public Optional<Employee> findOne(MySpecification<Employee> specification) {
        return employees.values().stream().filter(specification).findFirst();
    }

    @Override
    public void create(Employee employee) {
        var key = getKey(employee);
        if (employees.containsKey(key)) {
            throw new EmployeeAlreadyAddedException();
        } else {
            employees.put(key, employee);
        }
    }

    @Override
    public void create(List<Employee> employees) {
        employees.forEach(this::create);
    }

/*    @Override
    public void update(Employee employee) {
        var oldEmployee = employees.stream()
                .filter(employee1 -> employee.getId().equals(employee1.getId()))
                .findFirst()
                .orElseThrow(
                        () -> new NoSuchElementException("Попытка найти несуществующий элемент")
                );
        oldEmployee.setDepartment(employee.getDepartment());
        oldEmployee.setSalary(employee.getSalary());
    }

    @Override
    public void update(List<Employee> employees) {
        for (Employee employee : employees) {
            update(employee);
        }
    }*/

    @Override
    public void delete(Employee employee) {
        var key = getKey(employee);
        if (employees.containsKey(key)) {
            employees.remove(key, employee);
        } else {
            throw new EmployeeNotFoundException();
        }
    }

    @Override
    public void deleteAll() {
        employees = new HashMap<>();
    }

    private String getKey(Employee employee) {
        return employee.getSurname() + " " + employee.getName();
    }

}
