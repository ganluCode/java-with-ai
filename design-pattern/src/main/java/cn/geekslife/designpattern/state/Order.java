package cn.geekslife.designpattern.state;

/**
 * 订单类 - 环境类
 */
public class Order {
    protected OrderState state;
    protected String orderId;
    
    public Order(String orderId) {
        this.orderId = orderId;
        // 初始状态为待支付
        this.state = new PendingPaymentState(this);
    }
    
    // 设置状态
    public void setState(OrderState state) {
        this.state = state;
    }
    
    // 获取当前状态
    public OrderState getState() {
        return state;
    }
    
    // 支付订单
    public void payOrder() {
        System.out.println("订单 [" + orderId + "] 执行支付操作:");
        state.payOrder();
        System.out.println("当前状态: " + state.getStateName());
        System.out.println("---");
    }
    
    // 发货
    public void shipOrder() {
        System.out.println("订单 [" + orderId + "] 执行发货操作:");
        state.shipOrder();
        System.out.println("当前状态: " + state.getStateName());
        System.out.println("---");
    }
    
    // 确认收货
    public void deliverOrder() {
        System.out.println("订单 [" + orderId + "] 执行确认收货操作:");
        state.deliverOrder();
        System.out.println("当前状态: " + state.getStateName());
        System.out.println("---");
    }
    
    // 取消订单
    public void cancelOrder() {
        System.out.println("订单 [" + orderId + "] 执行取消操作:");
        state.cancelOrder();
        System.out.println("当前状态: " + state.getStateName());
        System.out.println("---");
    }
    
    // 退货
    public void returnOrder() {
        System.out.println("订单 [" + orderId + "] 执行退货操作:");
        state.returnOrder();
        System.out.println("当前状态: " + state.getStateName());
        System.out.println("---");
    }
    
    // 显示当前状态
    public void showState() {
        System.out.println("订单 [" + orderId + "] 当前状态: " + state.getStateName());
    }
}