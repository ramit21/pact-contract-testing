package com.pactdemo.consumer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pactdemo.consumer.model.Department;
import com.pactdemo.consumer.model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DepartmentController {

    private String baseUrl ="http://localhost:8100";

    @GetMapping("/")
    @ResponseBody
    public String welcome(){
        return "Welcome to Consumer app";
    }

    @GetMapping("/getTotalSalaryByDeptId/{deptId}")
    @ResponseBody
    public Department getTotalSalaryBydept(@PathVariable Long deptId) {
        Department dept = new Department(deptId);
        dept.setTotalEmployeeSalary(fetchEmployeeSalaryByDept(deptId));
        return dept;
    }

    private long fetchEmployeeSalaryByDept(Long deptId) {
        long totalSalary = 0;
        try{
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(baseUrl+"/getAllEmployees", String.class);
            ObjectMapper mapper = new ObjectMapper();
            Employee[] employees = mapper.readValue(response.getBody(), Employee[].class);
            for(Employee emp : employees){
                if( emp.getDepartmentId() == deptId){
                    totalSalary+= emp.getSalary();
                }
            }
        } catch(Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
        return totalSalary;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
