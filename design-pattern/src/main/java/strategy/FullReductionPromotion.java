package strategy;

/**
 * 满减促销策略
 */
public class FullReductionPromotion implements PromotionStrategy {
    private double fullAmount;  // 满足金额
    private double reduction;   // 减免金额
    
    public FullReductionPromotion(double fullAmount, double reduction) {
        this.fullAmount = fullAmount;
        this.reduction = reduction;
    }
    
    @Override
    public double calculatePromotionPrice(double originalPrice) {
        if (originalPrice >= fullAmount) {
            double result = originalPrice - reduction;
            return Math.max(result, 0); // 价格不能为负数
        }
        return originalPrice;
    }
    
    @Override
    public String getPromotionName() {
        return "满减促销";
    }
    
    @Override
    public String getPromotionDescription() {
        return "满" + fullAmount + "减" + reduction;
    }
}