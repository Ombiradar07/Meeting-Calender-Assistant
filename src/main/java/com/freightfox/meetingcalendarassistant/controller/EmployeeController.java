package com.freightfox.meetingcalendarassistant.controller;

import com.freightfox.meetingcalendarassistant.dto.EmployeeDTO;
import com.freightfox.meetingcalendarassistant.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @PostMapping("createEmployee")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Creating employee with employeeDTO.toString()");

        EmployeeDTO employee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @PostMapping("updateEmployee")
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO) {

        EmployeeDTO employee = employeeService.updateEmployee(employeeDTO);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @DeleteMapping("deleteEmployee")
    public ResponseEntity<String> deleteEmployee(Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>("Employee Deleted Successfully", HttpStatus.OK);

    }

    @GetMapping("getEmployee")
    public ResponseEntity<EmployeeDTO> getEmployeeById(Long id) {

        EmployeeDTO employee = employeeService.getEmployeeById(id);

        return new ResponseEntity<>(employee, HttpStatus.OK);

    }

    @GetMapping("getAllEmployees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {

        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);

    }

}
