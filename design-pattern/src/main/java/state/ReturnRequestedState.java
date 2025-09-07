package state;

/**
 * 退货申请状态
 */
public class ReturnRequestedState extends OrderState {
    
    public ReturnRequestedState(Order order) {
        super(order);
    }
    
    @Override
    public void payOrder() {
        System.out.println("退货处理中，无法支付");
    }
    
    @Override
    public void shipOrder() {
        System.out.println("退货处理中，无法发货");
    }
    
    @Override
    public void deliverOrder() {
        System.out.println("退货处理中，无法确认收货");
    }
    
    @Override
    public void cancelOrder() {
        System.out.println("退货处理中，无法取消");
    }
    
    @Override
    public void returnOrder() {
        System.out.println("退货申请已提交，正在处理中");
    }
    
    @Override
    public String getStateName() {
        return "退货申请中";
    }
}