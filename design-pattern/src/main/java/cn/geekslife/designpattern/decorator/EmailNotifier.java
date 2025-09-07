package cn.geekslife.designpattern.decorator;

/**
 * 邮件通知 - 具体组件
 */
public class EmailNotifier implements Notifier {
    private String emailAddress;
    
    public EmailNotifier(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    @Override
    public void send(String message) {
        System.out.println("发送邮件到 " + emailAddress + ": " + message);
    }
    
    // getter和setter
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
}