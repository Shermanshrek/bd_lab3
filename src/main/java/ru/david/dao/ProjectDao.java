package ru.david.dao;

import redis.clients.jedis.Jedis;
import ru.david.entity.Department;
import ru.david.entity.Employee;
import ru.david.entity.Project;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProjectDao extends RedisDao{
    private static final String ENTITY_TYPE = "project";
    private final DepartmentDao departmentDao;
    private final EmployeeDao employeeDao;

    public ProjectDao(Jedis jedis) {
        super(jedis);
        this.departmentDao = new DepartmentDao(jedis);
        this.employeeDao = new EmployeeDao(jedis);
    }

    public void save(Project project) {
        String id = String.valueOf(project.getProjectId());
        setField(ENTITY_TYPE, id, "name", project.getProjectName());
        setField(ENTITY_TYPE, id, "budget", project.getBudget().toString());
        setField(ENTITY_TYPE, id, "departmentId", String.valueOf(project.getDepartment().getDepartmentId()));
        if (project.getManager() != null) {
            setField(ENTITY_TYPE, id, "managerId", String.valueOf(project.getManager().getEmpId()));
        }
    }

    public Project findById(Long id) {
        String idStr = String.valueOf(id);
        if (!existsEntity(ENTITY_TYPE, idStr)) {
            return null;
        }
        String name = getField(ENTITY_TYPE, idStr, "name");

        String budgetStr = getField(ENTITY_TYPE, idStr, "budget");
        BigDecimal budget = budgetStr != null ? new BigDecimal(budgetStr) : BigDecimal.ZERO;

        String deptIdStr = getField(ENTITY_TYPE, idStr, "departmentId");
        Department department = deptIdStr != null ? departmentDao.findById(Long.valueOf(deptIdStr)) : null;

        String managerIdStr = getField(ENTITY_TYPE, idStr, "managerId");
        Employee manager = managerIdStr != null ? employeeDao.findById(Long.valueOf(managerIdStr)) : null;

        return new Project(id, name, budget, department, manager);
    }

    public void delete(Long id) {
        deleteEntity(ENTITY_TYPE, String.valueOf(id));
    }

    public boolean exists(Long id) {
        return existsEntity(ENTITY_TYPE, String.valueOf(id));
    }

    public List<Project> findAll() {
        return getAllIds(ENTITY_TYPE).stream()
                .map(id -> findById(Long.parseLong(id)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
