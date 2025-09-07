package strategy;

/**
 * 电商促销策略演示类
 */
public class ECommercePromotionDemo {
    public static void main(String[] args) {
        System.out.println("=== 电商促销策略模式演示 ===");
        
        // 创建促销上下文
        PromotionContext context = new PromotionContext();
        
        // 商品原价
        double originalPrice = 500.0;
        System.out.println("商品原价: " + originalPrice + " 元");
        System.out.println("---");
        
        // 使用满减促销
        System.out.println("1. 使用满减促销:");
        PromotionStrategy fullReduction = new FullReductionPromotion(500, 50);
        context.setPromotionStrategy(fullReduction);
        context.executePromotion(originalPrice);
        
        // 使用折扣促销
        System.out.println("2. 使用折扣促销:");
        PromotionStrategy discount = new DiscountPromotion(0.8);
        context.setPromotionStrategy(discount);
        context.executePromotion(originalPrice);
        
        // 使用赠品促销
        System.out.println("3. 使用赠品促销:");
        PromotionStrategy gift = new GiftPromotion("精美礼品", 50);
        context.setPromotionStrategy(gift);
        context.executePromotion(originalPrice);
        
        // 使用包邮促销
        System.out.println("4. 使用包邮促销:");
        PromotionStrategy freeShipping = new FreeShippingPromotion();
        context.setPromotionStrategy(freeShipping);
        context.executePromotion(originalPrice);
        
        // 使用组合促销
        System.out.println("5. 使用组合促销:");
        PromotionStrategy combined = new CombinedPromotion(
            new FullReductionPromotion(500, 50),
            new DiscountPromotion(0.9)
        );
        context.setPromotionStrategy(combined);
        context.executePromotion(originalPrice);
    }
}