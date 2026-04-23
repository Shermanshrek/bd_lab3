package ru.david.utils;

import redis.clients.jedis.Jedis;

import java.util.List;

public class ZSetDemo {
    private final Jedis jedis;

    public ZSetDemo(Jedis jedis) {
        this.jedis = jedis;
    }

    public void demo() {
        String zSetKey = "salary_rank";
        System.out.println("=== Демонстрация упорядоченных множеств ===");
        jedis.del(zSetKey);

        jedis.zadd(zSetKey, 50000, "Alice");
        jedis.zadd(zSetKey, 75000, "Bob");
        jedis.zadd(zSetKey, 60000, "Charlie");
        jedis.zadd(zSetKey, 90000, "David");

        // Вывод по возрастанию
        List<String> asc = jedis.zrange(zSetKey, 0, -1);
        System.out.println("По возрастанию: " + asc);

        // Вывод по убыванию
        List<String> desc = jedis.zrevrange(zSetKey, 0, -1);
        System.out.println("По убыванию: " + desc);

        // Диапазон по баллам (от 60000 до 80000)
        List<String> range = jedis.zrangeByScore(zSetKey, 60000, 80000);
        System.out.println("Зарплата от 60k до 80k: " + range);

        // Получение ранга (0-based)
        Long rank = jedis.zrank(zSetKey, "Bob");
        System.out.println("Ранг Bob (0-based): " + rank);

        // Удаление элемента
        jedis.zrem(zSetKey, "Charlie");
        System.out.println("После удаления Charlie: " + jedis.zrange(zSetKey, 0, -1));

        // Операции между ZSet (пример пересечения и объединения)
        String zSet2 = "bonus_rank";
        jedis.zadd(zSet2, 10000, "Alice");
        jedis.zadd(zSet2, 15000, "Bob");
        jedis.zadd(zSet2, 5000, "David");
        jedis.zinterstore("temp_intersect", zSetKey, zSet2);
        System.out.println("Пересечение (сумма баллов): " + jedis.zrangeWithScores("temp_intersect", 0, -1));
    }
}
