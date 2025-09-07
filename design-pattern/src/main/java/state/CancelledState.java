package state;

/**
 * 已取消状态
 */
public class CancelledState extends OrderState {
    
    public CancelledState(Order order) {
        super(order);
    }
    
    @Override
    public void payOrder() {
        System.out.println("订单已取消，无法支付");
    }
    
    @Override
    public void shipOrder() {
        System.out.println("订单已取消，无法发货");
    }
    
    @Override
    public void deliverOrder() {
        System.out.println("订单已取消，无法确认收货");
    }
    
    @Override
    public void cancelOrder() {
        System.out.println("订单已取消");
    }
    
    @Override
    public void returnOrder() {
        System.out.println("订单已取消，无法退货");
    }
    
    @Override
    public String getStateName() {
        return "已取消";
    }
}