package com.pactdemo.consumer.controller;

import com.pactdemo.consumer.model.Department;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController {

    @GetMapping("/")
    @ResponseBody
    public String welcome(){
        return "Welcome to Consumer app";
    }

    @GetMapping("/getTotalSalaryByDeptId/{deptId}")
    @ResponseBody
    public Department getGreetingMessage(@PathVariable Long deptId) {
        Department dept = new Department(deptId);
        dept.setTotalEmployeeSalary(fetchEmployeeSalaryByDept(deptId));
        return dept;
    }

    private long fetchEmployeeSalaryByDept(Long deptId) {
        return 30;
    }
}
