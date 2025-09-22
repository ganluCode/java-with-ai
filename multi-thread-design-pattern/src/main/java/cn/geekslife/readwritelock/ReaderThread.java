package cn.geekslife.readwritelock;

/**
 * 读者线程类 - 演示Read-Write Lock模式中的读操作
 */
public class ReaderThread extends Thread {
    private final Data data;
    private final int readCount;
    
    /**
     * 构造函数
     * @param name 线程名称
     * @param data 数据对象
     * @param readCount 读取次数
     */
    public ReaderThread(String name, Data data, int readCount) {
        super(name);
        this.data = data;
        this.readCount = readCount;
    }
    
    /**
     * 线程主逻辑
     */
    @Override
    public void run() {
        System.out.println(getName() + "：读者线程启动");
        
        try {
            for (int i = 0; i < readCount; i++) {
                // 读取数据
                String result = data.read();
                
                // 检查数据
                if (result != null && result.length() > 0) {
                    System.out.println(getName() + "：读取到数据长度 " + result.length());
                }
                
                // 随机间隔
                Thread.sleep((long) (Math.random() * 100));
            }
            
            System.out.println(getName() + "：读者线程完成");
        } catch (InterruptedException e) {
            System.out.println(getName() + "：读者线程被中断");
            interrupt();
        }
    }
}