package cn.geekslife.designpattern.memento;

/**
 * 备忘录类
 * 存储原发器对象的内部状态
 */
public class Memento {
    private String state;
    private long timestamp;
    private String description;
    
    /**
     * 构造函数
     * @param state 状态
     * @param description 状态描述
     */
    public Memento(String state, String description) {
        this.state = state;
        this.timestamp = System.currentTimeMillis();
        this.description = description;
    }
    
    /**
     * 获取状态
     * @return 状态
     */
    public String getState() {
        return state;
    }
    
    /**
     * 获取时间戳
     * @return 时间戳
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * 获取状态描述
     * @return 状态描述
     */
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return "Memento{" +
                "state='" + state + '\'' +
                ", timestamp=" + timestamp +
                ", description='" + description + '\'' +
                '}';
    }
}