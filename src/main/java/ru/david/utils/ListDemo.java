package ru.david.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.ListPosition;

import java.util.List;

public class ListDemo {
    private final Jedis jedis;

    public ListDemo(Jedis jedis) {
        this.jedis = jedis;
    }

    // Добавление элементов
    public void addToList(String listName, String... values) {
        jedis.rpush(listName, values);
    }

    // Удаление элемента (первое вхождение)
    public void removeFromList(String listName, String value) {
        jedis.lrem(listName, 1, value);
    }

    // Получение всех элементов
    public List<String> getList(String listName) {
        return jedis.lrange(listName, 0, -1);
    }

    // Демонстрация всех операций
    public void demo() {
        String listKey = "tasks";
        System.out.println("=== Демонстрация списков ===");
        jedis.del(listKey);

        addToList(listKey, "task1", "task2", "task3");
        System.out.println("После rpush: " + getList(listKey));

        jedis.lpush(listKey, "priority_task");
        System.out.println("После lpush: " + getList(listKey));

        String popped = jedis.rpop(listKey);
        System.out.println("rpop: " + popped + ", осталось: " + getList(listKey));

        removeFromList(listKey, "task2");
        System.out.println("После lrem task2: " + getList(listKey));

        jedis.linsert(listKey, ListPosition.AFTER, "task1", "new_task");
        System.out.println("После linsert после task1: " + getList(listKey));
    }
}
