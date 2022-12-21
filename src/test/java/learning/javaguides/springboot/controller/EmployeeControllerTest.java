package learning.javaguides.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.javaguides.springboot.model.Employee;
import learning.javaguides.springboot.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    ObjectMapper objectMapper;

    //JUnit Test for Create Employee
    @Test
    public void givenEmployeeObject_whenCreateEmployee_ThenReturnsSavedEmployee() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Devang")
                .lastName("Chauhan")
                .email("devang@gmail.com")
                .build();
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the resul or output using assert statements
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    //Junit Test for get All Employees
    @Test
    public void givenListOfEmployees_whenGetAllEmplpoyees_thenReturnEmpployeeList() throws Exception {

        //given - precondition or setup
        List<Employee> employees = new ArrayList<>();
        employees.add(Employee.builder()
                .firstName("Devang")
                .lastName("Chauhan")
                .email("devang@gmail.com")
                .build());

        employees.add(Employee.builder()
                .firstName("Drisana")
                .lastName("Chauhan")
                .email("drisana@gmail.com")
                .build());

        given(employeeService.getAllEmployees()).willReturn(employees);

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the resul or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(employees.size())));
    }

    //positive scenario - valid employee id
    //JUnit Test for GET employee by id REST API
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Devang")
                .lastName("Chauhan")
                .email("devang@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the resul or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail()
                )));
    }

    //negative scenario - valid employee id
    //JUnit Test for GET employee by id REST API
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Devang")
                .lastName("Chauhan")
                .email("devang@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the resul or output using assert statements
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //JUnit Test for Update employee by id REST API -positive scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Devang")
                .lastName("Chauhan")
                .email("devang@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Jay")
                .lastName("Parikh")
                .email("jay@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the resul or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    //JUnit Test for Update employee by id REST API -negative scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnStatus404() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Devang")
                .lastName("Chauhan")
                .email("devang@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Jay")
                .lastName("Parikh")
                .email("jay@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the resul or output using assert statements
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //JUnit Test for delete employee REST API
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnStatus200() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        //then - verify the resul or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print());
    }

}
