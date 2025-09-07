package cn.geekslife.activeobject;

/**
 * Future结果类 - Active Object模式中的异步结果载体
 */
public class FutureResult {
    private Result result = null;
    private boolean ready = false;
    private final Object lock = new Object();
    
    /**
     * 设置结果
     * @param result 结果
     */
    public void setResult(Result result) {
        synchronized (lock) {
            this.result = result;
            this.ready = true;
            lock.notifyAll();
        }
    }
    
    /**
     * 获取结果 - 如果结果未准备好则等待
     * @return 结果
     * @throws InterruptedException 如果线程被中断
     */
    public Result getResult() throws InterruptedException {
        synchronized (lock) {
            while (!ready) {
                lock.wait();
            }
            return result;
        }
    }
    
    /**
     * 检查结果是否准备好
     * @return 是否准备好
     */
    public boolean isReady() {
        synchronized (lock) {
            return ready;
        }
    }
    
    /**
     * 获取当前结果（不等待）
     * @return 当前结果，如果未准备好则返回null
     */
    public Result getCurrentResult() {
        synchronized (lock) {
            return result;
        }
    }
}