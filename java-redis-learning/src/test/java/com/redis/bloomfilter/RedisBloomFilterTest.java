package com.redis.bloomfilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RedisBloomFilter测试类
 */
public class RedisBloomFilterTest {
    
    private RedisBloomFilter bloomFilter;
    
    @BeforeEach
    public void setUp() {
        // 创建布隆过滤器实例（使用Lettuce客户端）
        bloomFilter = new RedisBloomFilter("test_bloom_filter", 100000, 0.01, "localhost", 6379);
    }
    
    @Test
    public void testAddAndMightContain() {
        String item = "test_item";
        
        // 添加元素
        boolean added = bloomFilter.add(item);
        assertTrue(added, "元素应该被成功添加");
        
        // 查询元素
        boolean mightContain = bloomFilter.mightContain(item);
        assertTrue(mightContain, "元素应该可能存在于布隆过滤器中");
    }
    
    @Test
    public void testNonExistentItem() {
        String item = "non_existent_item";
        
        // 查询不存在的元素
        boolean mightContain = bloomFilter.mightContain(item);
        assertFalse(mightContain, "不存在的元素应该返回false");
    }
    
    @Test
    public void testBatchOperations() {
        String[] items = {"item1", "item2", "item3", "item4", "item5"};
        
        // 批量添加
        boolean[] addResults = bloomFilter.addAll(items);
        for (boolean result : addResults) {
            assertTrue(result, "所有元素都应该被成功添加");
        }
        
        // 批量查询
        boolean[] queryResults = bloomFilter.mightContainAll(items);
        for (boolean result : queryResults) {
            assertTrue(result, "所有元素都应该可能存在于布隆过滤器中");
        }
    }
    
    @Test
    public void testGetInfo() {
        // 获取布隆过滤器信息
        java.util.Map<String, Object> info = bloomFilter.getInfo();
        
        assertEquals("test_bloom_filter", info.get("name"));
        assertEquals(100000L, info.get("capacity"));
        assertEquals(0.01, (Double) info.get("errorRate"), 0.001);
        assertTrue((Long) info.get("bitmapSize") > 0);
        assertTrue((Integer) info.get("hashCount") > 0);
    }
    
    @Test
    public void testClear() {
        String item = "test_item";
        
        // 添加元素
        bloomFilter.add(item);
        
        // 清空布隆过滤器
        bloomFilter.clear();
        
        // 查询元素应该不存在
        boolean mightContain = bloomFilter.mightContain(item);
        assertFalse(mightContain, "清空后元素不应该存在");
    }
}