package learning.javaguides.springboot.service.impl;

import learning.javaguides.springboot.exception.ResourceNotFoundException;
import learning.javaguides.springboot.model.Employee;
import learning.javaguides.springboot.repository.EmployeeRepository;
import learning.javaguides.springboot.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;


    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

        if (savedEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exist with given email:" + employee.getEmail());
        }

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        return employeeRepository.findById(id);
    }

    @Override

    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }

}
