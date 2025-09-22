package cn.geekslife.singletonthreadedexecution;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用ReentrantLock实现的银行账户类
 * 演示如何使用Lock接口实现Single Threaded Execution模式
 */
public class BankAccountWithLock {
    // 账户余额 - 共享资源
    private int balance;
    
    // 账户名称
    private final String accountName;
    
    // 可重入锁
    private final ReentrantLock lock = new ReentrantLock();
    
    // 构造函数
    public BankAccountWithLock(String accountName, int initialBalance) {
        this.accountName = accountName;
        this.balance = initialBalance;
    }
    
    /**
     * 使用ReentrantLock实现存款操作
     * @param amount 存款金额
     */
    public void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("存款金额必须大于0");
        }
        
        // 获取锁
        lock.lock();
        try {
            // 模拟一些处理时间
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            balance += amount;
            System.out.println(Thread.currentThread().getName() + 
                " 向账户 " + accountName + " 存入 " + amount + " 元，余额: " + balance);
        } finally {
            // 确保锁被释放
            lock.unlock();
        }
    }
    
    /**
     * 使用ReentrantLock实现取款操作
     * @param amount 取款金额
     * @throws IllegalStateException 当余额不足时抛出异常
     */
    public void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("取款金额必须大于0");
        }
        
        // 获取锁
        lock.lock();
        try {
            if (balance < amount) {
                throw new IllegalStateException("余额不足，当前余额: " + balance);
            }
            
            // 模拟一些处理时间
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            balance -= amount;
            System.out.println(Thread.currentThread().getName() + 
                " 从账户 " + accountName + " 取出 " + amount + " 元，余额: " + balance);
        } finally {
            // 确保锁被释放
            lock.unlock();
        }
    }
    
    /**
     * 使用ReentrantLock实现转账操作
     * @param target 目标账户
     * @param amount 转账金额
     */
    public void transfer(BankAccountWithLock target, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("转账金额必须大于0");
        }
        
        // 使用固定顺序获取锁以避免死锁
        BankAccountWithLock firstLock = this.hashCode() < target.hashCode() ? this : target;
        BankAccountWithLock secondLock = this.hashCode() < target.hashCode() ? target : this;
        
        // 获取第一个锁
        firstLock.lock.lock();
        try {
            // 获取第二个锁
            secondLock.lock.lock();
            try {
                if (this.balance < amount) {
                    throw new IllegalStateException("余额不足，当前余额: " + balance);
                }
                
                // 模拟一些处理时间
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                this.balance -= amount;
                target.balance += amount;
                
                System.out.println(Thread.currentThread().getName() + 
                    " 从账户 " + this.accountName + " 向账户 " + target.accountName + 
                    " 转账 " + amount + " 元");
                System.out.println("账户 " + this.accountName + " 余额: " + this.balance);
                System.out.println("账户 " + target.accountName + " 余额: " + target.balance);
            } finally {
                // 释放第二个锁
                secondLock.lock.unlock();
            }
        } finally {
            // 释放第一个锁
            firstLock.lock.unlock();
        }
    }
    
    /**
     * 读取余额
     * @return 当前余额
     */
    public int getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 获取账户名称
     * @return 账户名称
     */
    public String getAccountName() {
        return accountName;
    }
    
    /**
     * 重写toString方法
     * @return 账户信息字符串
     */
    @Override
    public String toString() {
        return "BankAccountWithLock{" +
                "accountName='" + accountName + '\'' +
                ", balance=" + balance +
                '}';
    }
}