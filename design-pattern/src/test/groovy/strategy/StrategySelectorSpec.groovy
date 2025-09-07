package strategy

import spock.lang.Specification

class StrategySelectorSpec extends Specification {

    def "should select payment strategy based on amount"() {
        when:
        PaymentStrategy strategy1 = StrategySelector.selectPaymentStrategy(50.0)
        PaymentStrategy strategy2 = StrategySelector.selectPaymentStrategy(500.0)
        PaymentStrategy strategy3 = StrategySelector.selectPaymentStrategy(2000.0)
        
        then:
        strategy1 instanceof WechatPayment
        strategy2 instanceof AlipayPayment
        strategy3 instanceof CreditCardPayment
    }
    
    def "should select sort strategy based on array size"() {
        when:
        SortStrategy strategy1 = StrategySelector.selectSortStrategy(5)
        SortStrategy strategy2 = StrategySelector.selectSortStrategy(50)
        SortStrategy strategy3 = StrategySelector.selectSortStrategy(500)
        
        then:
        strategy1 instanceof BubbleSort
        strategy2 instanceof QuickSort
        strategy3 instanceof MergeSort
    }
    
    def "should select promotion strategy based on product type"() {
        when:
        PromotionStrategy strategy1 = StrategySelector.selectPromotionStrategy("electronics")
        PromotionStrategy strategy2 = StrategySelector.selectPromotionStrategy("clothing")
        PromotionStrategy strategy3 = StrategySelector.selectPromotionStrategy("books")
        PromotionStrategy strategy4 = StrategySelector.selectPromotionStrategy("other")
        
        then:
        strategy1 instanceof FullReductionPromotion
        strategy2 instanceof DiscountPromotion
        strategy3 instanceof GiftPromotion
        strategy4 instanceof FreeShippingPromotion
    }
    
    def "should throw exception for invalid parameters"() {
        when:
        StrategySelector.selectPaymentStrategy(-10.0)
        
        then:
        thrown(IllegalArgumentException)
    }
}