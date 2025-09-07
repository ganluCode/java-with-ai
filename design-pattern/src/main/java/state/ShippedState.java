package state;

/**
 * 已发货状态
 */
public class ShippedState extends OrderState {
    
    public ShippedState(Order order) {
        super(order);
    }
    
    @Override
    public void payOrder() {
        System.out.println("订单已发货，无法支付");
    }
    
    @Override
    public void shipOrder() {
        System.out.println("订单已发货，无需重复发货");
    }
    
    @Override
    public void deliverOrder() {
        System.out.println("订单已确认收货");
        order.setState(new DeliveredState(order));
    }
    
    @Override
    public void cancelOrder() {
        System.out.println("订单已发货，无法直接取消，请申请退货");
    }
    
    @Override
    public void returnOrder() {
        System.out.println("申请退货处理中");
        order.setState(new ReturnRequestedState(order));
    }
    
    @Override
    public String getStateName() {
        return "已发货";
    }
}