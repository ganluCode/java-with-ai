package cn.geekslife.designpattern.state;

/**
 * 已支付状态
 */
public class PaidState extends OrderState {
    
    public PaidState(Order order) {
        super(order);
    }
    
    @Override
    public void payOrder() {
        System.out.println("订单已支付，无需重复支付");
    }
    
    @Override
    public void shipOrder() {
        System.out.println("订单已发货");
        order.setState(new ShippedState(order));
    }
    
    @Override
    public void deliverOrder() {
        System.out.println("订单未发货，无法确认收货");
    }
    
    @Override
    public void cancelOrder() {
        System.out.println("订单已支付，申请取消退款");
        order.setState(new CancelledState(order));
    }
    
    @Override
    public void returnOrder() {
        System.out.println("订单未发货，无法退货");
    }
    
    @Override
    public String getStateName() {
        return "已支付";
    }
}