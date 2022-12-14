package learning.javaguides.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.javaguides.springboot.model.Employee;
import learning.javaguides.springboot.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the resul or output using assert statements
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));

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

        BDDMockito.given(employeeService.getAllEmployees()).willReturn(employees);

        //when - action or behaviour we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the resul or output using assert statements
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employees.size())));
    }

}
