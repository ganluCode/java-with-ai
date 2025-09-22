package cn.geekslife.producerconsumer;

/**
 * 数据项类 - Producer-Consumer模式中的数据载体
 */
public class Item {
    private final String data;
    private final long timestamp;
    private final int id;
    
    /**
     * 构造函数
     * @param data 数据内容
     * @param id 数据项ID
     */
    public Item(String data, int id) {
        this.data = data;
        this.id = id;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 获取数据内容
     * @return 数据内容
     */
    public String getData() {
        return data;
    }
    
    /**
     * 获取时间戳
     * @return 时间戳
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * 获取数据项ID
     * @return 数据项ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * 重写toString方法
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return "Item{id=" + id + ", data='" + data + "', timestamp=" + timestamp + "}";
    }
}