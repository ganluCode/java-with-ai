package strategy

import spock.lang.Specification

class PaymentStrategySpec extends Specification {

    def "should execute credit card payment correctly"() {
        given:
        PaymentContext context = new PaymentContext()
        PaymentStrategy creditCard = new CreditCardPayment("1234-5678-9012-3456", "张三", "123", "12/25")
        context.setPaymentStrategy(creditCard)
        
        when:
        boolean result = context.executePayment(100.0)
        
        then:
        result == true
    }
    
    def "should execute alipay payment correctly"() {
        given:
        PaymentContext context = new PaymentContext()
        PaymentStrategy alipay = new AlipayPayment("zhangsan@example.com")
        context.setPaymentStrategy(alipay)
        
        when:
        boolean result = context.executePayment(200.0)
        
        then:
        result == true
    }
    
    def "should execute wechat payment correctly"() {
        given:
        PaymentContext context = new PaymentContext()
        PaymentStrategy wechat = new WechatPayment("wx123456789")
        context.setPaymentStrategy(wechat)
        
        when:
        boolean result = context.executePayment(150.0)
        
        then:
        result == true
    }
    
    def "should execute paypal payment correctly"() {
        given:
        PaymentContext context = new PaymentContext()
        PaymentStrategy paypal = new PayPalPayment("zhangsan@paypal.com")
        context.setPaymentStrategy(paypal)
        
        when:
        boolean result = context.executePayment(300.0)
        
        then:
        result == true
    }
    
    def "should fail when no payment strategy is set"() {
        given:
        PaymentContext context = new PaymentContext()
        
        when:
        boolean result = context.executePayment(100.0)
        
        then:
        result == false
    }
}