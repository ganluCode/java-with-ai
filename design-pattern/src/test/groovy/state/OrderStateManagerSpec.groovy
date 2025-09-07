package state

import spock.lang.Specification

class OrderStateManagerSpec extends Specification {

    def "should manage state transitions correctly"() {
        given:
        Order order = new Order("TEST201")
        OrderStateManager stateManager = new OrderStateManager(order)
        
        when:
        boolean paySuccess = stateManager.performTransition("pay")
        String paidState = order.getState().getStateName()
        
        boolean shipSuccess = stateManager.performTransition("ship")
        String shippedState = order.getState().getStateName()
        
        then:
        paySuccess == true
        paidState == "已支付"
        shipSuccess == true
        shippedState == "已发货"
    }
    
    def "should reject invalid state transitions"() {
        given:
        Order order = new Order("TEST202")
        OrderStateManager stateManager = new OrderStateManager(order)
        // 订单仍在待支付状态
        
        when:
        boolean invalidTransition = stateManager.performTransition("deliver")
        String state = order.getState().getStateName()
        
        then:
        invalidTransition == false
        state == "待支付" // 状态应该保持不变
    }
}