package com.freightfox.meetingcalendarassistant.config;

import com.freightfox.meetingcalendarassistant.dto.EmployeeDTO;
import com.freightfox.meetingcalendarassistant.entity.Employee;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // Define ModelMapper bean for dependency injection
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Add mappings for Employee -> EmployeeDTO
        modelMapper.addMappings(new PropertyMap<Employee, EmployeeDTO>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getId());
                map(source.getName(), destination.getName());
                map(source.getEmail(), destination.getEmail());
            }
        });

        return modelMapper;
    }
}
