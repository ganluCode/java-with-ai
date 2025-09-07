package cn.geekslife.designpattern.decorator;

/**
 * Slack通知装饰器 - 具体装饰器
 */
public class SlackNotifierDecorator extends NotifierDecorator {
    private String slackChannel;
    
    public SlackNotifierDecorator(Notifier notifier, String slackChannel) {
        super(notifier);
        this.slackChannel = slackChannel;
    }
    
    @Override
    public void send(String message) {
        super.send(message);
        sendSlackMessage(message);
    }
    
    private void sendSlackMessage(String message) {
        System.out.println("发送Slack消息到 " + slackChannel + ": " + message);
    }
    
    // getter和setter
    public String getSlackChannel() { return slackChannel; }
    public void setSlackChannel(String slackChannel) { this.slackChannel = slackChannel; }
}