package ru.david.dao;

import redis.clients.jedis.Jedis;
import ru.david.entity.Department;
import ru.david.entity.Employee;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EmployeeDao extends RedisDao{
    private static final String ENTITY_TYPE = "employee";
    private final DepartmentDao departmentDao;

    public EmployeeDao(Jedis jedis) {
        super(jedis);
        this.departmentDao = new DepartmentDao(jedis);
    }

    public void save(Employee employee) {
        String id = String.valueOf(employee.getEmpId());
        setField(ENTITY_TYPE, id, "fio", employee.getFio());
        setField(ENTITY_TYPE, id, "salary", employee.getSalary().toString());
        setField(ENTITY_TYPE, id, "departmentId", String.valueOf(employee.getDepartment().getDepartmentId()));
        if (employee.getManager() != null) {
            setField(ENTITY_TYPE, id, "managerId", String.valueOf(employee.getManager().getEmpId()));
        }
    }

    public Employee findById(Long id) {
        String idStr = String.valueOf(id);
        if (!existsEntity(ENTITY_TYPE, idStr)) {
            return null;
        }
        String fio = getField(ENTITY_TYPE, idStr, "fio");

        String salaryStr = getField(ENTITY_TYPE, idStr, "salary");
        BigDecimal salary = salaryStr != null ? new BigDecimal(salaryStr) : BigDecimal.ZERO;

        String departmentIdStr = getField(ENTITY_TYPE, idStr, "departmentId");
        Department department = departmentIdStr != null? departmentDao.findById(Long.parseLong(departmentIdStr)) : null;

        String managerIdStr = getField(ENTITY_TYPE, idStr, "managerId");
        Employee manager = managerIdStr != null? findById(Long.parseLong(managerIdStr)) : null;

        return new Employee(id, fio, salary, department, manager);
    }

    public void delete(Long id) {
        deleteEntity(ENTITY_TYPE, String.valueOf(id));
    }

    public boolean exists(Long id) {
        return existsEntity(ENTITY_TYPE, String.valueOf(id));
    }

    public List<Employee> findAll() {
        return getAllIds(ENTITY_TYPE).stream()
                .map(id -> findById(Long.parseLong(id)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
