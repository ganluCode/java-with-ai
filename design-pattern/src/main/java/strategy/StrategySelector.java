package strategy;

/**
 * 策略选择器 - 简化客户端的策略选择过程
 */
public class StrategySelector {
    
    /**
     * 根据金额选择最优支付策略
     * @param amount 金额
     * @return 支付策略
     */
    public static PaymentStrategy selectPaymentStrategy(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("金额必须大于0");
        }
        
        // 简单的策略选择逻辑
        if (amount < 100) {
            return new WechatPayment("default_wechat");
        } else if (amount < 1000) {
            return new AlipayPayment("default@example.com");
        } else {
            return new CreditCardPayment("1234-5678-9012-3456", "默认用户", "123", "12/25");
        }
    }
    
    /**
     * 根据数组大小选择最优排序策略
     * @param arraySize 数组大小
     * @return 排序策略
     */
    public static SortStrategy selectSortStrategy(int arraySize) {
        if (arraySize <= 0) {
            throw new IllegalArgumentException("数组大小必须大于0");
        }
        
        // 根据数组大小选择最优排序算法
        if (arraySize < 10) {
            return new BubbleSort();  // 小数组使用冒泡排序
        } else if (arraySize < 100) {
            return new QuickSort();   // 中等数组使用快速排序
        } else {
            return new MergeSort();   // 大数组使用归并排序
        }
    }
    
    /**
     * 根据商品类型选择促销策略
     * @param productType 商品类型
     * @return 促销策略
     */
    public static PromotionStrategy selectPromotionStrategy(String productType) {
        if (productType == null || productType.isEmpty()) {
            return new FreeShippingPromotion();  // 默认包邮
        }
        
        switch (productType.toLowerCase()) {
            case "electronics":
                return new FullReductionPromotion(1000, 100);  // 电子产品满减
            case "clothing":
                return new DiscountPromotion(0.9);  // 服装类9折
            case "books":
                return new GiftPromotion("书签", 5);  // 图书赠品
            default:
                return new FreeShippingPromotion();  // 默认包邮
        }
    }
}