package learning.javaguides.springboot.service;

import learning.javaguides.springboot.exception.ResourceNotFoundException;
import learning.javaguides.springboot.model.Employee;
import learning.javaguides.springboot.repository.EmployeeRepository;
import learning.javaguides.springboot.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Devang")
                .lastName("Chauhan")
                .email("devang@gmail.com")
                .build();
    }

    //JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //JUnit test for saveEmployee method which throws exception
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {

        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or the behaviour that we are going to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //Then - verify
        verify(employeeRepository, never()).save(employee);
    }

    //JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method")
    @Test
    public void givenEmployeesList_whenSaveGetAllEmployees_thenReturnEmployeesList() {

        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Snehal")
                .lastName("Chauhan")
                .email("snehal@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        // when - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //Then - verify
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //JUnit test for getAllEmployees method [negative scenario]
    @DisplayName("JUnit test for getAllEmployees method [negative scenario]")
    @Test
    public void givenEmptyEmployeesList_whenSaveGetAllEmployees_thenReturnEmptyEmployeesList() {

        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Snehal")
                .lastName("Chauhan")
                .email("snehal@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //Then - verify
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    //JUnit test for getAllEmployeeById method
    @DisplayName("JUnit test for getAllEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {

        //given - precondition or setup
        given(employeeRepository.findById(1l)).willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        //Then - verify
        assertThat(savedEmployee).isNotNull();

    }

    //JUnit test for updateEmployee method
    @DisplayName("JUnit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdatedEmployee_thenReturnUpdatedEmployeeObject() {

        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("drisana@gmail.com");
        employee.setFirstName("Drisana");

        // when - action or the behaviour that we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //Then - verify
        assertThat(updatedEmployee.getEmail()).isEqualTo("drisana@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Drisana");
    }

    //JUnit test for deleteEmployee method
    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {

        long employeeId = 1L;

        //given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when - action or the behaviour that we are going to test
        employeeService.deleteEmployee(employeeId);

        //Then - verify
        verify(employeeRepository, times(1)).deleteById(employeeId);

    }

}
