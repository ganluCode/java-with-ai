package cn.geekslife.readwritelock;

/**
 * 写者线程类 - 演示Read-Write Lock模式中的写操作
 */
public class WriterThread extends Thread {
    private final Data data;
    private final int writeCount;
    private final String prefix;
    
    /**
     * 构造函数
     * @param name 线程名称
     * @param data 数据对象
     * @param writeCount 写入次数
     * @param prefix 数据前缀
     */
    public WriterThread(String name, Data data, int writeCount, String prefix) {
        super(name);
        this.data = data;
        this.writeCount = writeCount;
        this.prefix = prefix;
    }
    
    /**
     * 线程主逻辑
     */
    @Override
    public void run() {
        System.out.println(getName() + "：写者线程启动");
        
        try {
            for (int i = 0; i < writeCount; i++) {
                // 写入数据
                String newData = prefix + "-" + System.currentTimeMillis() + "-" + i;
                data.write(newData);
                
                // 随机间隔
                Thread.sleep((long) (Math.random() * 200));
            }
            
            System.out.println(getName() + "：写者线程完成");
        } catch (InterruptedException e) {
            System.out.println(getName() + "：写者线程被中断");
            interrupt();
        }
    }
}