package cn.geekslife.threadspecific;

/**
 * 上下文类 - Thread-Specific Storage模式中的数据载体
 */
public class Context {
    private final String userId;
    private final String requestId;
    private final long startTime;
    private String sessionData;
    
    /**
     * 构造函数
     * @param userId 用户ID
     * @param requestId 请求ID
     */
    public Context(String userId, String requestId) {
        this.userId = userId;
        this.requestId = requestId;
        this.startTime = System.currentTimeMillis();
    }
    
    /**
     * 获取用户ID
     * @return 用户ID
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * 获取请求ID
     * @return 请求ID
     */
    public String getRequestId() {
        return requestId;
    }
    
    /**
     * 获取开始时间
     * @return 开始时间
     */
    public long getStartTime() {
        return startTime;
    }
    
    /**
     * 获取会话数据
     * @return 会话数据
     */
    public String getSessionData() {
        return sessionData;
    }
    
    /**
     * 设置会话数据
     * @param sessionData 会话数据
     */
    public void setSessionData(String sessionData) {
        this.sessionData = sessionData;
    }
    
    /**
     * 计算执行时间
     * @return 执行时间（毫秒）
     */
    public long getExecutionTime() {
        return System.currentTimeMillis() - startTime;
    }
    
    /**
     * 重写toString方法
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return "Context{" +
                "userId='" + userId + '\'' +
                ", requestId='" + requestId + '\'' +
                ", startTime=" + startTime +
                ", sessionData='" + sessionData + '\'' +
                ", executionTime=" + getExecutionTime() + "ms" +
                '}';
    }
}