package strategy;

/**
 * 信用卡支付策略
 */
public class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cardHolderName;
    private String cvv;
    private String expiryDate;
    
    public CreditCardPayment(String cardNumber, String cardHolderName, String cvv, String expiryDate) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }
    
    @Override
    public boolean pay(double amount) {
        System.out.println("使用信用卡支付: " + amount + " 元");
        System.out.println("卡号: " + cardNumber);
        System.out.println("持卡人: " + cardHolderName);
        // 模拟支付处理
        return processCreditCardPayment(amount);
    }
    
    @Override
    public String getPaymentName() {
        return "信用卡支付";
    }
    
    private boolean processCreditCardPayment(double amount) {
        // 模拟信用卡支付处理逻辑
        System.out.println("正在处理信用卡支付...");
        // 模拟支付成功
        return true;
    }
}