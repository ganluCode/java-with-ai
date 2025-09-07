package com.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Java动态代理演示
 * 展示如何使用Java内置的动态代理功能
 */
public class DynamicProxyDemo {
    
    /**
     * 日志拦截器
     */
    static class LoggingInterceptor implements InvocationHandler {
        private Object target;
        
        public LoggingInterceptor(Object target) {
            this.target = target;
        }
        
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("[动态代理] 调用方法: " + method.getName());
            long startTime = System.currentTimeMillis();
            
            // 调用真实对象的方法
            Object result = method.invoke(target, args);
            
            long endTime = System.currentTimeMillis();
            System.out.println("[动态代理] 方法执行完成，耗时: " + (endTime - startTime) + "ms");
            
            return result;
        }
    }
    
    /**
     * 性能监控拦截器
     */
    static class PerformanceInterceptor implements InvocationHandler {
        private Object target;
        
        public PerformanceInterceptor(Object target) {
            this.target = target;
        }
        
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            long startTime = System.nanoTime();
            
            // 调用真实对象的方法
            Object result = method.invoke(target, args);
            
            long endTime = System.nanoTime();
            System.out.println("[性能监控] " + method.getName() + " 执行时间: " + (endTime - startTime) + "ns");
            
            return result;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Java动态代理演示 ===");
        
        // 创建真实对象
        Subject realSubject = new RealSubject();
        
        // 创建日志代理
        Subject loggingProxy = (Subject) Proxy.newProxyInstance(
            Subject.class.getClassLoader(),
            new Class[]{Subject.class},
            new LoggingInterceptor(realSubject)
        );
        
        // 使用日志代理
        System.out.println("\n--- 使用日志代理 ---");
        loggingProxy.request();
        String data = loggingProxy.getData();
        System.out.println("获取数据: " + data);
        
        // 创建性能监控代理
        Subject performanceProxy = (Subject) Proxy.newProxyInstance(
            Subject.class.getClassLoader(),
            new Class[]{Subject.class},
            new PerformanceInterceptor(realSubject)
        );
        
        // 使用性能监控代理
        System.out.println("\n--- 使用性能监控代理 ---");
        performanceProxy.request();
        String perfData = performanceProxy.getData();
        System.out.println("获取数据: " + perfData);
        
        // 链式代理：先日志后性能监控
        System.out.println("\n--- 链式代理演示 ---");
        Subject chainedProxy = (Subject) Proxy.newProxyInstance(
            Subject.class.getClassLoader(),
            new Class[]{Subject.class},
            new LoggingInterceptor(
                Proxy.newProxyInstance(
                    Subject.class.getClassLoader(),
                    new Class[]{Subject.class},
                    new PerformanceInterceptor(realSubject)
                )
            )
        );
        
        chainedProxy.request();
        String chainedData = chainedProxy.getData();
        System.out.println("获取数据: " + chainedData);
    }
}