package com.pactdemo.consumer.model;

public class Department {
    private long departmentId;
    private long totalEmployeeSalary;

    public Department(long departmentId) {
        this.departmentId = departmentId;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public long getTotalEmployeeSalary() {
        return totalEmployeeSalary;
    }

    public void setTotalEmployeeSalary(long totalEmployeeSalary) {
        this.totalEmployeeSalary = totalEmployeeSalary;
    }
}
