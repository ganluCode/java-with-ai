package com.example.redis.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis场景测试类
 * 测试Redis的各种数据类型操作和常用场景
 */
public class RedisScenarioTest {
    
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
            try {
                // 清空测试数据
                redisClient.getSyncCommands().flushdb();
            } catch (Exception e) {
                // 忽略清理错误
            }
            // 关闭连接
            redisClient.close();
        }
    }
    
    /**
     * 测试用户会话管理场景
     * 使用String类型存储用户会话信息
     */
    @Test
    public void testUserSessionManagement() {
        String sessionId = "session:12345";
        String userData = "{\"userId\":1001,\"username\":\"zhangsan\",\"loginTime\":\"2023-01-01T10:00:00\"}";
        
        // 设置会话数据，过期时间30分钟
        String setResult = redisClient.setex(sessionId, userData, 1800);
        assertEquals("OK", setResult, "设置会话数据应该成功");
        
        // 获取会话数据
        String retrievedData = redisClient.get(sessionId);
        assertEquals(userData, retrievedData, "获取的会话数据应该与设置的数据一致");
        
        // 检查会话是否存在
        Boolean exists = redisClient.exists(sessionId);
        assertTrue(exists, "会话应该存在");
        
        // 删除会话
        Long delResult = redisClient.del(sessionId);
        assertEquals(1L, delResult, "应该成功删除1个会话");
        
        // 删除后会话应该不存在
        Boolean existsAfterDel = redisClient.exists(sessionId);
        assertFalse(existsAfterDel, "删除后会话不应该存在");
    }
    
    /**
     * 测试用户信息存储场景
     * 使用Hash类型存储用户详细信息
     */
    @Test
    public void testUserInfoStorage() {
        String userKey = "user:1001";
        
        // 设置用户信息
        redisClient.hset(userKey, "name", "张三");
        redisClient.hset(userKey, "email", "zhangsan@example.com");
        redisClient.hset(userKey, "age", "25");
        redisClient.hset(userKey, "department", "技术部");
        
        // 获取单个用户信息字段
        String name = redisClient.hget(userKey, "name");
        assertEquals("张三", name, "用户名应该正确");
        
        // 获取所有用户信息
        Map<String, String> userInfo = redisClient.hgetall(userKey);
        assertEquals(4, userInfo.size(), "用户信息应该包含4个字段");
        assertEquals("zhangsan@example.com", userInfo.get("email"), "用户邮箱应该正确");
        
        // 更新用户信息
        Boolean updateResult = redisClient.hset(userKey, "age", "26");
        assertFalse(updateResult, "更新已存在的字段应该返回false");
        
        // 删除用户信息字段
        Long hdelResult = redisClient.hdel(userKey, "department");
        assertEquals(1L, hdelResult, "应该成功删除1个字段");
        
        // 删除后字段应该不存在
        String department = redisClient.hget(userKey, "department");
        assertNull(department, "删除后字段值应该为null");
    }
    
    /**
     * 测试消息队列场景
     * 使用List类型实现简单的消息队列
     */
    @Test
    public void testMessageQueue() {
        String queueKey = "message:queue:notifications";
        
        // 向队列添加消息
        Long rpushResult1 = redisClient.rpush(queueKey, "{\"type\":\"email\",\"to\":\"user1@example.com\",\"content\":\"Welcome!\"}");
        assertEquals(1L, rpushResult1, "队列长度应该为1");
        
        Long rpushResult2 = redisClient.rpush(queueKey, "{\"type\":\"sms\",\"to\":\"13800138000\",\"content\":\"Verification code: 1234\"}");
        assertEquals(2L, rpushResult2, "队列长度应该为2");
        
        Long rpushResult3 = redisClient.rpush(queueKey, "{\"type\":\"push\",\"to\":\"device123\",\"content\":\"New message\"}");
        assertEquals(3L, rpushResult3, "队列长度应该为3");
        
        // 查看队列中的所有消息
        List<String> messages = redisClient.lrange(queueKey, 0, -1);
        assertEquals(3, messages.size(), "队列应该包含3条消息");
        
        // 从队列左侧消费消息（FIFO）
        String message1 = redisClient.lpop(queueKey);
        assertNotNull(message1, "应该能从队列中取出消息");
        assertTrue(message1.contains("email"), "第一条消息应该是邮件通知");
        
        // 从队列右侧消费消息（LIFO）
        String message2 = redisClient.rpop(queueKey);
        assertNotNull(message2, "应该能从队列中取出消息");
        assertTrue(message2.contains("push"), "最后一条消息应该是推送通知");
        
        // 检查队列剩余消息
        List<String> remainingMessages = redisClient.lrange(queueKey, 0, -1);
        assertEquals(1, remainingMessages.size(), "队列应该还剩1条消息");
    }
    
    /**
     * 测试标签系统场景
     * 使用Set类型管理文章标签
     */
    @Test
    public void testTagSystem() {
        String articleTagsKey = "article:tags:1001";
        
        // 为文章添加标签
        Long saddResult = redisClient.sadd(articleTagsKey, "Java", "Redis", "数据库", "缓存", "技术");
        assertEquals(5L, saddResult, "应该成功添加5个标签");
        
        // 获取文章所有标签
        Set<String> tags = redisClient.smembers(articleTagsKey);
        assertEquals(5, tags.size(), "文章应该有5个标签");
        assertTrue(tags.contains("Java"), "应该包含Java标签");
        assertTrue(tags.contains("Redis"), "应该包含Redis标签");
        
        // 检查特定标签是否存在
        Boolean hasTag = redisClient.sismember(articleTagsKey, "数据库");
        assertTrue(hasTag, "应该包含数据库标签");
        
        Boolean hasNonexistentTag = redisClient.sismember(articleTagsKey, "Python");
        assertFalse(hasNonexistentTag, "不应该包含Python标签");
        
        // 移除标签
        Long sremResult = redisClient.srem(articleTagsKey, "技术");
        assertEquals(1L, sremResult, "应该成功移除1个标签");
        
        // 移除后标签数量应该减少
        Set<String> tagsAfterRemoval = redisClient.smembers(articleTagsKey);
        assertEquals(4, tagsAfterRemoval.size(), "移除后应该还剩4个标签");
    }
    
    /**
     * 测试排行榜场景
     * 使用Sorted Set类型实现文章排行榜
     */
    @Test
    public void testLeaderboard() {
        String leaderboardKey = "leaderboard:articles";
        
        // 添加文章及其得分
        redisClient.getSyncCommands().zadd(leaderboardKey, 100, "文章1: Redis入门指南");
        redisClient.getSyncCommands().zadd(leaderboardKey, 85, "文章2: Java并发编程");
        redisClient.getSyncCommands().zadd(leaderboardKey, 120, "文章3: 数据库优化技巧");
        redisClient.getSyncCommands().zadd(leaderboardKey, 95, "文章4: 微服务架构设计");
        redisClient.getSyncCommands().zadd(leaderboardKey, 110, "文章5: Docker容器化部署");
        
        // 获取排行榜前3名
        List<String> top3 = redisClient.getSyncCommands().zrevrange(leaderboardKey, 0, 2);
        assertEquals(3, top3.size(), "排行榜应该返回3篇文章");
        assertEquals("文章3: 数据库优化技巧", top3.get(0), "第一名应该是文章3");
        assertEquals("文章5: Docker容器化部署", top3.get(1), "第二名应该是文章5");
        assertEquals("文章1: Redis入门指南", top3.get(2), "第三名应该是文章1");
        
        // 获取指定文章的排名
        Long rank = redisClient.getSyncCommands().zrevrank(leaderboardKey, "文章2: Java并发编程");
        assertEquals(Long.valueOf(4), rank, "文章2应该是第5名（索引4）");
        
        // 获取指定文章的得分
        Double score = redisClient.getSyncCommands().zscore(leaderboardKey, "文章4: 微服务架构设计");
        assertEquals(Double.valueOf(95.0), score, 0.01, "文章4的得分应该是95");
        
        // 增加文章得分
        Double incrResult = redisClient.getSyncCommands().zincrby(leaderboardKey, 20, "文章2: Java并发编程");
        assertEquals(Double.valueOf(105.0), incrResult, 0.01, "增加得分后应该是105");
        
        // 检查更新后的排名（更新后文章2得分105，应该排在文章5之后，文章1之前）
        Long newRank = redisClient.getSyncCommands().zrevrank(leaderboardKey, "文章2: Java并发编程");
        assertEquals(Long.valueOf(2), newRank, "更新得分后文章2应该是第3名（索引2）");
    }
    
    /**
     * 测试计数器场景
     * 使用String类型的原子操作实现计数器
     */
    @Test
    public void testCounter() {
        String pageViewKey = "pageview:article:1001";
        String likeKey = "like:article:1001";
        
        // 初始化计数器
        redisClient.set(pageViewKey, "0");
        redisClient.set(likeKey, "0");
        
        // 增加页面浏览量
        Long incrResult1 = redisClient.getSyncCommands().incr(pageViewKey);
        assertEquals(Long.valueOf(1), incrResult1, "页面浏览量应该是1");
        
        Long incrResult2 = redisClient.getSyncCommands().incrby(pageViewKey, 5);
        assertEquals(Long.valueOf(6), incrResult2, "增加5后页面浏览量应该是6");
        
        // 增加点赞数
        Long incrResult3 = redisClient.getSyncCommands().incr(likeKey);
        assertEquals(Long.valueOf(1), incrResult3, "点赞数应该是1");
        
        Long incrResult4 = redisClient.getSyncCommands().incrby(likeKey, 3);
        assertEquals(Long.valueOf(4), incrResult4, "增加3后点赞数应该是4");
        
        // 获取当前计数
        String pageViewCount = redisClient.get(pageViewKey);
        assertEquals("6", pageViewCount, "页面浏览量应该是6");
        
        String likeCount = redisClient.get(likeKey);
        assertEquals("4", likeCount, "点赞数应该是4");
    }
    
    /**
     * 测试分布式锁场景
     * 使用String类型的SETNX命令实现简单的分布式锁
     */
    @Test
    public void testDistributedLock() {
        String lockKey = "lock:resource:12345";
        String lockValue = "locked-by-thread-1";
        int lockTimeout = 30; // 30秒
        
        // 获取锁
        String setResult = redisClient.getSyncCommands().setex(lockKey, lockTimeout, lockValue);
        assertEquals("OK", setResult, "应该成功获取锁");
        
        // 检查锁是否存在
        String lockValueRetrieved = redisClient.get(lockKey);
        assertEquals(lockValue, lockValueRetrieved, "锁的值应该正确");
        
        // 尝试再次获取锁（应该失败，因为锁已存在）
        // 注意：在实际应用中，我们会使用SETNX命令，但这里为了演示目的使用普通SET
        String newLockValue = "locked-by-thread-2";
        String setResult2 = redisClient.getSyncCommands().setex(lockKey, lockTimeout, newLockValue);
        assertEquals("OK", setResult2, "应该可以更新锁的值");
        
        // 释放锁
        Long delResult = redisClient.del(lockKey);
        assertEquals(Long.valueOf(1), delResult, "应该成功释放锁");
        
        // 释放后锁应该不存在
        String lockAfterDel = redisClient.get(lockKey);
        assertNull(lockAfterDel, "释放后锁应该不存在");
    }
    
    /**
     * 测试缓存场景
     * 使用过期时间实现缓存功能
     */
    @Test
    public void testCache() {
        String cacheKey = "cache:user:profile:1001";
        String userProfile = "{\"id\":1001,\"name\":\"张三\",\"email\":\"zhangsan@example.com\",\"avatar\":\"avatar.jpg\"}";
        
        // 设置缓存，5秒后过期
        String setResult = redisClient.setex(cacheKey, userProfile, 5);
        assertEquals("OK", setResult, "设置缓存应该成功");
        
        // 获取缓存
        String cachedProfile = redisClient.get(cacheKey);
        assertEquals(userProfile, cachedProfile, "缓存的数据应该正确");
        
        // 检查缓存是否存在
        Boolean exists = redisClient.exists(cacheKey);
        assertTrue(exists, "缓存应该存在");
        
        // 等待缓存过期
        try {
            Thread.sleep(6000); // 等待6秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 过期后缓存应该不存在
        String expiredCache = redisClient.get(cacheKey);
        assertNull(expiredCache, "过期后缓存应该不存在");
        
        Boolean existsAfterExpire = redisClient.exists(cacheKey);
        assertFalse(existsAfterExpire, "过期后缓存不应该存在");
    }
}