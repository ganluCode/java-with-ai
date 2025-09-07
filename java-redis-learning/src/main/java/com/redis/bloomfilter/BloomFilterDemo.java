package com.redis.bloomfilter;

/**
 * Redis布隆过滤器演示类
 */
public class BloomFilterDemo {
    
    public static void main(String[] args) {
        try {
            // 创建布隆过滤器实例（使用Lettuce客户端）
            RedisBloomFilter bloomFilter = new RedisBloomFilter("demo_filter", 1000000, 0.01, "localhost", 6379);
            
            System.out.println("=== Redis布隆过滤器演示 ===");
            
            // 显示布隆过滤器信息
            System.out.println("布隆过滤器信息:");
            java.util.Map<String, Object> info = bloomFilter.getInfo();
            for (java.util.Map.Entry<String, Object> entry : info.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
            
            // 添加一些元素
            String[] itemsToAdd = {"user_1001", "user_1002", "user_1003", "product_2001", "order_3001"};
            System.out.println("\n添加元素:");
            for (String item : itemsToAdd) {
                boolean added = bloomFilter.add(item);
                System.out.println("  " + item + " -> " + (added ? "添加成功" : "添加失败"));
            }
            
            // 查询已添加的元素
            System.out.println("\n查询已添加的元素:");
            for (String item : itemsToAdd) {
                boolean exists = bloomFilter.mightContain(item);
                System.out.println("  " + item + " -> " + (exists ? "可能存在" : "一定不存在"));
            }
            
            // 查询未添加的元素
            String[] itemsToCheck = {"user_9999", "product_9999", "order_9999"};
            System.out.println("\n查询未添加的元素:");
            for (String item : itemsToCheck) {
                boolean exists = bloomFilter.mightContain(item);
                System.out.println("  " + item + " -> " + (exists ? "可能存在" : "一定不存在"));
            }
            
            // 显示更新后的信息
            System.out.println("\n更新后的布隆过滤器信息:");
            java.util.Map<String, Object> updatedInfo = bloomFilter.getInfo();
            System.out.println("  已插入元素数量: " + updatedInfo.get("insertedCount"));
            
        } catch (Exception e) {
            System.err.println("发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}