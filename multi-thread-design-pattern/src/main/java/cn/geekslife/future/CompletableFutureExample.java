package cn.geekslife.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 使用CompletableFuture的高级Future实现
 * 演示Future模式的现代Java实现
 */
public class CompletableFutureExample {
    
    /**
     * 异步处理数据
     * @param data 原始数据
     * @return CompletableFuture对象
     */
    public CompletableFuture<String> processAsync(String data) {
        System.out.println("CompletableFutureExample：开始异步处理 - " + data);
        
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("CompletableFutureExample：后台处理数据 - " + data);
            try {
                // 模拟耗时操作
                Thread.sleep(1000);
                return "处理结果: " + data.toUpperCase();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * 带超时的异步处理
     * @param data 原始数据
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 处理结果
     * @throws TimeoutException 超时异常
     * @throws ExecutionException 执行异常
     * @throws InterruptedException 中断异常
     */
    public String processWithTimeout(String data, long timeout, TimeUnit unit) 
            throws TimeoutException, ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("CompletableFutureExample：带超时处理 - " + data);
            try {
                Thread.sleep(1000);
                return "超时处理结果: " + data;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }).get(timeout, unit);
    }
    
    /**
     * 链式异步处理
     * @param data 原始数据
     * @return CompletableFuture对象
     */
    public CompletableFuture<String> processChain(String data) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("步骤1：转换为大写 - " + data);
            return data.toUpperCase();
        }).thenApply(result -> {
            System.out.println("步骤2：添加前缀 - " + result);
            return "[前缀]" + result;
        }).thenApply(result -> {
            System.out.println("步骤3：添加后缀 - " + result);
            return result + "[后缀]";
        });
    }
    
    /**
     * 异常处理示例
     * @param data 原始数据
     * @return CompletableFuture对象
     */
    public CompletableFuture<String> processWithExceptionHandling(String data) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("异常处理示例 - " + data);
            if (Math.random() < 0.5) {
                throw new RuntimeException("模拟处理异常");
            }
            return "成功处理: " + data;
        }).exceptionally(throwable -> {
            System.out.println("捕获异常: " + throwable.getMessage());
            return "异常处理结果: " + data;
        });
    }
}