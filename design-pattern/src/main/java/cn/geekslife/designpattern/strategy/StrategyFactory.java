package cn.geekslife.designpattern.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略工厂 - 解决客户端选择策略的复杂性问题
 * 集中管理策略对象的创建
 */
public class StrategyFactory {
    private static StrategyFactory instance;
    private Map<String, PaymentStrategy> paymentStrategies;
    private Map<String, SortStrategy> sortStrategies;
    private Map<String, PromotionStrategy> promotionStrategies;
    
    private StrategyFactory() {
        initStrategies();
    }
    
    public static StrategyFactory getInstance() {
        if (instance == null) {
            synchronized (StrategyFactory.class) {
                if (instance == null) {
                    instance = new StrategyFactory();
                }
            }
        }
        return instance;
    }
    
    // 初始化所有策略
    private void initStrategies() {
        paymentStrategies = new HashMap<>();
        paymentStrategies.put("credit_card", new CreditCardPayment("1234-5678-9012-3456", "默认用户", "123", "12/25"));
        paymentStrategies.put("alipay", new AlipayPayment("default@example.com"));
        paymentStrategies.put("wechat", new WechatPayment("default_wechat"));
        paymentStrategies.put("paypal", new PayPalPayment("default@paypal.com"));
        
        sortStrategies = new HashMap<>();
        sortStrategies.put("bubble", new BubbleSort());
        sortStrategies.put("quick", new QuickSort());
        sortStrategies.put("merge", new MergeSort());
        sortStrategies.put("selection", new SelectionSort());
        
        promotionStrategies = new HashMap<>();
        promotionStrategies.put("full_100_reduce_20", new FullReductionPromotion(100, 20));
        promotionStrategies.put("eight_discount", new DiscountPromotion(0.8));
        promotionStrategies.put("gift", new GiftPromotion("精美礼品", 50));
        promotionStrategies.put("free_shipping", new FreeShippingPromotion());
    }
    
    // 获取支付策略
    public PaymentStrategy getPaymentStrategy(String strategyName) {
        return paymentStrategies.get(strategyName);
    }
    
    // 获取排序策略
    public SortStrategy getSortStrategy(String strategyName) {
        return sortStrategies.get(strategyName);
    }
    
    // 获取促销策略
    public PromotionStrategy getPromotionStrategy(String strategyName) {
        return promotionStrategies.get(strategyName);
    }
    
    // 获取所有支付策略名称
    public String[] getPaymentStrategyNames() {
        return paymentStrategies.keySet().toArray(new String[0]);
    }
    
    // 获取所有排序策略名称
    public String[] getSortStrategyNames() {
        return sortStrategies.keySet().toArray(new String[0]);
    }
    
    // 获取所有促销策略名称
    public String[] getPromotionStrategyNames() {
        return promotionStrategies.keySet().toArray(new String[0]);
    }
}