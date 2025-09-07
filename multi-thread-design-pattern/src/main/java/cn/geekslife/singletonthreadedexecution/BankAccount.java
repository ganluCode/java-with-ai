package cn.geekslife.singletonthreadedexecution;

/**
 * 银行账户类 - 演示Single Threaded Execution模式
 * 该类演示了如何使用synchronized关键字保护共享资源
 */
public class BankAccount {
    // 账户余额 - 共享资源
    private int balance;
    
    // 账户名称
    private final String accountName;
    
    // 构造函数
    public BankAccount(String accountName, int initialBalance) {
        this.accountName = accountName;
        this.balance = initialBalance;
    }
    
    /**
     * 方法级同步 - 存款操作
     * 使用synchronized关键字确保同一时刻只有一个线程可以执行此方法
     * @param amount 存款金额
     */
    public synchronized void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("存款金额必须大于0");
        }
        
        // 模拟一些处理时间
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        balance += amount;
        System.out.println(Thread.currentThread().getName() + 
            " 向账户 " + accountName + " 存入 " + amount + " 元，余额: " + balance);
    }
    
    /**
     * 方法级同步 - 取款操作
     * @param amount 取款金额
     * @throws IllegalStateException 当余额不足时抛出异常
     */
    public synchronized void withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("取款金额必须大于0");
        }
        
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
    }
    
    /**
     * 代码块级同步 - 转账操作
     * 演示如何使用synchronized代码块来保护临界区
     * @param target 目标账户
     * @param amount 转账金额
     */
    public void transfer(BankAccount target, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("转账金额必须大于0");
        }
        
        // 使用固定顺序获取锁以避免死锁
        BankAccount firstLock = this.hashCode() < target.hashCode() ? this : target;
        BankAccount secondLock = this.hashCode() < target.hashCode() ? target : this;
        
        synchronized (firstLock) {
            synchronized (secondLock) {
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
            }
        }
    }
    
    /**
     * 读取余额 - 读操作也需要同步以保证可见性
     * @return 当前余额
     */
    public synchronized int getBalance() {
        return balance;
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
        return "BankAccount{" +
                "accountName='" + accountName + '\'' +
                ", balance=" + balance +
                '}';
    }
}