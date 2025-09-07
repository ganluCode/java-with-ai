package cn.geekslife.balking;

/**
 * 带异常处理的Balking模式示例
 * 演示在Balking模式中如何处理异常情况
 */
public class ExceptionBalkingExample {
    // 状态标志
    private boolean active = false;
    private boolean error = false;
    private String errorMessage;
    
    /**
     * 激活操作 - Balking模式实现
     * @throws IllegalStateException 如果对象处于错误状态
     */
    public synchronized void activate() {
        // 如果对象处于错误状态，抛出异常
        if (error) {
            throw new IllegalStateException("对象处于错误状态: " + errorMessage);
        }
        
        // Balking模式：如果已激活，则放弃执行
        if (active) {
            System.out.println(Thread.currentThread().getName() + "：对象已激活，放弃执行");
            return;
        }
        
        // 执行激活操作
        doActivate();
        active = true;
        System.out.println(Thread.currentThread().getName() + "：对象激活完成");
    }
    
    /**
     * 实际的激活操作
     * @throws RuntimeException 模拟激活过程中可能发生的错误
     */
    private void doActivate() {
        System.out.println(Thread.currentThread().getName() + "：正在激活对象...");
        try {
            // 模拟激活耗时
            Thread.sleep(100);
            
            // 模拟可能的激活失败
            if (Math.random() < 0.2) { // 20%概率失败
                errorMessage = "激活过程中发生错误 - " + System.currentTimeMillis();
                error = true;
                throw new RuntimeException(errorMessage);
            }
            
            System.out.println(Thread.currentThread().getName() + "：对象激活中...");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            errorMessage = "激活被中断";
            error = true;
            throw new RuntimeException(errorMessage, e);
        }
    }
    
    /**
     * 执行操作 - Balking模式实现
     * @throws IllegalStateException 如果对象未激活或处于错误状态
     */
    public synchronized void execute() {
        // 如果对象处于错误状态，抛出异常
        if (error) {
            throw new IllegalStateException("对象处于错误状态: " + errorMessage);
        }
        
        // Balking模式：如果未激活，则放弃执行
        if (!active) {
            System.out.println(Thread.currentThread().getName() + "：对象未激活，放弃执行");
            return;
        }
        
        // 执行操作
        doExecute();
    }
    
    /**
     * 实际的操作执行
     */
    private void doExecute() {
        System.out.println(Thread.currentThread().getName() + "：正在执行操作...");
        try {
            // 模拟执行耗时
            Thread.sleep(50);
            System.out.println(Thread.currentThread().getName() + "：操作执行中...");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 检查是否已激活
     * @return 是否已激活
     */
    public synchronized boolean isActive() {
        return active;
    }
    
    /**
     * 检查是否处于错误状态
     * @return 是否处于错误状态
     */
    public synchronized boolean isError() {
        return error;
    }
    
    /**
     * 获取错误信息
     * @return 错误信息
     */
    public synchronized String getErrorMessage() {
        return errorMessage;
    }
}