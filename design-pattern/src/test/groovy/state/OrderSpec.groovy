package state

import spock.lang.Specification

class OrderSpec extends Specification {

    def "should start in pending payment state"() {
        given:
        Order order = new Order("TEST001")
        
        when:
        String initialState = order.getState().getStateName()
        
        then:
        initialState == "待支付"
    }
    
    def "should transition through normal order flow"() {
        given:
        Order order = new Order("TEST002")
        
        when:
        order.payOrder()
        String paidState = order.getState().getStateName()
        
        order.shipOrder()
        String shippedState = order.getState().getStateName()
        
        order.deliverOrder()
        String deliveredState = order.getState().getStateName()
        
        then:
        paidState == "已支付"
        shippedState == "已发货"
        deliveredState == "已收货"
    }
    
    def "should handle order cancellation from pending payment"() {
        given:
        Order order = new Order("TEST003")
        
        when:
        order.cancelOrder()
        String cancelledState = order.getState().getStateName()
        
        then:
        cancelledState == "已取消"
    }
    
    def "should handle order return from delivered state"() {
        given:
        Order order = new Order("TEST004")
        order.payOrder()     // 已支付
        order.shipOrder()    // 已发货
        order.deliverOrder() // 已收货
        
        when:
        order.returnOrder()
        String returnState = order.getState().getStateName()
        
        then:
        returnState == "退货申请中"
    }
    
    def "should prevent invalid operations"() {
        given:
        Order order = new Order("TEST005")
        // 仍在待支付状态
        
        when:
        order.shipOrder() // 尝试发货未支付的订单
        String state = order.getState().getStateName()
        
        then:
        // 状态应该保持不变
        state == "待支付"
    }
}