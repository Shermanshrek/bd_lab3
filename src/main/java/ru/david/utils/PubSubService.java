package ru.david.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class PubSubService {
    private final Jedis jedis;
    private static final String CHANNEL = "notifications";

    public PubSubService(Jedis jedis) {
        this.jedis = jedis;
    }

    // Запуск подписчика в отдельном потоке
    public void startSubscriber() {
        new Thread(() -> {
            // Для подписки нужно отдельное соединение
            try (Jedis subJedis = new Jedis("localhost", 6379)) {
                subJedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        System.out.println("[SUBSCRIBER] Получено: " + message + " из канала " + channel);
                    }

                    @Override
                    public void onSubscribe(String channel, int subscribedChannels) {
                        System.out.println("Подписались на канал: " + channel);
                    }
                }, CHANNEL);
            }
        }).start();
    }

    // Публикация сообщения
    public void publish(String message) {
        jedis.publish(CHANNEL, message);
        System.out.println("[PUBLISHER] Отправлено: " + message);
    }

    // Демонстрация работы очереди через список (простая очередь)
    public void demoQueue() {
        String queueKey = "task_queue";
        System.out.println("\n=== Демонстрация очереди (список) ===");
        jedis.del(queueKey);

        // Producer: добавляем задачи
        jedis.rpush(queueKey, "task1", "task2", "task3");
        System.out.println("Очередь после добавления: " + jedis.lrange(queueKey, 0, -1));

        // Consumer: забираем задачи
        String task = jedis.lpop(queueKey);
        System.out.println("Обработана задача: " + task);
        System.out.println("Очередь после обработки: " + jedis.lrange(queueKey, 0, -1));
    }
}
