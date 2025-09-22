package cn.geekslife.designpattern.strategy;

/**
 * PayPal支付策略
 */
public class PayPalPayment implements PaymentStrategy {
    private String payPalEmail;
    
    public PayPalPayment(String payPalEmail) {
        this.payPalEmail = payPalEmail;
    }
    
    @Override
    public boolean pay(double amount) {
        System.out.println("使用PayPal支付: " + amount + " 元");
        System.out.println("PayPal账户: " + payPalEmail);
        // 模拟支付处理
        return processPayPalPayment(amount);
    }
    
    @Override
    public String getPaymentName() {
        return "PayPal支付";
    }
    
    private boolean processPayPalPayment(double amount) {
        // 模拟PayPal支付处理逻辑
        System.out.println("正在处理PayPal支付...");
        // 模拟支付成功
        return true;
    }
}