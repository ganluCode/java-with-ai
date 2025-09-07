package cn.geekslife.producerconsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 生产者-消费者管理器 - 演示完整的Producer-Consumer模式应用
 */
public class ProducerConsumerManager {
    private final BoundedBuffer buffer;
    private final ExecutorService producerService;
    private final ExecutorService consumerService;
    
    /**
     * 构造函数
     * @param bufferSize 缓冲区大小
     * @param producerCount 生产者数量
     * @param consumerCount 消费者数量
     */
    public ProducerConsumerManager(int bufferSize, int producerCount, int consumerCount) {
        this.buffer = new BoundedBuffer(bufferSize);
        this.producerService = Executors.newFixedThreadPool(producerCount);
        this.consumerService = Executors.newFixedThreadPool(consumerCount);
    }
    
    /**
     * 启动生产者-消费者系统
     * @param itemsPerProducer 每个生产者生产的项目数
     * @param itemsPerConsumer 每个消费者消费的项目数
     */
    public void start(int itemsPerProducer, int itemsPerConsumer) {
        System.out.println("启动生产者-消费者系统");
        System.out.println("缓冲区大小: " + buffer.getCapacity());
        System.out.println("生产者数量: " + producerService.getClass().getSimpleName());
        System.out.println("消费者数量: " + consumerService.getClass().getSimpleName());
        
        // 启动生产者
        for (int i = 1; i <= producerService.getClass().getSimpleName().equals("ThreadPoolExecutor") ? 
             ((java.util.concurrent.ThreadPoolExecutor) producerService).getCorePoolSize() : 1; i++) {
            producerService.submit(new Producer("Producer-" + i, buffer, itemsPerProducer));
        }
        
        // 启动消费者
        for (int i = 1; i <= consumerService.getClass().getSimpleName().equals("ThreadPoolExecutor") ? 
             ((java.util.concurrent.ThreadPoolExecutor) consumerService).getCorePoolSize() : 1; i++) {
            consumerService.submit(new Consumer("Consumer-" + i, buffer, itemsPerConsumer));
        }
    }
    
    /**
     * 关闭生产者-消费者系统
     */
    public void shutdown() {
        System.out.println("关闭生产者-消费者系统");
        
        // 关闭生产者线程池
        producerService.shutdown();
        // 关闭消费者线程池
        consumerService.shutdown();
        
        try {
            // 等待最多10秒让现有任务完成
            if (!producerService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("生产者线程池关闭超时，强制关闭");
                producerService.shutdownNow();
            }
            
            if (!consumerService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("消费者线程池关闭超时，强制关闭");
                consumerService.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("关闭过程中被中断");
            producerService.shutdownNow();
            consumerService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("生产者-消费者系统已关闭");
        System.out.println("最终缓冲区大小: " + buffer.size());
    }
    
    /**
     * 获取缓冲区
     * @return 缓冲区
     */
    public BoundedBuffer getBuffer() {
        return buffer;
    }
}