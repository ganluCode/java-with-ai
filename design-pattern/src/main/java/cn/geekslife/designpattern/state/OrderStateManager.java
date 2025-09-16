package cn.geekslife.designpattern.state;

/**
 * 基于状态转换表的订单状态管理器
 * 集中管理状态转换逻辑
 */
public class OrderStateManager {
    private StateTransitionTable transitionTable;
    private Order order;
    
    public OrderStateManager(Order order) {
        this.order = order;
        this.transitionTable = new StateTransitionTable();
    }
    
    // 执行状态转换
    public boolean performTransition(String event) {
        String currentState = getStateCode(order.getState());
        if (transitionTable.isValidTransition(currentState, event)) {
            String nextState = transitionTable.getNextState(currentState, event);
            setStateByCode(nextState);
            return true;
        } else {
            System.out.println("无效的状态转换: " + currentState + " -> " + event);
            return false;
        }
    }
    
    // 根据状态对象获取状态代码
    private String getStateCode(OrderState state) {
        if (state instanceof PendingPaymentState) return "pending";
        if (state instanceof PaidState) return "paid";
        if (state instanceof ShippedState) return "shipped";
        if (state instanceof DeliveredState) return "delivered";
        if (state instanceof CancelledState) return "cancelled";
        if (state instanceof ReturnRequestedState) return "return_requested";
        return "unknown";
    }
    
    // 根据状态代码设置状态
    private void setStateByCode(String stateCode) {
        switch (stateCode) {
            case "pending":
                order.setState(new PendingPaymentState(order));
                break;
            case "paid":
                order.setState(new PaidState(order));
                break;
            case "shipped":
                order.setState(new ShippedState(order));
                break;
            case "delivered":
                order.setState(new DeliveredState(order));
                break;
            case "cancelled":
                order.setState(new CancelledState(order));
                break;
            case "return_requested":
                order.setState(new ReturnRequestedState(order));
                break;
        }
    }
}