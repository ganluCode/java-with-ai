package com.example.proxy;

/**
 * 真实主题类
 * 定义了代理对象所代表的真实对象
 */
public class RealSubject implements Subject {
    private String data;
    
    public RealSubject() {
        // 模拟创建开销很大的对象
        System.out.println("创建RealSubject对象...");
        try {
            Thread.sleep(1000); // 模拟耗时操作
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.data = "真实数据";
        System.out.println("RealSubject对象创建完成");
    }
    
    @Override
    public void request() {
        System.out.println("RealSubject处理请求");
    }
    
    @Override
    public String getData() {
        return data;
    }
}