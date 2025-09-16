package cn.geekslife.designpattern.strategy;

/**
 * 支付宝支付策略
 */
public class AlipayPayment implements PaymentStrategy {
    private String alipayAccount;
    
    public AlipayPayment(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }
    
    @Override
    public boolean pay(double amount) {
        System.out.println("使用支付宝支付: " + amount + " 元");
        System.out.println("支付宝账户: " + alipayAccount);
        // 模拟支付处理
        return processAlipayPayment(amount);
    }
    
    @Override
    public String getPaymentName() {
        return "支付宝支付";
    }
    
    private boolean processAlipayPayment(double amount) {
        // 模拟支付宝支付处理逻辑
        System.out.println("正在处理支付宝支付...");
        // 模拟支付成功
        return true;
    }
}