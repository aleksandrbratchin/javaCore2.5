package ru.bratchin.javaCore25.specification.employee;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.bratchin.javaCore25.exception.SalaryIsNullException;
import ru.bratchin.javaCore25.model.entity.Employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class EmployeeSalaryLessThanSpecificationTest {

    private final Employee correctEmployee = new Employee("Малышева", "Амалия", "2", 83166.43);
    private final Employee incorrectEmployee = new Employee("Малышева", "Амалия", "2", null);

    @Nested
    class Success {
        @Test
        void employeeSalaryLess() {
            EmployeeSalaryLessThanSpecification specification = new EmployeeSalaryLessThanSpecification(90000D);

            Boolean test = specification.test(correctEmployee);

            assertThat(test).isTrue();
        }

        @Test
        void employeeSalaryMore() {
            EmployeeSalaryLessThanSpecification specification = new EmployeeSalaryLessThanSpecification(80000D);

            Boolean test = specification.test(correctEmployee);

            assertThat(test).isFalse();
        }

        @Test
        void employeeSalaryEqual() {
            EmployeeSalaryLessThanSpecification specification = new EmployeeSalaryLessThanSpecification(83166.43);

            Boolean test = specification.test(correctEmployee);

            assertThat(test).isFalse();
        }
    }


    @Nested
    class Error {
        @Test
        void incorrectEmployee() {
            EmployeeSalaryLessThanSpecification specification = new EmployeeSalaryLessThanSpecification(90000D);

            Throwable thrown = catchThrowable(() -> specification.test(incorrectEmployee));

            assertThat(thrown).isInstanceOf(SalaryIsNullException.class)
                    .hasMessageContaining("Малышева");
        }

        @Test
        void salaryIsNull() {

            Throwable thrown = catchThrowable(() -> new EmployeeSalaryLessThanSpecification(null));

            assertThat(thrown).isInstanceOf(SalaryIsNullException.class);
        }
    }


}