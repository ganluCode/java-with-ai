package com.example.redis.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * LettuceRedisClient测试类
 */
public class LettuceRedisClientTest {
    
    private LettuceRedisClient redisClient;
    
    @BeforeEach
    public void setUp() {
        redisClient = new LettuceRedisClient();
        // 确保连接成功
        assertTrue(redisClient.isConnected(), "Redis客户端应该成功连接");
    }
    
    @AfterEach
    public void tearDown() {
        if (redisClient != null) {
            // 清空测试数据
            redisClient.getSyncCommands().flushdb();
            // 关闭连接
            redisClient.close();
        }
    }
    
    @Test
    public void testStringOperations() {
        String key = "test:string:key";
        String value = "test-value";
        
        // 测试设置和获取字符串
        String setResult = redisClient.set(key, value);
        assertEquals("OK", setResult, "设置字符串应该返回OK");
        
        String getResult = redisClient.get(key);
        assertEquals(value, getResult, "获取的字符串值应该与设置的值一致");
        
        // 测试键是否存在
        Boolean exists = redisClient.exists(key);
        assertTrue(exists, "键应该存在");
        
        // 测试删除键
        Long delResult = redisClient.del(key);
        assertEquals(1L, delResult, "应该成功删除1个键");
        
        // 删除后键应该不存在
        Boolean existsAfterDel = redisClient.exists(key);
        assertFalse(existsAfterDel, "删除后键不应该存在");
    }
    
    @Test
    public void testHashOperations() {
        String key = "test:hash:key";
        String field1 = "field1";
        String field2 = "field2";
        String value1 = "value1";
        String value2 = "value2";
        
        // 测试设置哈希字段
        Boolean hsetResult1 = redisClient.hset(key, field1, value1);
        assertTrue(hsetResult1, "设置哈希字段应该成功");
        
        Boolean hsetResult2 = redisClient.hset(key, field2, value2);
        assertTrue(hsetResult2, "设置哈希字段应该成功");
        
        // 测试获取哈希字段值
        String hgetResult1 = redisClient.hget(key, field1);
        assertEquals(value1, hgetResult1, "获取的哈希字段值应该正确");
        
        String hgetResult2 = redisClient.hget(key, field2);
        assertEquals(value2, hgetResult2, "获取的哈希字段值应该正确");
        
        // 测试获取所有哈希字段
        Map<String, String> hgetAllResult = redisClient.hgetall(key);
        assertEquals(2, hgetAllResult.size(), "哈希应该包含2个字段");
        assertEquals(value1, hgetAllResult.get(field1), "哈希字段1的值应该正确");
        assertEquals(value2, hgetAllResult.get(field2), "哈希字段2的值应该正确");
        
        // 测试删除哈希字段
        Long hdelResult = redisClient.hdel(key, field1);
        assertEquals(1L, hdelResult, "应该成功删除1个哈希字段");
        
        // 删除后字段应该不存在
        String hgetAfterDel = redisClient.hget(key, field1);
        assertNull(hgetAfterDel, "删除后字段值应该为null");
    }
    
    @Test
    public void testListOperations() {
        String key = "test:list:key";
        String value1 = "value1";
        String value2 = "value2";
        String value3 = "value3";
        
        // 测试在列表左侧插入元素
        Long lpushResult1 = redisClient.lpush(key, value1);
        assertEquals(1L, lpushResult1, "列表长度应该为1");
        
        Long lpushResult2 = redisClient.lpush(key, value2);
        assertEquals(2L, lpushResult2, "列表长度应该为2");
        
        // 测试在列表右侧插入元素
        Long rpushResult = redisClient.rpush(key, value3);
        assertEquals(3L, rpushResult, "列表长度应该为3");
        
        // 测试获取列表范围
        List<String> lrangeResult = redisClient.lrange(key, 0, -1);
        assertEquals(3, lrangeResult.size(), "列表应该包含3个元素");
        assertEquals(value2, lrangeResult.get(0), "列表第一个元素应该正确");
        assertEquals(value1, lrangeResult.get(1), "列表第二个元素应该正确");
        assertEquals(value3, lrangeResult.get(2), "列表第三个元素应该正确");
        
        // 测试从列表左侧弹出元素
        String lpopResult = redisClient.lpop(key);
        assertEquals(value2, lpopResult, "弹出的元素应该正确");
        
        // 测试从列表右侧弹出元素
        String rpopResult = redisClient.rpop(key);
        assertEquals(value3, rpopResult, "弹出的元素应该正确");
    }
    
    @Test
    public void testSetOperations() {
        String key = "test:set:key";
        String member1 = "member1";
        String member2 = "member2";
        String member3 = "member3";
        
        // 测试向集合添加元素
        Long saddResult = redisClient.sadd(key, member1, member2, member3);
        assertEquals(3L, saddResult, "应该成功添加3个元素");
        
        // 测试获取集合所有元素
        Set<String> smembersResult = redisClient.smembers(key);
        assertEquals(3, smembersResult.size(), "集合应该包含3个元素");
        assertTrue(smembersResult.contains(member1), "集合应该包含member1");
        assertTrue(smembersResult.contains(member2), "集合应该包含member2");
        assertTrue(smembersResult.contains(member3), "集合应该包含member3");
        
        // 测试检查元素是否在集合中
        Boolean sismemberResult1 = redisClient.sismember(key, member1);
        assertTrue(sismemberResult1, "member1应该在集合中");
        
        Boolean sismemberResult2 = redisClient.sismember(key, "nonexistent");
        assertFalse(sismemberResult2, "不存在的元素不应该在集合中");
        
        // 测试从集合中移除元素
        Long sremResult = redisClient.srem(key, member1);
        assertEquals(1L, sremResult, "应该成功移除1个元素");
        
        // 移除后元素不应该在集合中
        Boolean sismemberAfterRem = redisClient.sismember(key, member1);
        assertFalse(sismemberAfterRem, "移除后member1不应该在集合中");
    }
    
    @Test
    public void testExpireOperation() {
        String key = "test:expire:key";
        String value = "test-value";
        
        // 设置键值
        redisClient.set(key, value);
        
        // 设置过期时间
        Boolean expireResult = redisClient.expire(key, 2); // 2秒过期
        assertTrue(expireResult, "设置过期时间应该成功");
        
        // 等待3秒
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 检查键是否已过期
        Boolean existsResult = redisClient.exists(key);
        assertFalse(existsResult, "键应该已过期");
    }
}