package cn.geekslife.designpattern.strategy;

/**
 * 策略模式支付演示类
 */
public class PaymentStrategyDemo {
    public static void main(String[] args) {
        System.out.println("=== 支付策略模式演示 ===");
        
        // 创建支付上下文
        PaymentContext context = new PaymentContext();
        
        // 使用信用卡支付
        System.out.println("1. 使用信用卡支付:");
        PaymentStrategy creditCard = new CreditCardPayment("1234-5678-9012-3456", "张三", "123", "12/25");
        context.setPaymentStrategy(creditCard);
        context.executePayment(100.0);
        
        // 使用支付宝支付
        System.out.println("2. 使用支付宝支付:");
        PaymentStrategy alipay = new AlipayPayment("zhangsan@example.com");
        context.setPaymentStrategy(alipay);
        context.executePayment(200.0);
        
        // 使用微信支付
        System.out.println("3. 使用微信支付:");
        PaymentStrategy wechat = new WechatPayment("wx123456789");
        context.setPaymentStrategy(wechat);
        context.executePayment(150.0);
        
        // 使用PayPal支付
        System.out.println("4. 使用PayPal支付:");
        PaymentStrategy paypal = new PayPalPayment("zhangsan@paypal.com");
        context.setPaymentStrategy(paypal);
        context.executePayment(300.0);
    }
}