package strategy;

/**
 * 赠品促销策略
 */
public class GiftPromotion implements PromotionStrategy {
    private String giftName;    // 赠品名称
    private double giftValue;   // 赠品价值
    
    public GiftPromotion(String giftName, double giftValue) {
        this.giftName = giftName;
        this.giftValue = giftValue;
    }
    
    @Override
    public double calculatePromotionPrice(double originalPrice) {
        // 赠品不影响商品价格，但可以认为总价值增加了
        return originalPrice;
    }
    
    @Override
    public String getPromotionName() {
        return "赠品促销";
    }
    
    @Override
    public String getPromotionDescription() {
        return "购买即赠" + giftName + "(价值" + giftValue + "元)";
    }
    
    // 获取赠品价值
    public double getGiftValue() {
        return giftValue;
    }
    
    // 获取赠品名称
    public String getGiftName() {
        return giftName;
    }
}