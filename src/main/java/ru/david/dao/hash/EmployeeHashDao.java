package ru.david.dao.hash;

import redis.clients.jedis.Jedis;
import ru.david.entity.Department;
import ru.david.entity.Employee;

import java.math.BigDecimal;
import java.util.Map;

public class EmployeeHashDao {
    private final Jedis jedis;
    private final DepartmentHashDao departmentHashDao;

    public EmployeeHashDao(Jedis jedis) {
        this.jedis = jedis;
        departmentHashDao = new DepartmentHashDao(jedis);
    }

    public void save(Employee employee) {
        String key = "emp_hash:" + employee.getEmpId();
        jedis.hset(key, "fio", employee.getFio());
        jedis.hset(key, "salary", employee.getSalary().toString());
        jedis.hset(key, "department", String.valueOf(employee.getDepartment().getDepartmentId()));
        if (employee.getManager() != null) {
            jedis.hset(key, "managerId", String.valueOf(employee.getManager().getEmpId()));
        }
    }

    public Employee findById(Long id) {
        String key = "emp_hash:" + id;
        Map<String, String> map = jedis.hgetAll(key);
        if (map.isEmpty()) return null;
        String fio = map.get("fio");
        BigDecimal salary = new BigDecimal(map.get("salary"));
        Department dept = departmentHashDao.findById(Long.valueOf(map.get("department")));

        String managerIdStr = map.get("managerId");
        Employee manager = managerIdStr != null ? findById(Long.valueOf(managerIdStr)) : null;

        return new Employee(id, fio, salary, dept, manager);
    }

    public void delete(Long id) {
        jedis.del("emp_hash:" + id);
    }
}
