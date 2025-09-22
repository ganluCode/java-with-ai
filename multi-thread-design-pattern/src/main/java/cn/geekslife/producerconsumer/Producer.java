package cn.geekslife.producerconsumer;

/**
 * 生产者类 - Producer-Consumer模式中的生产者
 */
public class Producer implements Runnable {
    private final String name;
    private final BoundedBuffer buffer;
    private final int itemCount;
    private volatile boolean running = true;
    
    /**
     * 构造函数
     * @param name 生产者名称
     * @param buffer 缓冲区
     * @param itemCount 生产数据项数量
     */
    public Producer(String name, BoundedBuffer buffer, int itemCount) {
        this.name = name;
        this.buffer = buffer;
        this.itemCount = itemCount;
    }
    
    /**
     * 生产者主逻辑
     */
    @Override
    public void run() {
        System.out.println(name + "：生产者启动");
        
        try {
            for (int i = 1; i <= itemCount && running; i++) {
                // 创建数据项
                Item item = new Item("数据-" + name + "-" + i, i);
                
                // 将数据项放入缓冲区
                buffer.put(item);
                
                // 模拟生产耗时
                Thread.sleep((long) (Math.random() * 100));
            }
            
            System.out.println(name + "：生产者完成，共生产 " + itemCount + " 个数据项");
        } catch (InterruptedException e) {
            System.out.println(name + "：生产者被中断");
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 停止生产者
     */
    public void stop() {
        running = false;
        System.out.println(name + "：收到停止信号");
    }
    
    /**
     * 获取生产者名称
     * @return 生产者名称
     */
    public String getName() {
        return name;
    }
}