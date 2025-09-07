package cn.geekslife.twophase;

/**
 * 工作线程类 - 演示Two-Phase Termination模式的具体应用
 */
public class Worker extends GracefulThread {
    private int count = 0;
    
    /**
     * 执行具体工作
     * @throws InterruptedException 如果线程被中断
     */
    @Override
    protected void doWork() throws InterruptedException {
        count++;
        System.out.println(getName() + "：执行工作 #" + count);
        
        // 模拟耗时操作
        Thread.sleep(500);
        
        // 定期检查终止标志
        if (count % 10 == 0) {
            System.out.println(getName() + "：已完成 " + count + " 个工作单元");
        }
    }
    
    /**
     * 清理资源
     */
    @Override
    protected void cleanup() {
        super.cleanup();
        System.out.println(getName() + "：总共完成了 " + count + " 个工作单元");
        System.out.println(getName() + "：保存未完成的工作");
        System.out.println(getName() + "：关闭打开的资源");
    }
    
    /**
     * 获取已完成的工作单元数
     * @return 工作单元数
     */
    public int getCount() {
        return count;
    }
}