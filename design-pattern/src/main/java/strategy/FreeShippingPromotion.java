package strategy;

/**
 * 包邮促销策略
 */
public class FreeShippingPromotion implements PromotionStrategy {
    
    @Override
    public double calculatePromotionPrice(double originalPrice) {
        // 包邮不影响商品价格，但在实际应用中可能需要考虑运费
        return originalPrice;
    }
    
    @Override
    public String getPromotionName() {
        return "包邮促销";
    }
    
    @Override
    public String getPromotionDescription() {
        return "全场包邮";
    }
}