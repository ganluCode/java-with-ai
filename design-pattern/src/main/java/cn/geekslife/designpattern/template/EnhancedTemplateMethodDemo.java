package cn.geekslife.designpattern.template;

/**
 * 模板方法模式增强特性演示类
 */
public class EnhancedTemplateMethodDemo {
    public static void main(String[] args) {
        System.out.println("=== 增强版模板方法模式演示 ===");
        
        System.out.println("\n1. 使用回调机制的增强版数据库操作:");
        EnhancedDatabaseTemplate mysql = new EnhancedMySQLDatabase();
        
        // 使用Lambda表达式作为回调
        mysql.executeDatabaseOperation(
            connection -> System.out.println("连接建立完成: " + connection),
            data -> System.out.println("数据准备完成: " + data),
            result -> System.out.println("操作执行完成: " + result),
            processedResult -> System.out.println("结果处理完成: " + processedResult),
            () -> System.out.println("连接关闭完成")
        );
        
        System.out.println("\n2. 使用配置驱动的模板方法:");
        // 配置不同的流程
        ConfigurableTemplate template1 = new ConcreteConfigurableTemplate(true, true, true);
        template1.execute();
        
        System.out.println("\n--- 跳过数据准备和验证 ---");
        ConfigurableTemplate template2 = new ConcreteConfigurableTemplate(false, true, false);
        template2.execute();
        
        System.out.println("\n3. 使用函数式模板方法:");
        // 使用函数式接口
        FunctionalTemplate.executeTemplate(
            () -> System.out.println("初始化完成"),
            () -> {
                System.out.println("准备数据...");
                return "函数式数据";
            },
            () -> System.out.println("数据验证完成"),
            data -> {
                System.out.println("处理数据: " + data);
                return "函数式结果";
            },
            result -> {
                System.out.println("处理结果: " + result);
                return "最终结果";
            },
            () -> System.out.println("资源清理完成")
        );
    }
}