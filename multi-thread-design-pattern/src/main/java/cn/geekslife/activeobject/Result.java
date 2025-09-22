package cn.geekslife.activeobject;

/**
 * 结果类 - Active Object模式中的返回值载体
 */
public class Result {
    private final String value;
    private final long timestamp;
    private final String taskId;
    
    /**
     * 构造函数
     * @param value 结果值
     * @param taskId 任务ID
     */
    public Result(String value, String taskId) {
        this.value = value;
        this.taskId = taskId;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 获取结果值
     * @return 结果值
     */
    public String getValue() {
        return value;
    }
    
    /**
     * 获取时间戳
     * @return 时间戳
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * 获取任务ID
     * @return 任务ID
     */
    public String getTaskId() {
        return taskId;
    }
    
    /**
     * 重写toString方法
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return "Result{" +
                "value='" + value + '\'' +
                ", taskId='" + taskId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}