package ru.david.dao;

import redis.clients.jedis.Jedis;
import ru.david.entity.Department;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DepartmentDao extends RedisDao{
    private static final String ENTITY_TYPE = "department";

    public DepartmentDao(Jedis jedis) {
        super(jedis);
    }

    public void save(Department department) {
        String id = String.valueOf(department.getDepartmentId());
        setField(ENTITY_TYPE, id, "name", department.getDepartmentName());
    }

    public Department findById(Long id) {
        String idStr = String.valueOf(id);
        if (!existsEntity(ENTITY_TYPE, idStr)) {
            return null;
        }
        String name = getField(ENTITY_TYPE, idStr, "name");
        return new Department(id, name);
    }

    public void delete(Long id) {
        deleteEntity(ENTITY_TYPE, String.valueOf(id));
    }

    public boolean exists(Long id) {
        return existsEntity(ENTITY_TYPE, String.valueOf(id));
    }

    public List<Department> findAll() {
        return getAllIds(ENTITY_TYPE).stream()
                .map(id -> findById(Long.parseLong(id)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
