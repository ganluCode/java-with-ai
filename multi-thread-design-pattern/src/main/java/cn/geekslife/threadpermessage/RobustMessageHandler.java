package cn.geekslife.threadpermessage;

/**
 * 消息处理器类 - 演示带有异常处理的Thread-Per-Message模式
 */
public class RobustMessageHandler {
    
    /**
     * 处理消息 - 包含异常处理
     * @param message 消息
     */
    public void handleMessage(Message message) {
        System.out.println("RobustMessageHandler：接收到消息 " + message);
        
        Thread thread = new Thread(() -> {
            try {
                message.process();
            } catch (Exception e) {
                System.err.println("RobustMessageHandler：处理消息时发生错误: " + e.getMessage());
                e.printStackTrace();
            } finally {
                System.out.println("RobustMessageHandler：消息 " + message.getMessageId() + " 处理线程结束");
            }
        }, "RobustMessageThread-" + message.getMessageId());
        
        // 设置为守护线程，确保JVM可以正常退出
        thread.setDaemon(false);
        
        // 启动线程
        thread.start();
    }
    
    /**
     * 处理可能失败的消息
     * @param message 消息
     */
    public void handleUnreliableMessage(Message message) {
        System.out.println("RobustMessageHandler：接收到不可靠消息 " + message);
        
        Thread thread = new Thread(() -> {
            try {
                // 模拟可能失败的处理
                if (Math.random() < 0.3) { // 30%概率失败
                    throw new RuntimeException("模拟处理失败");
                }
                
                message.process();
            } catch (Exception e) {
                System.err.println("RobustMessageHandler：处理不可靠消息时发生错误: " + e.getMessage());
                // 可以实现重试逻辑或其他错误处理策略
                handleErrorMessage(message, e);
            } finally {
                System.out.println("RobustMessageHandler：不可靠消息 " + message.getMessageId() + " 处理线程结束");
            }
        }, "UnreliableMessageThread-" + message.getMessageId());
        
        thread.start();
    }
    
    /**
     * 处理错误消息
     * @param message 消息
     * @param error 错误
     */
    private void handleErrorMessage(Message message, Exception error) {
        System.out.println("RobustMessageHandler：记录错误消息 " + message.getMessageId() + ": " + error.getMessage());
        // 这里可以实现错误日志记录、消息重试等逻辑
    }
}