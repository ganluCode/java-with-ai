package cn.geekslife.designpattern.state;

/**
 * 待支付状态
 */
public class PendingPaymentState extends OrderState {
    
    public PendingPaymentState(Order order) {
        super(order);
    }
    
    @Override
    public void payOrder() {
        System.out.println("订单支付成功");
        order.setState(new PaidState(order));
    }
    
    @Override
    public void shipOrder() {
        System.out.println("订单未支付，无法发货");
    }
    
    @Override
    public void deliverOrder() {
        System.out.println("订单未支付，无法确认收货");
    }
    
    @Override
    public void cancelOrder() {
        System.out.println("订单已取消");
        order.setState(new CancelledState(order));
    }
    
    @Override
    public void returnOrder() {
        System.out.println("订单未支付，无法退货");
    }
    
    @Override
    public String getStateName() {
        return "待支付";
    }
}