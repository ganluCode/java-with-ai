package cn.geekslife.designpattern.strategy

import cn.geekslife.designpattern.strategy.CombinedPromotion
import cn.geekslife.designpattern.strategy.DiscountPromotion
import cn.geekslife.designpattern.strategy.FreeShippingPromotion
import cn.geekslife.designpattern.strategy.FullReductionPromotion
import cn.geekslife.designpattern.strategy.GiftPromotion
import cn.geekslife.designpattern.strategy.PromotionContext
import cn.geekslife.designpattern.strategy.PromotionStrategy
import spock.lang.Specification

class PromotionStrategySpec extends Specification {

    def "should calculate correct price with full reduction promotion"() {
        given:
        PromotionContext context = new PromotionContext()
        PromotionStrategy fullReduction = new FullReductionPromotion(100, 20)
        context.setPromotionStrategy(fullReduction)
        
        when:
        double result = context.executePromotion(150.0)
        
        then:
        result == 130.0
    }
    
    def "should not apply full reduction when amount is less than threshold"() {
        given:
        PromotionContext context = new PromotionContext()
        PromotionStrategy fullReduction = new FullReductionPromotion(100, 20)
        context.setPromotionStrategy(fullReduction)
        
        when:
        double result = context.executePromotion(80.0)
        
        then:
        result == 80.0
    }
    
    def "should calculate correct price with discount promotion"() {
        given:
        PromotionContext context = new PromotionContext()
        PromotionStrategy discount = new DiscountPromotion(0.8)
        context.setPromotionStrategy(discount)
        
        when:
        double result = context.executePromotion(100.0)
        
        then:
        result == 80.0
    }
    
    def "should calculate correct price with gift promotion"() {
        given:
        PromotionContext context = new PromotionContext()
        PromotionStrategy gift = new GiftPromotion("精美礼品", 50)
        context.setPromotionStrategy(gift)
        
        when:
        double result = context.executePromotion(200.0)
        
        then:
        result == 200.0
    }
    
    def "should calculate correct price with free shipping promotion"() {
        given:
        PromotionContext context = new PromotionContext()
        PromotionStrategy freeShipping = new FreeShippingPromotion()
        context.setPromotionStrategy(freeShipping)
        
        when:
        double result = context.executePromotion(300.0)
        
        then:
        result == 300.0
    }
    
    def "should calculate correct price with combined promotion"() {
        given:
        PromotionContext context = new PromotionContext()
        PromotionStrategy combined = new CombinedPromotion(
            new FullReductionPromotion(100, 10),
            new DiscountPromotion(0.9)
        )
        context.setPromotionStrategy(combined)
        
        when:
        // 原价120元，先满100减10变为110元，再打9折变为99元
        double result = context.executePromotion(120.0)
        
        then:
        result == 99.0
    }
}