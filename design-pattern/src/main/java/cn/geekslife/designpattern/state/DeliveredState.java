package cn.geekslife.designpattern.state;

/**
 * 已收货状态
 */
public class DeliveredState extends OrderState {
    
    public DeliveredState(Order order) {
        super(order);
    }
    
    @Override
    public void payOrder() {
        System.out.println("订单已完成，无法支付");
    }
    
    @Override
    public void shipOrder() {
        System.out.println("订单已完成，无法发货");
    }
    
    @Override
    public void deliverOrder() {
        System.out.println("订单已完成，无需重复确认收货");
    }
    
    @Override
    public void cancelOrder() {
        System.out.println("订单已完成，无法取消");
    }
    
    @Override
    public void returnOrder() {
        System.out.println("申请退货处理中");
        order.setState(new ReturnRequestedState(order));
    }
    
    @Override
    public String getStateName() {
        return "已收货";
    }
}