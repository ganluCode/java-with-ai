package cn.geekslife.designpattern.template;

/**
 * MySQL增强版数据库操作 - 具体实现
 */
public class EnhancedMySQLDatabase extends EnhancedDatabaseTemplate {
    
    @Override
    protected Object connect() {
        System.out.println("连接MySQL数据库...");
        return "MySQL连接对象";
    }
    
    @Override
    protected Object prepareData() {
        System.out.println("准备MySQL数据...");
        return "MySQL数据";
    }
    
    @Override
    protected Object executeOperation() {
        System.out.println("执行MySQL数据库操作...");
        return "MySQL查询结果";
    }
    
    @Override
    protected Object processResult(Object result) {
        System.out.println("处理MySQL查询结果...");
        return "处理后的MySQL结果";
    }
    
    @Override
    protected void close() {
        System.out.println("关闭MySQL数据库连接...");
    }
}