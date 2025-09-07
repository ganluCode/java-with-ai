package com.example.proxy;

/**
 * 日志代理类
 * 在访问对象时添加日志记录功能
 */
public class LoggingProxy implements Subject {
    private RealSubject realSubject;
    
    @Override
    public void request() {
        System.out.println("[LOG] 开始处理请求: " + System.currentTimeMillis());
        long startTime = System.currentTimeMillis();
        
        if (realSubject == null) {
            realSubject = new RealSubject();
        }
        realSubject.request();
        
        long endTime = System.currentTimeMillis();
        System.out.println("[LOG] 请求处理完成，耗时: " + (endTime - startTime) + "ms");
    }
    
    @Override
    public String getData() {
        System.out.println("[LOG] 开始获取数据: " + System.currentTimeMillis());
        long startTime = System.currentTimeMillis();
        
        if (realSubject == null) {
            realSubject = new RealSubject();
        }
        
        String data = realSubject.getData();
        long endTime = System.currentTimeMillis();
        System.out.println("[LOG] 数据获取完成，耗时: " + (endTime - startTime) + "ms");
        return data;
    }
}