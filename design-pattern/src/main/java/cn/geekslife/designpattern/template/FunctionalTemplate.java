package cn.geekslife.designpattern.template;

/**
 * 函数式模板方法 - 使用函数式编程解决里氏替换原则问题
 */
public class FunctionalTemplate {
    
    // 函数式模板方法
    public static void executeTemplate(
            Runnable initializer,
            java.util.function.Supplier<Object> dataPreparer,
            Runnable validator,
            java.util.function.Function<Object, Object> operationExecutor,
            java.util.function.Function<Object, Object> resultProcessor,
            Runnable cleanup) {
        
        System.out.println("开始执行函数式模板方法...");
        
        // 1. 初始化
        if (initializer != null) {
            initializer.run();
        }
        
        // 2. 准备数据
        Object data = null;
        if (dataPreparer != null) {
            data = dataPreparer.get();
            System.out.println("数据准备完成: " + data);
        }
        
        // 3. 验证数据
        if (validator != null) {
            validator.run();
        }
        
        // 4. 执行操作
        Object result = null;
        if (operationExecutor != null && data != null) {
            result = operationExecutor.apply(data);
            System.out.println("操作执行完成: " + result);
        }
        
        // 5. 处理结果
        if (resultProcessor != null && result != null) {
            Object processedResult = resultProcessor.apply(result);
            System.out.println("结果处理完成: " + processedResult);
        }
        
        // 6. 清理资源
        if (cleanup != null) {
            cleanup.run();
        }
        
        System.out.println("函数式模板方法执行完成");
    }
}