package state;

/**
 * 订单状态抽象类
 */
public abstract class OrderState {
    protected Order order;
    
    public OrderState(Order order) {
        this.order = order;
    }
    
    // 支付订单
    public abstract void payOrder();
    
    // 发货
    public abstract void shipOrder();
    
    // 确认收货
    public abstract void deliverOrder();
    
    // 取消订单
    public abstract void cancelOrder();
    
    // 退货
    public abstract void returnOrder();
    
    // 获取状态名称
    public abstract String getStateName();
}