package ru.bratchin.javaCore25.specification.employee;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.bratchin.javaCore25.model.entity.Employee;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeSpecificationTest {

    private final Employee correctEmployee = new Employee("Малышева", "Амалия", "2", 83166.43);

    @Nested
    class Success {
        @Test
        void employeeAreEqual() {
            EmployeeSpecification specification = new EmployeeSpecification(
                    new Employee("Малышева", "Амалия", null, null)
            );

            Boolean test = specification.test(correctEmployee);

            assertThat(test).isTrue();
        }

        @Test
        void employeeAreNotEqual() {
            EmployeeSpecification specification = new EmployeeSpecification(
                    new Employee("", "Амалия", null, null)
            );

            Boolean test = specification.test(correctEmployee);

            assertThat(test).isFalse();
        }
    }


}