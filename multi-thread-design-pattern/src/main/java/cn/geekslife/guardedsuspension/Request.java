package cn.geekslife.guardedsuspension;

/**
 * 请求类 - 用于Guarded Suspension模式演示
 */
public class Request {
    private final String name;
    private final long timestamp;
    
    /**
     * 构造函数
     * @param name 请求名称
     */
    public Request(String name) {
        this.name = name;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 获取请求名称
     * @return 请求名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取时间戳
     * @return 时间戳
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * 重写toString方法
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return "Request{name='" + name + "', timestamp=" + timestamp + "}";
    }
}