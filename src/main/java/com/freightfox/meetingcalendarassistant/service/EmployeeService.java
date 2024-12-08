package com.freightfox.meetingcalendarassistant.service;

import com.freightfox.meetingcalendarassistant.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO employeeDto);

    public EmployeeDTO updateEmployee(EmployeeDTO employeeDto);

    EmployeeDTO getEmployeeById(Long id);

    List<EmployeeDTO> getAllEmployees();

    void deleteEmployee(Long id);
}
