package ru.david.dao.hash;

import redis.clients.jedis.Jedis;
import ru.david.entity.Department;
import ru.david.entity.Employee;
import ru.david.entity.Project;

import java.math.BigDecimal;
import java.util.Map;

public class ProjectHashDao {
    private final Jedis jedis;
    private final DepartmentHashDao departmentHashDao;
    private final EmployeeHashDao employeeHashDao;

    public ProjectHashDao(Jedis jedis) {
        this.jedis = jedis;
        departmentHashDao = new DepartmentHashDao(jedis);
        employeeHashDao = new EmployeeHashDao(jedis);
    }

    public void save(Project project) {
        String key = "project_hash:" + project.getProjectId();
        jedis.hset(key, "name", project.getProjectName());
        jedis.hset(key, "budget", String.valueOf(project.getBudget()));
        jedis.hset(key, "department", String.valueOf(project.getDepartment().getDepartmentId()));

        if (project.getManager() != null) {
            jedis.hset(key, "managerId", String.valueOf(project.getManager().getEmpId()));
        }
    }

    public Project findById(Long id) {
        String key = "project_hash:" + id;
        Map<String, String> map = jedis.hgetAll(key);
        if (map.isEmpty()) return null;
        String projectName = map.get("name");
        BigDecimal budget = new BigDecimal(map.get("budget"));
        Department department = departmentHashDao.findById(Long.valueOf(map.get("department")));

        String managerIdStr = map.get("managerId");
        Employee manager = managerIdStr != null ? employeeHashDao.findById(Long.valueOf(managerIdStr)) : null;

        return new Project(id, projectName, budget, department, manager);
    }

    public void delete(Long id) {
        jedis.del("project_hash:" + id);
    }
}
