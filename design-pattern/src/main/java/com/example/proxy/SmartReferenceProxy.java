package com.example.proxy;

/**
 * 智能引用代理类
 * 在访问对象时执行一些附加操作
 */
public class SmartReferenceProxy implements Subject {
    private RealSubject realSubject;
    private int referenceCount = 0;
    
    @Override
    public void request() {
        if (realSubject == null) {
            realSubject = new RealSubject();
        }
        referenceCount++;
        System.out.println("访问次数: " + referenceCount);
        realSubject.request();
    }
    
    @Override
    public String getData() {
        if (realSubject == null) {
            realSubject = new RealSubject();
        }
        referenceCount++;
        System.out.println("访问次数: " + referenceCount);
        return realSubject.getData();
    }
    
    public int getReferenceCount() {
        return referenceCount;
    }
}