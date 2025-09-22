package cn.geekslife.threadpermessage;

/**
 * 消息类 - Thread-Per-Message模式中的数据载体
 */
public class Message {
    private final String content;
    private final long timestamp;
    private final String messageId;
    
    /**
     * 构造函数
     * @param content 消息内容
     * @param messageId 消息ID
     */
    public Message(String content, String messageId) {
        this.content = content;
        this.messageId = messageId;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 处理消息 - 模拟业务逻辑处理
     */
    public void process() {
        System.out.println(Thread.currentThread().getName() + "：开始处理消息 [" + messageId + "] - " + content);
        
        try {
            // 模拟处理耗时
            long processingTime = (long) (Math.random() * 1000) + 100;
            Thread.sleep(processingTime);
            
            System.out.println(Thread.currentThread().getName() + "：消息 [" + messageId + "] 处理完成，耗时 " + processingTime + "ms");
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "：消息 [" + messageId + "] 处理被中断");
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 获取消息内容
     * @return 消息内容
     */
    public String getContent() {
        return content;
    }
    
    /**
     * 获取时间戳
     * @return 时间戳
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * 获取消息ID
     * @return 消息ID
     */
    public String getMessageId() {
        return messageId;
    }
    
    /**
     * 重写toString方法
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return "Message{id='" + messageId + "', content='" + content + "', timestamp=" + timestamp + "}";
    }
}