package cn.geekslife.designpattern.strategy;

/**
 * 促销上下文类
 */
public class PromotionContext {
    private PromotionStrategy promotionStrategy;
    
    // 设置促销策略
    public void setPromotionStrategy(PromotionStrategy promotionStrategy) {
        this.promotionStrategy = promotionStrategy;
    }
    
    // 执行促销计算
    public double executePromotion(double originalPrice) {
        if (promotionStrategy == null) {
            System.out.println("请先选择促销策略");
            return originalPrice;
        }
        
        System.out.println("应用促销策略: " + promotionStrategy.getPromotionName());
        System.out.println("促销详情: " + promotionStrategy.getPromotionDescription());
        double promotionPrice = promotionStrategy.calculatePromotionPrice(originalPrice);
        double discount = originalPrice - promotionPrice;
        
        System.out.println("原价: " + originalPrice + " 元");
        System.out.println("优惠金额: " + discount + " 元");
        System.out.println("促销价: " + promotionPrice + " 元");
        System.out.println("---");
        
        return promotionPrice;
    }
    
    // 获取当前促销策略
    public PromotionStrategy getPromotionStrategy() {
        return promotionStrategy;
    }
}