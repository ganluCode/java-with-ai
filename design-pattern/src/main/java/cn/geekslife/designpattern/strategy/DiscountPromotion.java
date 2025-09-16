package cn.geekslife.designpattern.strategy;

/**
 * 折扣促销策略
 */
public class DiscountPromotion implements PromotionStrategy {
    private double discount; // 折扣率 (0.8表示8折)
    
    public DiscountPromotion(double discount) {
        this.discount = discount;
    }
    
    @Override
    public double calculatePromotionPrice(double originalPrice) {
        return originalPrice * discount;
    }
    
    @Override
    public String getPromotionName() {
        return "折扣促销";
    }
    
    @Override
    public String getPromotionDescription() {
        return (discount * 10) + "折";
    }
}