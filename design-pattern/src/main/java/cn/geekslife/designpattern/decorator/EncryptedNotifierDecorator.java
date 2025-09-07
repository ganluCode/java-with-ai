package cn.geekslife.designpattern.decorator;

import java.util.Base64;

/**
 * 加密通知装饰器 - 具体装饰器
 */
public class EncryptedNotifierDecorator extends NotifierDecorator {
    public EncryptedNotifierDecorator(Notifier notifier) {
        super(notifier);
    }
    
    @Override
    public void send(String message) {
        String encryptedMessage = encrypt(message);
        super.send(encryptedMessage);
    }
    
    private String encrypt(String message) {
        return Base64.getEncoder().encodeToString(message.getBytes());
    }
}