package cn.geekslife.balking;

/**
 * 单次使用工具类 - 演示Balking模式在一次性操作中的应用
 */
public class SingleUseTool {
    // 是否已使用标志
    private boolean used = false;
    
    /**
     * 使用工具 - Balking模式实现
     * 工具只能使用一次，重复使用时会放弃执行
     * @return 是否成功使用
     */
    public synchronized boolean use() {
        // Balking模式：如果工具已被使用，则放弃执行
        if (used) {
            System.out.println(Thread.currentThread().getName() + "：工具已被使用，放弃执行");
            return false;
        }
        
        // 执行使用操作
        doUse();
        // 标记为已使用
        used = true;
        System.out.println(Thread.currentThread().getName() + "：工具使用完成");
        return true;
    }
    
    /**
     * 实际的使用操作
     */
    private void doUse() {
        System.out.println(Thread.currentThread().getName() + "：正在使用工具...");
        try {
            // 模拟使用耗时
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(Thread.currentThread().getName() + "：工具使用中...");
    }
    
    /**
     * 检查工具是否已被使用
     * @return 是否已被使用
     */
    public synchronized boolean isUsed() {
        return used;
    }
}