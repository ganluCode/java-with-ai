package state;

/**
 * 状态模式增强特性演示类
 */
public class EnhancedStatePatternDemo {
    public static void main(String[] args) {
        System.out.println("=== 增强版状态模式演示 ===");
        
        System.out.println("\n1. 基于状态转换表的订单管理:");
        EnhancedOrder enhancedOrder = new EnhancedOrder("ENH2023001");
        enhancedOrder.showState();
        System.out.println("---");
        
        // 测试状态转换表管理的状态转换
        enhancedOrder.payOrder();     // 待支付 -> 已支付
        enhancedOrder.shipOrder();    // 已支付 -> 已发货
        enhancedOrder.deliverOrder(); // 已发货 -> 已收货
        enhancedOrder.returnOrder();  // 已收货 -> 退货申请中
        
        System.out.println("\n2. 基于枚举的简单状态管理:");
        MediaPlayerContext playerContext = new MediaPlayerContext();
        playerContext.showState();
        System.out.println("---");
        
        playerContext.play();   // 停止 -> 播放
        playerContext.pause();  // 播放 -> 暂停
        playerContext.play();   // 暂停 -> 播放
        playerContext.stop();   // 播放 -> 停止
    }
}