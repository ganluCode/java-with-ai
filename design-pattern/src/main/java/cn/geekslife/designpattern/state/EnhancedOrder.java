package cn.geekslife.designpattern.state;

/**
 * 增强版订单类 - 使用状态管理器
 */
public class EnhancedOrder extends Order {
    private OrderStateManager stateManager;
    
    public EnhancedOrder(String orderId) {
        super(orderId);
        // 初始化状态管理器
        this.stateManager = new OrderStateManager(this);
    }
    
    // 设置状态
    public void setState(OrderState state) {
        this.state = state;
    }
    
    // 获取当前状态
    public OrderState getState() {
        return state;
    }
    
    // 支付订单 - 使用状态管理器
    public void payOrder() {
        System.out.println("订单 [" + orderId + "] 执行支付操作:");
        boolean success = stateManager.performTransition("pay");
        if (success) {
            System.out.println("支付成功，当前状态: " + state.getStateName());
        } else {
            state.payOrder(); // 回退到原有逻辑
        }
        System.out.println("---");
    }
    
    // 发货 - 使用状态管理器
    public void shipOrder() {
        System.out.println("订单 [" + orderId + "] 执行发货操作:");
        boolean success = stateManager.performTransition("ship");
        if (success) {
            System.out.println("发货成功，当前状态: " + state.getStateName());
        } else {
            state.shipOrder(); // 回退到原有逻辑
        }
        System.out.println("---");
    }
    
    // 确认收货 - 使用状态管理器
    public void deliverOrder() {
        System.out.println("订单 [" + orderId + "] 执行确认收货操作:");
        boolean success = stateManager.performTransition("deliver");
        if (success) {
            System.out.println("确认收货成功，当前状态: " + state.getStateName());
        } else {
            state.deliverOrder(); // 回退到原有逻辑
        }
        System.out.println("---");
    }
    
    // 取消订单 - 使用状态管理器
    public void cancelOrder() {
        System.out.println("订单 [" + orderId + "] 执行取消操作:");
        boolean success = stateManager.performTransition("cancel");
        if (success) {
            System.out.println("取消成功，当前状态: " + state.getStateName());
        } else {
            state.cancelOrder(); // 回退到原有逻辑
        }
        System.out.println("---");
    }
    
    // 退货 - 使用状态管理器
    public void returnOrder() {
        System.out.println("订单 [" + orderId + "] 执行退货操作:");
        boolean success = stateManager.performTransition("return");
        if (success) {
            System.out.println("退货申请成功，当前状态: " + state.getStateName());
        } else {
            state.returnOrder(); // 回退到原有逻辑
        }
        System.out.println("---");
    }
    
    // 显示当前状态
    public void showState() {
        System.out.println("订单 [" + orderId + "] 当前状态: " + state.getStateName());
    }
}