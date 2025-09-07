package cn.geekslife.activeobject;

/**
 * 服务对象类 - Active Object模式中的实际业务逻辑执行者
 */
public class Servant {
    private int workCount = 0;
    
    /**
     * 执行具体工作
     * @param name 工作名称
     * @param taskId 任务ID
     * @return 结果
     */
    public Result doWork(String name, String taskId) {
        System.out.println("Servant：开始执行工作 " + name + " (ID: " + taskId + ")");
        
        try {
            // 模拟耗时的工作
            long workTime = (long) (Math.random() * 1000) + 500;
            Thread.sleep(workTime);
            
            workCount++;
            String resultValue = "工作完成: " + name + " (执行时间: " + workTime + "ms, 序号: " + workCount + ")";
            Result result = new Result(resultValue, taskId);
            
            System.out.println("Servant：工作完成 " + name + " (ID: " + taskId + ")");
            return result;
        } catch (InterruptedException e) {
            System.out.println("Servant：工作被中断 " + name + " (ID: " + taskId + ")");
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 获取已完成的工作数量
     * @return 工作数量
     */
    public int getWorkCount() {
        return workCount;
    }
}