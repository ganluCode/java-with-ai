package cn.geekslife.designpattern.strategy;

/**
 * 支付上下文类
 */
public class PaymentContext {
    private PaymentStrategy paymentStrategy;
    
    // 设置支付策略
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    
    // 执行支付
    public boolean executePayment(double amount) {
        if (paymentStrategy == null) {
            System.out.println("请先选择支付方式");
            return false;
        }
        
        System.out.println("开始执行支付...");
        boolean result = paymentStrategy.pay(amount);
        if (result) {
            System.out.println("支付成功！使用方式: " + paymentStrategy.getPaymentName());
        } else {
            System.out.println("支付失败！");
        }
        System.out.println("---");
        return result;
    }
    
    // 获取当前支付策略
    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }
}