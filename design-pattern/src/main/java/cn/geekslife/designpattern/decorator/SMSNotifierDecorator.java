package cn.geekslife.designpattern.decorator;

/**
 * SMS通知装饰器 - 具体装饰器
 */
public class SMSNotifierDecorator extends NotifierDecorator {
    private String phoneNumber;
    
    public SMSNotifierDecorator(Notifier notifier, String phoneNumber) {
        super(notifier);
        this.phoneNumber = phoneNumber;
    }
    
    @Override
    public void send(String message) {
        super.send(message);
        sendSMS(message);
    }
    
    private void sendSMS(String message) {
        System.out.println("发送短信到 " + phoneNumber + ": " + message);
    }
    
    // getter和setter
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}