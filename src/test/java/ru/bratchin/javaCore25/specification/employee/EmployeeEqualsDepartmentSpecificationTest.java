package ru.bratchin.javaCore25.specification.employee;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.bratchin.javaCore25.exception.DepartmentIsNullException;
import ru.bratchin.javaCore25.model.entity.Employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class EmployeeEqualsDepartmentSpecificationTest {

    private final Employee correctEmployee = new Employee("Малышева", "Амалия", "2", 83166.43);

    @Nested
    class Success {
        @Test
        void departmentsAreEqual() {
            EmployeeEqualsDepartmentSpecification specification = new EmployeeEqualsDepartmentSpecification("2");

            Boolean test = specification.test(correctEmployee);

            assertThat(test).isTrue();
        }

        @Test
        void departmentsAreNotEqual() {
            EmployeeEqualsDepartmentSpecification specification = new EmployeeEqualsDepartmentSpecification("3");

            Boolean test = specification.test(correctEmployee);

            assertThat(test).isFalse();
        }
    }

    @Nested
    class Error {
        private final Employee incorrectEmployee = new Employee("Малышева", "Амалия", null, 83166.43);

        @Test
        void incorrectEmployee() {
            EmployeeEqualsDepartmentSpecification specification = new EmployeeEqualsDepartmentSpecification("2");

            Throwable thrown = catchThrowable(() -> specification.test(incorrectEmployee));

            assertThat(thrown).isInstanceOf(DepartmentIsNullException.class)
                    .hasMessageContaining("Малышева");
        }

        @Test
        void departmentIsNull() {
            EmployeeEqualsDepartmentSpecification specification = new EmployeeEqualsDepartmentSpecification(null);

            Boolean test = specification.test(correctEmployee);

            assertThat(test).isFalse();
        }

        @Test
        void incorrectEmployeeAndDepartmentIsNull() {
            EmployeeEqualsDepartmentSpecification specification = new EmployeeEqualsDepartmentSpecification(null);

            Throwable thrown = catchThrowable(() -> specification.test(incorrectEmployee));

            assertThat(thrown).isInstanceOf(DepartmentIsNullException.class)
                    .hasMessageContaining("Малышева");
        }
    }


}