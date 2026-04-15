package ru.david.dao.hash;

import redis.clients.jedis.Jedis;
import ru.david.entity.Department;

import java.util.Map;

public class DepartmentHashDao {
    private final Jedis jedis;
    public DepartmentHashDao(Jedis jedis) {
        this.jedis = jedis;
    }

    public void save(Department department) {
        String key = "dept_hash:" + department.getDepartmentId();
        jedis.hset(key, "name", department.getDepartmentName());
    }

    public Department findById(Long id) {
        String key = "dept_hash:" + id;
        Map<String, String> map = jedis.hgetAll(key);
        if (map.isEmpty()) return null;
        return new Department(id, map.get("name"));
    }
    public void delete(Long id) {
        jedis.del("dept_hash:" + id);
    }
}
