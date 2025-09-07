package cn.geekslife.singletonthreadedexecution;

/**
 * 线程不安全的银行账户类
 * 用于演示不使用Single Threaded Execution模式可能导致的问题
 */
public class UnsafeBankAccount {
    // 账户余额 - 共享资源
    private int balance;
    
    // 账户名称
    private final String accountName;
    
    // 构造函数
    public UnsafeBankAccount(String accountName, int initialBalance) {
        this.accountName = accountName;
        this.balance = initialBalance;
    }
    
    /**
     * 不安全的存款操作 - 没有同步保护
     * @param amount 存款金额
     */
    public void unsafeDeposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("存款金额必须大于0");
        }
        
        // 模拟一些处理时间 - 增加竞态条件发生的可能性
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 这里可能发生竞态条件
        balance += amount;
        System.out.println(Thread.currentThread().getName() + 
            " 向账户 " + accountName + " 存入 " + amount + " 元，余额: " + balance);
    }
    
    /**
     * 不安全的取款操作 - 没有同步保护
     * @param amount 取款金额
     * @throws IllegalStateException 当余额不足时抛出异常
     */
    public void unsafeWithdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("取款金额必须大于0");
        }
        
        // 模拟一些处理时间 - 增加竞态条件发生的可能性
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 这里可能发生竞态条件
        if (balance < amount) {
            throw new IllegalStateException("余额不足，当前余额: " + balance);
        }
        
        balance -= amount;
        System.out.println(Thread.currentThread().getName() + 
            " 从账户 " + accountName + " 取出 " + amount + " 元，余额: " + balance);
    }
    
    /**
     * 读取余额 - 没有同步保护
     * @return 当前余额
     */
    public int getBalance() {
        return balance;
    }
    
    /**
     * 获取账户名称
     * @return 账户名称
     */
    public String getAccountName() {
        return accountName;
    }
}