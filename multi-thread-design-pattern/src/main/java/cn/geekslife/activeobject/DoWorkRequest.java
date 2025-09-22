package cn.geekslife.activeobject;

/**
 * 具体的工作请求类 - Active Object模式中的具体命令实现
 */
public class DoWorkRequest extends MethodRequest {
    private final Servant servant;
    private final String name;
    private final FutureResult future;
    private final String taskId;
    
    /**
     * 构造函数
     * @param servant 服务对象
     * @param name 工作名称
     * @param future Future结果对象
     * @param taskId 任务ID
     */
    public DoWorkRequest(Servant servant, String name, FutureResult future, String taskId) {
        this.servant = servant;
        this.name = name;
        this.future = future;
        this.taskId = taskId;
    }
    
    /**
     * 执行方法请求
     */
    @Override
    public void execute() {
        System.out.println("DoWorkRequest：执行任务 " + name + " (ID: " + taskId + ")");
        
        try {
            // 执行实际的工作
            Result result = servant.doWork(name, taskId);
            
            // 设置Future结果
            future.setResult(result);
            
            System.out.println("DoWorkRequest：任务完成 " + name + " (ID: " + taskId + ")");
        } catch (Exception e) {
            System.err.println("DoWorkRequest：任务执行异常 " + name + " (ID: " + taskId + "): " + e.getMessage());
            // 在实际应用中，可能需要设置异常结果
            future.setResult(new Result("ERROR: " + e.getMessage(), taskId));
        }
    }
    
    /**
     * 获取任务ID
     * @return 任务ID
     */
    public String getTaskId() {
        return taskId;
    }
}