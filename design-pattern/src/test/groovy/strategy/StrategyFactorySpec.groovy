package strategy

import spock.lang.Specification

class StrategyFactorySpec extends Specification {

    def "should create strategy factory as singleton"() {
        when:
        StrategyFactory factory1 = StrategyFactory.getInstance()
        StrategyFactory factory2 = StrategyFactory.getInstance()
        
        then:
        factory1.is(factory2)
    }
    
    def "should get payment strategies from factory"() {
        given:
        StrategyFactory factory = StrategyFactory.getInstance()
        
        when:
        PaymentStrategy creditCard = factory.getPaymentStrategy("credit_card")
        PaymentStrategy alipay = factory.getPaymentStrategy("alipay")
        PaymentStrategy wechat = factory.getPaymentStrategy("wechat")
        PaymentStrategy paypal = factory.getPaymentStrategy("paypal")
        
        then:
        creditCard instanceof CreditCardPayment
        alipay instanceof AlipayPayment
        wechat instanceof WechatPayment
        paypal instanceof PayPalPayment
    }
    
    def "should get sort strategies from factory"() {
        given:
        StrategyFactory factory = StrategyFactory.getInstance()
        
        when:
        SortStrategy bubble = factory.getSortStrategy("bubble")
        SortStrategy quick = factory.getSortStrategy("quick")
        SortStrategy merge = factory.getSortStrategy("merge")
        SortStrategy selection = factory.getSortStrategy("selection")
        
        then:
        bubble instanceof BubbleSort
        quick instanceof QuickSort
        merge instanceof MergeSort
        selection instanceof SelectionSort
    }
    
    def "should get promotion strategies from factory"() {
        given:
        StrategyFactory factory = StrategyFactory.getInstance()
        
        when:
        PromotionStrategy fullReduction = factory.getPromotionStrategy("full_100_reduce_20")
        PromotionStrategy discount = factory.getPromotionStrategy("eight_discount")
        PromotionStrategy gift = factory.getPromotionStrategy("gift")
        PromotionStrategy freeShipping = factory.getPromotionStrategy("free_shipping")
        
        then:
        fullReduction instanceof FullReductionPromotion
        discount instanceof DiscountPromotion
        gift instanceof GiftPromotion
        freeShipping instanceof FreeShippingPromotion
    }
}