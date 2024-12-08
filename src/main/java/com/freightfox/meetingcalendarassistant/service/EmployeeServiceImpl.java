package com.freightfox.meetingcalendarassistant.service;

import com.freightfox.meetingcalendarassistant.dto.EmployeeDTO;
import com.freightfox.meetingcalendarassistant.entity.Employee;
import com.freightfox.meetingcalendarassistant.exception.EmployeeAlreadyExistsException;
import com.freightfox.meetingcalendarassistant.exception.EmployeeNotFoundException;
import com.freightfox.meetingcalendarassistant.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            ModelMapper modelMapper
    ) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDto) {

        //TODO Check if employee already exists in DB
        Optional<Employee> byEmail = employeeRepository.findByEmail(employeeDto.getEmail());
        if (byEmail.isPresent()) {
            throw new EmployeeAlreadyExistsException("Employee already exists with email " + employeeDto.getEmail());
        }

        // TODO Else Save employee to DB
        Employee employee = modelMapper.map(employeeDto, Employee.class);
        employeeRepository.save(employee);

        return modelMapper.map(employee, EmployeeDTO.class);

    }

    public EmployeeDTO updateEmployee(EmployeeDTO employeeDto) {
        Employee employee = employeeRepository.findById(employeeDto.getId()).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + employeeDto.getId()));

        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());

        employeeRepository.save(employee);

        return modelMapper.map(employee, EmployeeDTO.class);
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id " + id));

        return modelMapper.map(employee, EmployeeDTO.class);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(element -> modelMapper.map(element, EmployeeDTO.class)).toList();
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) throw new EmployeeNotFoundException("Employee not found with id " + id);
        employeeRepository.deleteById(id);
    }
}
