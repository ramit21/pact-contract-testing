package com.pactdemo.producer.controller;

import com.pactdemo.producer.dto.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EmployeeController {

    private List<Employee> employees ;

    public EmployeeController(){
        employees = prepareEmployees();
    }

    @GetMapping("/")
    @ResponseBody
    public String welcome(){
        return "Welcome to Producer app";
    }

    @GetMapping("/getAllEmployees")
    @ResponseBody
    public List<Employee> getAllEmployees(){
        return employees;
    }

    private List<Employee> prepareEmployees(){
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Ramit", 10, 1, 16));
        employees.add(new Employee("Steve", 10, 1, 8));
        employees.add(new Employee("David", 10, 1, 4));
        return employees;
    }

}
