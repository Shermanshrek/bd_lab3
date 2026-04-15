package ru.david.bd_lab2.entity;

import java.math.BigDecimal;

public class Project {
    private Long projectId;

    private String projectName;

    private BigDecimal budget;

    private Department department;

    private Employee manager;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
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

    public Project() {}

    public Project(Long projectId, String projectName, BigDecimal budget, Department department, Employee manager) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.budget = budget;
        this.department = department;
        this.manager = manager;
    }
}
