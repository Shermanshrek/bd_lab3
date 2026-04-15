package ru.david.entity;

import java.math.BigDecimal;
import java.util.List;

public class Employee {
    private Long empId;

    private String fio;

    private BigDecimal salary;

    private Department department;

    private Employee manager;

    private List<Employee> subordinates;

    public Employee(Object o, String fio, BigDecimal bigDecimal, Department itDept, Object o1) {
        this.empId = (Long) o;
        this.fio = fio;
        this.salary = bigDecimal;
        this.department = itDept;
    }

    public Employee(Long empId, String fio, BigDecimal salary, Department department, Employee manager, List<Employee> subordinates) {
        this.empId = empId;
        this.fio = fio;
        this.salary = salary;
        this.department = department;
        this.manager = manager;
        this.subordinates = subordinates;
    }

    public Employee() {}

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(List<Employee> subordinates) {
        this.subordinates = subordinates;
    }
}
