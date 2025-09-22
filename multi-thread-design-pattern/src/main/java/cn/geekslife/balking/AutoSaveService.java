package cn.geekslife.balking;

/**
 * 自动保存服务 - 演示Balking模式在实际应用中的使用
 */
public class AutoSaveService {
    // 数据对象
    private final Data data;
    // 自动保存线程
    private Thread autoSaveThread;
    // 是否正在运行
    private volatile boolean running = false;
    
    /**
     * 构造函数
     * @param data 数据对象
     */
    public AutoSaveService(Data data) {
        this.data = data;
    }
    
    /**
     * 启动自动保存服务 - Balking模式实现
     */
    public synchronized void start() {
        // Balking模式：如果服务已在运行，则放弃执行
        if (running) {
            System.out.println(Thread.currentThread().getName() + "：自动保存服务已在运行，放弃启动");
            return;
        }
        
        // 启动自动保存线程
        running = true;
        autoSaveThread = new Thread(this::doAutoSave, "AutoSaveThread");
        autoSaveThread.setDaemon(true);
        autoSaveThread.start();
        System.out.println(Thread.currentThread().getName() + "：自动保存服务已启动");
    }
    
    /**
     * 停止自动保存服务 - Balking模式实现
     */
    public synchronized void stop() {
        // Balking模式：如果服务未运行，则放弃执行
        if (!running) {
            System.out.println(Thread.currentThread().getName() + "：自动保存服务未运行，放弃停止");
            return;
        }
        
        // 停止自动保存线程
        running = false;
        if (autoSaveThread != null) {
            autoSaveThread.interrupt();
        }
        System.out.println(Thread.currentThread().getName() + "：自动保存服务已停止");
    }
    
    /**
     * 实际的自动保存操作
     */
    private void doAutoSave() {
        System.out.println("自动保存线程已启动");
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                // 每5秒自动保存一次
                Thread.sleep(5000);
                
                // 执行保存操作（Balking模式在save方法中实现）
                if (running) {
                    data.save();
                }
            } catch (InterruptedException e) {
                System.out.println("自动保存线程被中断");
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("自动保存线程已停止");
    }
    
    /**
     * 检查服务是否正在运行
     * @return 是否正在运行
     */
    public synchronized boolean isRunning() {
        return running;
    }
}