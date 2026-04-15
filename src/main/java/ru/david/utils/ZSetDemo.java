package ru.david.utils;

import redis.clients.jedis.Jedis;

import java.util.List;

public class ZSetDemo {
    private final Jedis jedis;

    public ZSetDemo(Jedis jedis) {
        this.jedis = jedis;
    }

    public void demo() {
        String zsetKey = "salary_rank";
        System.out.println("=== Демонстрация упорядоченных множеств ===");
        jedis.del(zsetKey);

        jedis.zadd(zsetKey, 50000, "Alice");
        jedis.zadd(zsetKey, 75000, "Bob");
        jedis.zadd(zsetKey, 60000, "Charlie");
        jedis.zadd(zsetKey, 90000, "David");

        // Вывод по возрастанию
        List<String> asc = jedis.zrange(zsetKey, 0, -1);
        System.out.println("По возрастанию: " + asc);
        // Вывод по убыванию
        List<String> desc = jedis.zrevrange(zsetKey, 0, -1);
        System.out.println("По убыванию: " + desc);

        // Диапазон по баллам (от 60000 до 80000)
        List<String> range = jedis.zrangeByScore(zsetKey, 60000, 80000);
        System.out.println("Зарплата от 60k до 80k: " + range);

        // Получение ранга (0-based)
        Long rank = jedis.zrank(zsetKey, "Bob");
        System.out.println("Ранг Bob (0-based): " + rank);

        // Удаление элемента
        jedis.zrem(zsetKey, "Charlie");
        System.out.println("После удаления Charlie: " + jedis.zrange(zsetKey, 0, -1));

        // Операции между ZSet (пример пересечения и объединения)
        String zset2 = "bonus_rank";
        jedis.zadd(zset2, 10000, "Alice");
        jedis.zadd(zset2, 15000, "Bob");
        jedis.zadd(zset2, 5000, "David");
        jedis.zinterstore("temp_intersect", zsetKey, zset2);
        System.out.println("Пересечение (сумма баллов): " + jedis.zrangeWithScores("temp_intersect", 0, -1));
    }
}
