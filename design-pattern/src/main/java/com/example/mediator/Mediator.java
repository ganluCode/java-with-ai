package com.example.mediator;

/**
 * 中介者接口
 * 定义中介者的通用接口
 */
public interface Mediator {
    /**
     * 处理组件发送的通知
     * @param sender 发送通知的组件
     * @param event 事件类型
     * @param data 附加数据
     */
    void notify(Component sender, String event, Object... data);
}