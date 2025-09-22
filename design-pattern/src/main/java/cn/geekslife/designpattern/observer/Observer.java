package cn.geekslife.designpattern.observer;

/**
 * 抽象观察者接口
 * 定义了更新方法，用于接收主题的通知
 */
public interface Observer {
    void update(Object subject);
}