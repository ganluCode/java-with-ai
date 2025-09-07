package cn.geekslife.future;

/**
 * Future数据类 - Future模式中的占位符实现
 */
public class FutureData implements Data {
    private RealData realData = null;
    private boolean ready = false;
    private final Object lock = new Object();
    
    /**
     * 设置真实数据
     * @param realData 真实数据
     */
    public void setRealData(RealData realData) {
        synchronized (lock) {
            if (ready) {
                return;
            }
            this.realData = realData;
            this.ready = true;
            lock.notifyAll();
        }
    }
    
    /**
     * 获取数据内容 - 如果数据未准备就绪则等待
     * @return 数据内容
     */
    @Override
    public String getContent() {
        synchronized (lock) {
            while (!ready) {
                try {
                    System.out.println("FutureData：数据未准备就绪，等待中...");
                    lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("FutureData：等待被中断");
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
            return realData.getContent();
        }
    }
    
    /**
     * 检查数据是否准备就绪
     * @return 是否准备就绪
     */
    @Override
    public boolean isReady() {
        synchronized (lock) {
            return ready;
        }
    }
}