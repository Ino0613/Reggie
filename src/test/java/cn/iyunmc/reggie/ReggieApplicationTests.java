package cn.iyunmc.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import java.util.Set;

@SpringBootTest
class ReggieApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testRedis() {

        //1.获取连接
        Jedis jedis = new Jedis("localhost",6379);

        //2.执行具体的操作
        jedis.set("username", "xiaoming");

        jedis.hset("myhash", "addr", "bj");

        String hget = jedis.hget("myhash", "addr");
        System.out.println(hget);

        Set<String> keys = jedis.keys("*");

        for (String key : keys) {
            System.out.println(key);
        }
        //3.关闭连接
        jedis.close();

    }

}
