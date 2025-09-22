package cn.geekslife.producerconsumer;

/**
 * 消费者类 - Producer-Consumer模式中的消费者
 */
public class Consumer implements Runnable {
    private final String name;
    private final BoundedBuffer buffer;
    private final int consumeCount;
    private volatile boolean running = true;
    
    /**
     * 构造函数
     * @param name 消费者名称
     * @param buffer 缓冲区
     * @param consumeCount 消费数据项数量
     */
    public Consumer(String name, BoundedBuffer buffer, int consumeCount) {
        this.name = name;
        this.buffer = buffer;
        this.consumeCount = consumeCount;
    }
    
    /**
     * 消费者主逻辑
     */
    @Override
    public void run() {
        System.out.println(name + "：消费者启动");
        
        try {
            for (int i = 1; i <= consumeCount && running; i++) {
                // 从缓冲区取出数据项
                Item item = buffer.take();
                
                // 处理数据项
                processItem(item);
                
                // 模拟消费耗时
                Thread.sleep((long) (Math.random() * 150));
            }
            
            System.out.println(name + "：消费者完成，共消费 " + consumeCount + " 个数据项");
        } catch (InterruptedException e) {
            System.out.println(name + "：消费者被中断");
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 处理数据项
     * @param item 数据项
     */
    private void processItem(Item item) {
        System.out.println(name + "：处理数据项 " + item);
        // 模拟数据处理
        try {
            Thread.sleep((long) (Math.random() * 50));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 停止消费者
     */
    public void stop() {
        running = false;
        System.out.println(name + "：收到停止信号");
    }
    
    /**
     * 获取消费者名称
     * @return 消费者名称
     */
    public String getName() {
        return name;
    }
}