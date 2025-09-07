package strategy;

/**
 * 抽象支付策略接口
 */
public interface PaymentStrategy {
    /**
     * 执行支付
     * @param amount 支付金额
     * @return 支付结果
     */
    boolean pay(double amount);
    
    /**
     * 获取支付方式名称
     * @return 支付方式名称
     */
    String getPaymentName();
}