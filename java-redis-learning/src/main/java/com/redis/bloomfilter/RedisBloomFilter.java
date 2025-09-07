package com.redis.bloomfilter;

import io.lettuce.core.RedisClient;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于Redis的布隆过滤器实现
 */
public class RedisBloomFilter {
    
    private String name;
    private long capacity;
    private double errorRate;
    private long bitmapSize;
    private int hashCount;
    
    private LettuceRedisTemplate redisTemplate;
    
    // 默认构造函数
    public RedisBloomFilter() {
    }
    
    // 带参数的构造函数（使用Lettuce）
    public RedisBloomFilter(String name, long capacity, double errorRate, String host, int port) {
        this.name = name;
        this.capacity = capacity;
        this.errorRate = errorRate;
        this.redisTemplate = new LettuceRedisTemplate(host, port);
        init();
    }
    
    // 带参数的构造函数（直接使用LettuceRedisTemplate）
    public RedisBloomFilter(String name, long capacity, double errorRate, LettuceRedisTemplate redisTemplate) {
        this.name = name;
        this.capacity = capacity;
        this.errorRate = errorRate;
        this.redisTemplate = redisTemplate;
        init();
    }
    
    // 兼容Jedis的构造函数（已废弃）
    @Deprecated
    public RedisBloomFilter(String name, long capacity, double errorRate, redis.clients.jedis.JedisPool jedisPool) {
        this.name = name;
        this.capacity = capacity;
        this.errorRate = errorRate;
        // 为了兼容性，这里仍然使用Jedis，但建议使用Lettuce版本
        this.redisTemplate = new LettuceRedisTemplate("localhost", 6379); // 临时解决方案
        init();
    }
    
    // 兼容Jedis的构造函数（已废弃）
    @Deprecated
    public RedisBloomFilter(String name, long capacity, double errorRate, JedisRedisTemplate redisTemplate) {
        this.name = name;
        this.capacity = capacity;
        this.errorRate = errorRate;
        this.redisTemplate = new LettuceRedisTemplate("localhost", 6379); // 临时解决方案
        init();
    }
    
    /**
     * 初始化布隆过滤器
     */
    public void init() {
        // 计算布隆过滤器参数
        calculateParameters();
        
        // 初始化元数据
        initMetadata();
    }
    
    /**
     * 计算布隆过滤器参数
     */
    private void calculateParameters() {
        if (capacity <= 0 || errorRate <= 0 || errorRate >= 1) {
            throw new IllegalArgumentException("Capacity must be positive and error rate must be between 0 and 1");
        }
        
        // 计算位图大小 m = -(n * ln(p)) / (ln(2)^2)
        bitmapSize = (long) Math.ceil(-capacity * Math.log(errorRate) / (Math.log(2) * Math.log(2)));
        
        // 计算哈希函数个数 k = (m/n) * ln(2)
        hashCount = (int) Math.ceil(bitmapSize * Math.log(2) / capacity);
        
        // 确保hashCount至少为1
        if (hashCount < 1) {
            hashCount = 1;
        }
    }
    
    /**
     * 初始化元数据
     */
    private void initMetadata() {
        String metaKey = "bf:" + name + ":meta";
        
        // 检查是否已经初始化过
        if (redisTemplate.hasKey(metaKey)) {
            // 如果已存在，读取现有配置
            loadMetadata();
        } else {
            // 如果不存在，创建新的元数据
            Map<String, String> metadata = new HashMap<>();
            metadata.put("capacity", String.valueOf(capacity));
            metadata.put("error_rate", String.valueOf(errorRate));
            metadata.put("bitmap_size", String.valueOf(bitmapSize));
            metadata.put("hash_count", String.valueOf(hashCount));
            metadata.put("inserted_count", "0");
            metadata.put("version", "1");
            
            redisTemplate.opsForHashPutAll(metaKey, metadata);
        }
    }
    
    /**
     * 加载现有元数据
     */
    private void loadMetadata() {
        String metaKey = "bf:" + name + ":meta";
        
        capacity = Long.parseLong((String) redisTemplate.opsForHashGet(metaKey, "capacity"));
        errorRate = Double.parseDouble((String) redisTemplate.opsForHashGet(metaKey, "error_rate"));
        bitmapSize = Long.parseLong((String) redisTemplate.opsForHashGet(metaKey, "bitmap_size"));
        hashCount = Integer.parseInt((String) redisTemplate.opsForHashGet(metaKey, "hash_count"));
    }
    
