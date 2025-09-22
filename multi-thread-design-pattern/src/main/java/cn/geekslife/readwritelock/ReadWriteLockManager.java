package cn.geekslife.readwritelock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 读写锁管理器 - 演示完整的Read-Write Lock模式应用
 */
public class ReadWriteLockManager {
    private final Data data;
    private final ExecutorService readerService;
    private final ExecutorService writerService;
    private final int readerCount;
    private final int writerCount;
    
    /**
     * 构造函数
     * @param initialData 初始数据
     * @param readerCount 读者线程数
     * @param writerCount 写者线程数
     */
    public ReadWriteLockManager(String initialData, int readerCount, int writerCount) {
        this.data = new Data(initialData);
        this.readerService = Executors.newFixedThreadPool(readerCount);
        this.writerService = Executors.newFixedThreadPool(writerCount);
        this.readerCount = readerCount;
        this.writerCount = writerCount;
    }
    
    /**
     * 启动读写锁系统
     * @param readCount 每个读者的读取次数
     * @param writeCount 每个写者的写入次数
     */
    public void start(int readCount, int writeCount) {
        System.out.println("启动读写锁系统");
        System.out.println("初始数据: " + data.snapshot());
        System.out.println("读者线程数: " + readerCount);
        System.out.println("写者线程数: " + writerCount);
        
        // 启动读者线程
        for (int i = 1; i <= readerCount; i++) {
            readerService.submit(new ReaderTask("Reader-" + i, readCount));
        }
        
        // 启动写者线程
        for (int i = 1; i <= writerCount; i++) {
            writerService.submit(new WriterTask("Writer-" + i, writeCount, "Data"));
        }
    }
    
    /**
     * 关闭读写锁系统
     */
    public void shutdown() {
        System.out.println("关闭读写锁系统");
        
        // 关闭线程池
        readerService.shutdown();
        writerService.shutdown();
        
        try {
            // 等待最多10秒让现有任务完成
            if (!readerService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("读者线程池关闭超时，强制关闭");
                readerService.shutdownNow();
            }
            
            if (!writerService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("写者线程池关闭超时，强制关闭");
                writerService.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.out.println("关闭过程中被中断");
            readerService.shutdownNow();
            writerService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("读写锁系统已关闭");
        System.out.println("最终数据: " + data.snapshot());
    }
    
    /**
     * 读者任务类
     */
    private class ReaderTask implements Runnable {
        private final String name;
        private final int readCount;
        
        public ReaderTask(String name, int readCount) {
            this.name = name;
            this.readCount = readCount;
        }
        
        @Override
        public void run() {
            System.out.println(name + "：读者任务启动");
            
            try {
                for (int i = 0; i < readCount; i++) {
                    // 读取数据
                    String result = data.read();
                    
                    // 随机间隔
                    Thread.sleep((long) (Math.random() * 50));
                }
                
                System.out.println(name + "：读者任务完成");
            } catch (InterruptedException e) {
                System.out.println(name + "：读者任务被中断");
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * 写者任务类
     */
    private class WriterTask implements Runnable {
        private final String name;
        private final int writeCount;
        private final String prefix;
        
        public WriterTask(String name, int writeCount, String prefix) {
            this.name = name;
            this.writeCount = writeCount;
            this.prefix = prefix;
        }
        
        @Override
        public void run() {
            System.out.println(name + "：写者任务启动");
            
            try {
                for (int i = 0; i < writeCount; i++) {
                    // 写入数据
                    String newData = prefix + "-" + System.currentTimeMillis() + "-" + i;
                    data.write(newData);
                    
                    // 随机间隔
                    Thread.sleep((long) (Math.random() * 100));
                }
                
                System.out.println(name + "：写者任务完成");
            } catch (InterruptedException e) {
                System.out.println(name + "：写者任务被中断");
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * 获取数据对象
     * @return 数据对象
     */
    public Data getData() {
        return data;
    }
}