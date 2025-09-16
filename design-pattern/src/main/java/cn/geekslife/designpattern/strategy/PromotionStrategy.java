package cn.geekslife.designpattern.strategy;

/**
 * 抽象促销策略接口
 */
public interface PromotionStrategy {
    /**
     * 计算促销后的价格
     * @param originalPrice 原价
     * @return 促销后价格
     */
    double calculatePromotionPrice(double originalPrice);
    
    /**
     * 获取促销策略名称
     * @return 策略名称
     */
    String getPromotionName();
    
    /**
     * 获取促销描述
     * @return 促销描述
     */
    String getPromotionDescription();
}