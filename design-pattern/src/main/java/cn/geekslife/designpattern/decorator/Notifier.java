package cn.geekslife.designpattern.decorator;

import java.util.Base64;

/**
 * 通知接口 - 组件接口
 */
public interface Notifier {
    void send(String message);
}