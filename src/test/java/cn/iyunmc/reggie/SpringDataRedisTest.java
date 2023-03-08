package cn.iyunmc.reggie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 操作String类型数据
     */
    @Test
    public void testString(){
        redisTemplate.opsForValue().set("city333","beijing");

        String value = (String) redisTemplate.opsForValue().get("city333");
        System.out.println(value);

        redisTemplate.opsForValue().set("keyTime","value1",10L, TimeUnit.SECONDS);

        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("city333", "nanjing");
        System.out.println(aBoolean);

    }

    /**
     * 测试Hash类型数据
     */
    @Test
    public void testHash () {
        HashOperations operations = redisTemplate.opsForHash();
        operations.put("002", "name", "xiaoming");
        operations.put("002", "age", "20");

        Set keys = operations.keys("002");
        for (Object key : keys) {
            System.out.println(key);
        }

        List values = operations.values("002");
        for (Object value : values) {
            System.out.println(value);
        }
    }

    /**
     * 操作List类型的数据
     */
    @Test
    public void testList() {
        ListOperations listOperations = redisTemplate.opsForList();

        //存值
        listOperations.leftPush("mylist3", "a");
        listOperations.leftPushAll("mylist3", "b", "c", "d");

        //取值
        List<String> mylist = listOperations.range("mylist3", 0, -1);

        for (String s : mylist) {
            System.out.println(s);
        }

        //获得列表长度 llen
        Long size = listOperations.size("mylist3");
        int intValue = size.intValue();
        for (int i = 0; i < intValue; i++) {
            //出队列
            Object pop = listOperations.rightPop("mylist3");
            System.out.println(pop);
        }
    }

    /**
     * 操作Set类型的数据
     */
    @Test
    public void testSet() {
        SetOperations setOperations = redisTemplate.opsForSet();

        //存值
        setOperations.add("myset1", "set1","set2","set3","set4");

        //取值
        Set myset1 = setOperations.members("myset1");
        for (Object o : myset1) {
            System.out.println(o);
        }

        //删除成员
        setOperations.remove("myset1", "set3");

        Set myset11 = setOperations.members("myset1");
        for (Object s : myset11) {
            System.out.println(s);
        }
    }

    /**
     * 操作ZSet类型的数据
     */
    @Test
    public void testZset() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("myZSet1", "a", 80);
        zSetOperations.add("myZSet1", "b", 23);
        zSetOperations.add("myZset1", "a", 23);
        zSetOperations.add("myZset1", "c", 23);
        //取值
        Set<String> myZSet1 = zSetOperations.range("myZSet1", 0, -1);
        for (String s : myZSet1) {
            System.out.println(s);
        }

        //修改分数
        zSetOperations.incrementScore("myZSet1","b",67);

        //删除成员
        zSetOperations.remove("myZSet1","c");

        myZSet1 = zSetOperations.range("myZSet1", 0, -1);
        for (String s : myZSet1) {
            System.out.println(s);
        }
    }

    /**
     * 通用操作，针对不同的数据类型都可以操作
     */
    @Test
    public void testCommon() {
        //获取Redis中所有的key
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

        //判断某个key是否存在
        Boolean ino = redisTemplate.hasKey("Ino");
        System.out.println(ino);

        //删除指定key
        redisTemplate.delete("name");

        //获取指定key对应的value的数据类型
        DataType myset = redisTemplate.type("myset");
        System.out.println(myset.name());
    }

}
