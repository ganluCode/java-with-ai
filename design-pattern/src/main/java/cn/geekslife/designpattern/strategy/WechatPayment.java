package cn.geekslife.designpattern.strategy;

/**
 * 微信支付策略
 */
public class WechatPayment implements PaymentStrategy {
    private String wechatAccount;
    
    public WechatPayment(String wechatAccount) {
        this.wechatAccount = wechatAccount;
    }
    
    @Override
    public boolean pay(double amount) {
        System.out.println("使用微信支付: " + amount + " 元");
        System.out.println("微信账户: " + wechatAccount);
        // 模拟支付处理
        return processWechatPayment(amount);
    }
    
    @Override
    public String getPaymentName() {
        return "微信支付";
    }
    
    private boolean processWechatPayment(double amount) {
        // 模拟微信支付处理逻辑
        System.out.println("正在处理微信支付...");
        // 模拟支付成功
        return true;
    }
}