package cn.geekslife.twophase;

/**
 * 数据保存线程类 - 演示Two-Phase Termination模式在数据持久化中的应用
 */
public class DataSaveThread extends GracefulThread {
    private final Object data;
    private final String filename;
    private boolean dataSaved = false;
    
    /**
     * 构造函数
     * @param data 数据
     * @param filename 文件名
     */
    public DataSaveThread(Object data, String filename) {
        this.data = data;
        this.filename = filename;
        setName("DataSaveThread");
    }
    
    /**
     * 执行具体工作
     * @throws InterruptedException 如果线程被中断
     */
    @Override
    protected void doWork() throws InterruptedException {
        System.out.println(getName() + "：开始保存数据到 " + filename);
        
        try {
            // 模拟数据保存过程
            for (int i = 0; i < 10; i++) {
                if (isShutdownRequested()) {
                    System.out.println(getName() + "：检测到终止请求，准备保存未完成数据");
                    break;
                }
                
                System.out.println(getName() + "：保存进度 " + (i + 1) + "/10");
                Thread.sleep(200);
            }
            
            // 保存数据
            saveData();
            dataSaved = true;
            
        } catch (Exception e) {
            System.err.println(getName() + "：数据保存异常: " + e.getMessage());
        }
        
        // 工作完成，退出循环
        throw new InterruptedException("工作完成");
    }
    
    /**
     * 保存数据
     */
    private void saveData() {
        System.out.println(getName() + "：正在保存数据...");
        try {
            // 模拟保存操作
            Thread.sleep(500);
            System.out.println(getName() + "：数据保存到 " + filename + " 完成");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 清理资源
     */
    @Override
    protected void cleanup() {
        super.cleanup();
        if (!dataSaved) {
            System.out.println(getName() + "：数据未完全保存，进行紧急保存");
            saveData();
        }
        System.out.println(getName() + "：数据保存线程清理完成");
    }
    
    /**
     * 检查数据是否已保存
     * @return 数据是否已保存
     */
    public boolean isDataSaved() {
        return dataSaved;
    }
}