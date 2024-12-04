package com.freightfox.meetingcalendarassistant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @GetMapping("/test")
    public String hello() {
        return "Hello, Meeting Calendar Assistant!";
    }
}