    // getter和setter方法
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getCapacity() {
        return capacity;
    }
    
    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }
    
    public double getErrorRate() {
        return errorRate;
    }
    
    public void setErrorRate(double errorRate) {
        this.errorRate = errorRate;
    }
    
    public long getBitmapSize() {
        return bitmapSize;
    }
    
    public int getHashCount() {
        return hashCount;
    }
    
    public LettuceRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }
    
    public void setRedisTemplate(LettuceRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * 使用双重哈希法生成多个哈希值
     * @param item 要计算哈希值的元素
     * @return 哈希值数组
     */
    private long[] hash(String item) {
        long[] hashes = new long[hashCount];
        
        // 计算两个基础哈希值
        long hash1 = murmurHash(item, 0);
        long hash2 = murmurHash(item, hash1);
        
        // 使用双重哈希法生成多个哈希值
        for (int i = 0; i < hashCount; i++) {
            hashes[i] = (hash1 + i * hash2) % bitmapSize;
            // 确保哈希值为正数
            if (hashes[i] < 0) {
                hashes[i] = Math.abs(hashes[i]);
            }
        }
        
        return hashes;
    }
    
    /**
     * MurmurHash算法实现
     * @param data 数据
     * @param seed 种子
     * @return 哈希值
     */
    private long murmurHash(String data, long seed) {
        byte[] bytes = data.getBytes();
        return murmurHash64(bytes, bytes.length, seed);
    }
    
    /**
     * 64位MurmurHash算法实现
     * @param data 数据字节数组
     * @param length 数据长度
     * @param seed 种子
     * @return 哈希值
     */
    /**
     * 向布隆过滤器中添加元素
     * @param item 要添加的元素
     * @return 添加成功返回true
     */
    public boolean add(String item) {
        return addWithRetry(item, 3);
    }
    
    /**
     * 向布隆过滤器中添加元素（带重试机制）
     * @param item 要添加的元素
     * @param maxRetries 最大重试次数
     * @return 添加成功返回true
     */
    public boolean addWithRetry(String item, int maxRetries) {
        if (item == null) {
            return false;
        }
        
        for (int i = 0; i < maxRetries; i++) {
            try {
                // 计算多个哈希值
                long[] hashes = hash(item);
                
                String bitmapKey = "bf:" + name + ":bitmap";
                
                // 设置对应位置的bit为1
                for (long hashValue : hashes) {
                    redisTemplate.setBit(bitmapKey, hashValue, true);
                }
                
                // 更新已插入元素数量
                updateInsertedCount();
                
                return true;
            } catch (Exception e) {
                // 网络异常，等待后重试
                try {
                    Thread.sleep(100 * (i + 1)); // 指数退避
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        
        return false; // 重试次数用完仍失败
    }
    
    /**
     * 查询元素是否可能存在于布隆过滤器中
     * @param item 要查询的元素
     * @return 如果元素可能存在返回true，如果一定不存在返回false
     */
    public boolean mightContain(String item) {
        return mightContainWithRetry(item, 3);
    }
    
    /**
     * 查询元素是否可能存在于布隆过滤器中（带重试机制）
     * @param item 要查询的元素
     * @param maxRetries 最大重试次数
     * @return 如果元素可能存在返回true，如果一定不存在返回false
     */
    public boolean mightContainWithRetry(String item, int maxRetries) {
        if (item == null) {
            return false;
        }
        
        for (int i = 0; i < maxRetries; i++) {
            try {
                // 计算多个哈希值
                long[] hashes = hash(item);
                
                String bitmapKey = "bf:" + name + ":bitmap";
                
                // 检查所有位置的bit是否都为1
                for (long hashValue : hashes) {
                    if (!redisTemplate.getBit(bitmapKey, hashValue)) {
                        return false; // 只要有一个bit为0，则元素一定不存在
                    }
                }
                
                return true; // 所有bit都为1，则元素可能存在于集合中
            } catch (Exception e) {
                // 网络异常，等待后重试
                try {
                    Thread.sleep(100 * (i + 1)); // 指数退避
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        
        return false; // 重试次数用完仍失败
    }
    
    /**
     * 批量查询元素
     * @param items 要查询的元素列表
     * @return 查询结果列表
     */
    public boolean[] mightContainAll(String... items) {
        boolean[] results = new boolean[items.length];
        
        for (int i = 0; i < items.length; i++) {
            results[i] = mightContain(items[i]);
        }
        
        return results;
    }
    
    /**
     * 批量添加元素
     * @param items 要添加的元素列表
     * @return 添加结果列表
     */
    public boolean[] addAll(String... items) {
        boolean[] results = new boolean[items.length];
        
        for (int i = 0; i < items.length; i++) {
            results[i] = add(items[i]);
        }
        
        return results;
    }
    
    /**
     * 更新已插入元素数量
     */
    private void updateInsertedCount() {
        String metaKey = "bf:" + name + ":meta";
        String currentCountStr = (String) redisTemplate.opsForHashGet(metaKey, "inserted_count");
        long currentCount = 0;
        
        if (currentCountStr != null) {
            currentCount = Long.parseLong(currentCountStr);
        }
        
        redisTemplate.opsForHashPut(metaKey, "inserted_count", String.valueOf(currentCount + 1));
    }
    
    /**
     * 获取已插入元素数量
     * @return 已插入元素数量
     */
    public long getInsertedCount() {
        String metaKey = "bf:" + name + ":meta";
        String countStr = (String) redisTemplate.opsForHashGet(metaKey, "inserted_count");
        
        if (countStr != null) {
            return Long.parseLong(countStr);
        }
        
        return 0;
    }
    
    /**
     * 获取布隆过滤器信息
     * @return 包含过滤器信息的Map
     */
    public Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<>();
        String metaKey = "bf:" + name + ":meta";
        
        info.put("name", name);
        info.put("capacity", capacity);
        info.put("errorRate", errorRate);
        info.put("bitmapSize", bitmapSize);
        info.put("hashCount", hashCount);
        info.put("insertedCount", getInsertedCount());
        
        String version = (String) redisTemplate.opsForHashGet(metaKey, "version");
        info.put("version", version != null ? version : "1");
        
        return info;
    }
    
    /**
     * 清空布隆过滤器
     */
    public void clear() {
        String bitmapKey = "bf:" + name + ":bitmap";
        String metaKey = "bf:" + name + ":meta";
        
        // 删除位图和元数据
        redisTemplate.delete(bitmapKey);
        redisTemplate.delete(metaKey);
        
        // 重新初始化
        init();
    }
    
    private long murmurHash64(byte[] data, int length, long seed) {
        final long m = 0xc6a4a7935bd1e995L;
        final int r = 47;
        
        long h = seed ^ (length * m);
        
        int i = 0;
        while (i + 8 <= length) {
            long k = ((long) data[i] & 0xff) |
                     (((long) data[i + 1] & 0xff) << 8) |
                     (((long) data[i + 2] & 0xff) << 16) |
                     (((long) data[i + 3] & 0xff) << 24) |
                     (((long) data[i + 4] & 0xff) << 32) |
                     (((long) data[i + 5] & 0xff) << 40) |
                     (((long) data[i + 6] & 0xff) << 48) |
                     (((long) data[i + 7] & 0xff) << 56);
            
            k *= m;
            k ^= k >>> r;
            k *= m;
            
            h ^= k;
            h *= m;
            
            i += 8;
        }
        
        switch (length - i) {
            case 7: h ^= (long) (data[i + 6] & 0xff) << 48;
            case 6: h ^= (long) (data[i + 5] & 0xff) << 40;
            case 5: h ^= (long) (data[i + 4] & 0xff) << 32;
            case 4: h ^= (long) (data[i + 3] & 0xff) << 24;
            case 3: h ^= (long) (data[i + 2] & 0xff) << 16;
            case 2: h ^= (long) (data[i + 1] & 0xff) << 8;
            case 1: h ^= (long) (data[i] & 0xff);
                    h *= m;
        }
        
        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;
        
        return h;
    }
}