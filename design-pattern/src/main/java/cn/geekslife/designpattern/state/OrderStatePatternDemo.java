package cn.geekslife.designpattern.state;

/**
 * 订单状态模式演示类
 */
public class OrderStatePatternDemo {
    public static void main(String[] args) {
        System.out.println("=== 订单状态管理系统演示 ===");
        
        // 创建订单
        Order order = new Order("ORD2023001");
        order.showState();
        System.out.println("---");
        
        // 模拟订单流程
        order.payOrder();        // 待支付 -> 已支付
        order.shipOrder();       // 已支付 -> 已发货
        order.deliverOrder();    // 已发货 -> 已收货
        order.returnOrder();     // 已收货 -> 退货申请中
        
        System.out.println("\n=== 另一个订单流程演示 ===");
        
        // 创建另一个订单并取消
        Order order2 = new Order("ORD2023002");
        order2.showState();
        System.out.println("---");
        
        order2.cancelOrder();    // 待支付 -> 已取消
        order2.payOrder();       // 已取消 -> 无法支付
    }
}