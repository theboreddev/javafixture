package com.theboreddev.fixture;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

public class FixturePlainObjectTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();


    @Test
    public void shouldPopulateACollectionOfObjects() {

        List<Employee> employees = Fixture.of(10, Employee.class).apply();

        assertThat(employees).hasSize(10).allMatch(this::isValidEmployee);
    }

    @Test
    public void shouldPopulateACollectionOfObjectsRestrictingTheValueOfOneField() {

        List<Employee> employees = Fixture.of(10, Employee.class)
                .withFieldSupplier("age", () -> Random.randomInt(18, 150))
                .apply();

        assertThat(employees).hasSize(10).allMatch(employee -> employee.getAge() >= 18 && employee.getAge() <= 150);
    }

    @Test
    public void shouldPopulateACollectionOfObjectsRestrictingTheValueOfMultipleFields() {

        List<Employee> employees = Fixture.of(10, Employee.class)
                .withFieldSupplier("age", () -> Random.randomInt(18, 150))
                .withFieldSupplier("salary", () -> Random.randomInt(8000, 100000))
                .apply();

        assertThat(employees).hasSize(10)
                .allMatch(employee -> employee.getAge() >= 18 && employee.getAge() <= 150)
                .allMatch(employee -> employee.getSalary() >= 8000 && employee.getSalary() <= 100000);
    }

    @Test
    public void shouldPopulateACollectionOfObjectsWithACustomSupplier() {

        List<Employee> employees = Fixture.of(10, Employee.class).withSupplier(() -> new Employee(
                Random.randomAlphaNumeric(10),
                Random.randomAlphaNumeric(20),
                Random.randomInt(100),
                Random.randomDouble(100000)
        )).apply();

        assertThat(employees).hasSize(10).allMatch(this::isValidEmployeeWithSomeRestrictions);
    }

    @Test
    public void shouldRaiseExceptionIfFieldSupplierIsSpecifiedForASimpleType() {

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage(is("Cannot specify field supplier if type is not an object!"));

        Fixture.of(10, String.class).withFieldSupplier("field", () -> Random.randomAlphaNumeric(10)).apply();
    }

    @Test
    public void shouldRaiseExceptionIfFieldDoesNotExist() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("Field 'nonExistingField' does not exist!"));

        Fixture.of(10, Employee.class).withFieldSupplier("nonExistingField", () -> Random.randomAlphaNumeric(10)).apply();
    }


    private boolean isValidEmployee(Employee employee) {
        return employee != null &&
                employee.getName().length() > 0 &&
                employee.getSurname().length() > 0 &&
                employee.getAge() > 0 && employee.getAge() <= 100 &&
                employee.getSalary() >= 0;
    }

    private boolean isValidEmployeeWithSomeRestrictions(Employee employee) {
        return employee != null &&
                employee.getName().length() == 10 &&
                employee.getSurname().length() == 20 &&
                employee.getAge() > 0 && employee.getAge() <= 100 &&
                employee.getSalary() >= 0 && employee.getSalary() <= 100000.0;
    }

    static class Employee {
        private final String name;
        private final String surname;
        private final int age;
        private final double salary;


        public Employee(String name, String surname, int age, double salary) {
            this.name = name;
            this.surname = surname;
            this.age = age;
            this.salary = salary;
        }


        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public int getAge() {
            return age;
        }

        public double getSalary() {
            return salary;
        }

        @Override
        public String toString() {
            return "Employee{" +
                    "name='" + name + '\'' +
                    ", surname='" + surname + '\'' +
                    ", age=" + age +
                    ", salary=" + salary +
                    '}';
        }
    }
}
