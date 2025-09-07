package state

import spock.lang.Specification

class OrderStateSpec extends Specification {

    def "pending payment state should handle operations correctly"() {
        given:
        Order order = new Order("TEST101")
        OrderState state = new PendingPaymentState(order)
        
        when:
        state.payOrder()
        String payResult = order.getState().getStateName()
        
        order.setState(new PendingPaymentState(order)) // 重置状态
        state = order.getState()
        state.cancelOrder()
        String cancelResult = order.getState().getStateName()
        
        then:
        payResult == "已支付"
        cancelResult == "已取消"
    }
    
    def "paid state should handle operations correctly"() {
        given:
        Order order = new Order("TEST102")
        order.payOrder() // 转换到已支付状态
        OrderState state = order.getState()
        
        when:
        state.shipOrder()
        String shipResult = order.getState().getStateName()
        
        order.setState(new PaidState(order)) // 重置状态
        state = order.getState()
        state.cancelOrder()
        String cancelResult = order.getState().getStateName()
        
        then:
        shipResult == "已发货"
        cancelResult == "已取消"
    }
    
    def "shipped state should handle operations correctly"() {
        given:
        Order order = new Order("TEST103")
        order.payOrder()   // 已支付
        order.shipOrder()  // 已发货
        OrderState state = order.getState()
        
        when:
        state.deliverOrder()
        String deliverResult = order.getState().getStateName()
        
        order.setState(new ShippedState(order)) // 重置状态
        state = order.getState()
        state.returnOrder()
        String returnResult = order.getState().getStateName()
        
        then:
        deliverResult == "已收货"
        returnResult == "退货申请中"
    }
}