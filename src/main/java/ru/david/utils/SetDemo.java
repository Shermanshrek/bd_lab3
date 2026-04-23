package ru.david.utils;

import redis.clients.jedis.Jedis;

import java.util.Set;

public class SetDemo {
    private final Jedis jedis;

    public SetDemo(Jedis jedis) {
        this.jedis = jedis;
    }

    public void demo() {
        String setA = "programmers";
        String setB = "designers";
        System.out.println("=== Демонстрация множеств ===");
        jedis.del(setA, setB);

        jedis.sadd(setA, "Alice", "Bob", "Charlie");
        jedis.sadd(setB, "Bob", "David", "Eve");
        System.out.println("Программисты: " + jedis.smembers(setA));
        System.out.println("Дизайнеры: " + jedis.smembers(setB));

        // Пересечение
        Set<String> intersect = jedis.sinter(setA, setB);
        System.out.println("Пересечение: " + intersect);

        // Разность (A - B)
        Set<String> diff = jedis.sdiff(setA, setB);
        System.out.println("Программисты не дизайнеры: " + diff);

        // Объединение
        Set<String> union = jedis.sunion(setA, setB);
        System.out.println("Объединение: " + union);

        // Проверка принадлежности
        System.out.println("Bob программист? " + jedis.sismember(setA, "Bob"));
        // Удаление
        jedis.srem(setA, "Charlie");
        System.out.println("После удаления Charlie: " + jedis.smembers(setA));
    }
}
