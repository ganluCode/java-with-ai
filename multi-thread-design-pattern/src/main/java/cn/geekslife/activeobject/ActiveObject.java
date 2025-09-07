package cn.geekslife.activeobject;

/**
 * Active Object接口 - Active Object模式的公共接口
 */
public interface ActiveObject {
    /**
     * 执行工作
     * @param name 工作名称
     * @return Future结果对象
     */
    FutureResult doWork(String name);
    
    /**
     * 关闭Active Object
     */
    void shutdown();
    
    /**
     * 获取队列大小
     * @return 队列大小
     */
    int getQueueSize();
}