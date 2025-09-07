package com.example.redis.lock;

/**
 * Redis分布式锁配置类
 */
public class LockConfig {
    private String lockKey;
    private long leaseTime; // 锁的过期时间（秒）
    private long renewalTime; // 续期时间间隔（秒）
    private long watchdogPeriod; // Watchdog监控周期（秒）
    private boolean fair; // 是否公平锁
    private boolean enablePubSub; // 是否启用发布/订阅机制
    private long subscriptionTimeout; // 订阅超时时间（毫秒）
    
    private LockConfig(Builder builder) {
        this.lockKey = builder.lockKey;
        this.leaseTime = builder.leaseTime;
        this.renewalTime = builder.renewalTime;
        this.watchdogPeriod = builder.watchdogPeriod;
        this.fair = builder.fair;
        this.enablePubSub = builder.enablePubSub;
        this.subscriptionTimeout = builder.subscriptionTimeout;
    }
    
    public String getLockKey() {
        return lockKey;
    }
    
    public long getLeaseTime() {
        return leaseTime;
    }
    
    public long getRenewalTime() {
        return renewalTime;
    }
    
    public long getWatchdogPeriod() {
        return watchdogPeriod;
    }
    
    public boolean isFair() {
        return fair;
    }
    
    public boolean isEnablePubSub() {
        return enablePubSub;
    }
    
    public long getSubscriptionTimeout() {
        return subscriptionTimeout;
    }
    
    /**
     * Builder模式构建LockConfig
     */
    public static class Builder {
    
        /**
         * 构建LockConfig实例
         * @return LockConfig实例
         */
        public LockConfig builder() {
            return new LockConfig(this);
        }
        private String lockKey = "defaultLock";
        private long leaseTime = 30; // 默认30秒
        private long renewalTime = 10; // 默认10秒
        private long watchdogPeriod = 5; // 默认Watchdog监控周期5秒
        private boolean fair = false; // 默认非公平锁
        private boolean enablePubSub = true; // 默认启用发布/订阅机制
        private long subscriptionTimeout = 5000; // 默认订阅超时时间5秒
        
        public Builder lockKey(String lockKey) {
            this.lockKey = lockKey;
            return this;
        }
        
        public Builder leaseTime(long leaseTime) {
            this.leaseTime = leaseTime;
            return this;
        }
        
        public Builder renewalTime(long renewalTime) {
            this.renewalTime = renewalTime;
            return this;
        }
        
        public Builder watchdogPeriod(long watchdogPeriod) {
            this.watchdogPeriod = watchdogPeriod;
            return this;
        }
        
        public Builder fair(boolean fair) {
            this.fair = fair;
            return this;
        }
        
        public Builder enablePubSub(boolean enablePubSub) {
            this.enablePubSub = enablePubSub;
            return this;
        }
        
        public Builder subscriptionTimeout(long subscriptionTimeout) {
            this.subscriptionTimeout = subscriptionTimeout;
            return this;
        }
        
        public LockConfig build() {
            return new LockConfig(this);
        }
    }
}