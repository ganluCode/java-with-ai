package cn.geekslife.volatiletest;

/**
 * volatile 防止指令重排序测试类
 * 
 * 该测试演示了 volatile 如何防止指令重排序：
 * 1. 没有 volatile 的情况下，可能会出现指令重排序
 * 2. 使用 volatile 可以防止这种重排序，保证代码执行顺序
 */
public class VolatileReorderingTest {
    
    // 非 volatile 变量
    private int a = 0;
    private boolean flag = false;
    
    // volatile 变量
    private volatile int volatileA = 0;
    private volatile boolean volatileFlag = false;
    
    // 测试结果计数器
    private int nonVolatileReorderCount = 0;
    private int volatileReorderCount = 0;
    
    // 用于增加重排序可能性的变量
    private int x = 0, y = 0;
    private volatile int volatileX = 0, volatileY = 0;
    
    /**
     * 写入线程 - 非 volatile 变量
     * 正常顺序应该是先设置 a=1，再设置 flag=true
     * 但由于指令重排序，可能先设置 flag=true，再设置 a=1
     */
    public void nonVolatileWrite() {
        a = 1;        // 操作1
        flag = true;  // 操作2
    }
    
    /**
     * 读取线程 - 非 volatile 变量
     * 可能会读到 flag=true 但 a=0 的情况，说明发生了重排序
     */
    public void nonVolatileRead() {
        if (flag) {   // 操作3
            if (a == 0) { // 操作4
                nonVolatileReorderCount++;
            }
        }
    }
    
    /**
     * 写入线程 - volatile 变量
     * volatile 会防止操作1和操作2之间的重排序
     */
    public void volatileWrite() {
        volatileA = 1;           // 操作1
        volatileFlag = true;     // 操作2
    }
    
    /**
     * 读取线程 - volatile 变量
     * 由于 volatile 的防止重排序特性，不会出现 flag=true 但 a=0 的情况
     */
    public void volatileRead() {
        if (volatileFlag) {      // 操作3
            if (volatileA == 0) { // 操作4
                volatileReorderCount++;
            }
        }
    }
    
    /**
     * 更复杂的重排序测试场景 - 非 volatile
     */
    public void complexNonVolatileWrite() {
        x = 1;  // 操作1
        y = 1;  // 操作2
    }
    
    public void complexNonVolatileRead() {
        if (y == 1) {  // 操作3
            if (x == 0) {  // 操作4 - 如果 x 还是 0，说明发生了重排序
                nonVolatileReorderCount++;
            }
        }
    }
    
    /**
     * 更复杂的重排序测试场景 - volatile
     */
    public void complexVolatileWrite() {
        volatileX = 1;  // 操作1
        volatileY = 1;  // 操作2
    }
    
    public void complexVolatileRead() {
        if (volatileY == 1) {  // 操作3
            if (volatileX == 0) {  // 操作4 - volatile 防止这种情况发生
                volatileReorderCount++;
            }
        }
    }
    
    // Getter 方法
    public int getNonVolatileReorderCount() {
        return nonVolatileReorderCount;
    }
    
    public int getVolatileReorderCount() {
        return volatileReorderCount;
    }
    
    // 重置计数器
    public void resetCounters() {
        nonVolatileReorderCount = 0;
        volatileReorderCount = 0;
    }
}