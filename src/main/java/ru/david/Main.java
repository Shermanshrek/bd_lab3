package ru.david;

import redis.clients.jedis.Jedis;
import ru.david.dao.DepartmentDao;
import ru.david.dao.EmployeeDao;
import ru.david.dao.ProjectDao;
import ru.david.dao.hash.DepartmentHashDao;
import ru.david.entity.Department;
import ru.david.entity.Employee;
import ru.david.entity.Project;
import ru.david.utils.ListDemo;
import ru.david.utils.PubSubService;
import ru.david.utils.SetDemo;
import ru.david.utils.ZSetDemo;

import java.math.BigDecimal;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            System.out.println("Connected to Redis");
            // 1. CRUD с шаблоном "entity:id:field"
            System.out.println("\n=== 1. CRUD с шаблоном ===");
            DepartmentDao deptDao = new DepartmentDao(jedis);
            EmployeeDao empDao = new EmployeeDao(jedis);
            ProjectDao projDao = new ProjectDao(jedis);

            Department it = new Department(1L, "IT");
            deptDao.save(it);
            Employee emp = new Employee(101L, "Иванов Иван", new BigDecimal("120000"), it, null, null);
            empDao.save(emp);
            Project proj = new Project(10L, "CRM", new BigDecimal("500000"), it, emp);
            projDao.save(proj);

            Set<String> allKeys = jedis.keys("*");
            System.out.println("All keys: " + allKeys);

            System.out.println("Сотрудник: " + empDao.findById(101L).getFio());
            System.out.println("Проект: " + projDao.findById(10L).getProjectName());

            // 2. Списки
            System.out.println("\n=== 2. Списки ===");
            new ListDemo(jedis).demo();

            // 3. Множества
            System.out.println("\n=== 3. Множества ===");
            new SetDemo(jedis).demo();

            // 4. Упорядоченные множества
            System.out.println("\n=== 4. Упорядоченные множества ===");
            new ZSetDemo(jedis).demo();

            // 5. Хэш-таблицы (альтернативный DAO)
            System.out.println("\n=== 5. Хэш-таблицы ===");
            DepartmentHashDao deptHashDao = new DepartmentHashDao(jedis);
            Department hr = new Department(2L, "HR");
            deptHashDao.save(hr);
            System.out.println("Найден отдел через хэш: " + deptHashDao.findById(2L).getDepartmentName());

            // 6. Pub/Sub и очередь
            PubSubService pubSub = new PubSubService(jedis);
            pubSub.startSubscriber();
            Thread.sleep(500); // даём время подписчику подключиться
            pubSub.publish("Тестовое уведомление");
            pubSub.demoQueue();

            Thread.sleep(1000); // даём время на вывод подписчика
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}