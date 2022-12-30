package com.pactdemo.producer.dto;

public class Employee {

    private String name;
    private long salary;
    private long departmentId;
    private long experience;

    public Employee(String name, long salary, long departmentId, long experience) {
        this.name = name;
        this.salary = salary;
        this.departmentId = departmentId;
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }
}
