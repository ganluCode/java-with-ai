package cn.geekslife.designpattern.strategy;

/**
 * 组合促销策略
 */
public class CombinedPromotion implements PromotionStrategy {
    private PromotionStrategy[] strategies;
    
    public CombinedPromotion(PromotionStrategy... strategies) {
        this.strategies = strategies;
    }
    
    @Override
    public double calculatePromotionPrice(double originalPrice) {
        double price = originalPrice;
        for (PromotionStrategy strategy : strategies) {
            price = strategy.calculatePromotionPrice(price);
        }
        return price;
    }
    
    @Override
    public String getPromotionName() {
        return "组合促销";
    }
    
    @Override
    public String getPromotionDescription() {
        StringBuilder description = new StringBuilder("组合优惠: ");
        for (int i = 0; i < strategies.length; i++) {
            if (i > 0) description.append(" + ");
            description.append(strategies[i].getPromotionDescription());
        }
        return description.toString();
    }
}