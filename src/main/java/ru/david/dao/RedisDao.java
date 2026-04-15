package ru.david.dao;

import redis.clients.jedis.Jedis;

import java.util.Set;

public abstract class RedisDao {
    protected Jedis jedis;

    protected RedisDao(Jedis jedis) {
        this.jedis = jedis;
    }

    protected void setField(String entityType, String entityId, String fieldName, String value) {
        String key = String.format("%s:%s:%s", entityType, entityId, fieldName);
        jedis.set(key, value);
    }

    protected String getField(String entityType, String entityId, String fieldName) {
        String key = String.format("%s:%s:%s", entityType, entityId, fieldName);
        return jedis.get(key);
    }

    protected void deleteEntity(String entityType, String entityId){
        Set<String> keys = jedis.keys(String.format("%s:%s", entityType, entityId));
        if (!keys.isEmpty()){
            jedis.del(keys.toArray(new String[0]));
        }
    }

    protected boolean existsEntity(String entityType, String entityId) {
        Set<String> keys = jedis.keys(String.format("%s:%s:*", entityType, entityId));
        return keys != null && !keys.isEmpty();
    }

    protected Set<String> getAllIds(String entityType) {
        Set<String> keys = jedis.keys(String.format("%s:*:*", entityType));
        return keys.stream()
                .map(key -> key.split(":")[1])
                .collect(java.util.stream.Collectors.toSet());
    }
}
