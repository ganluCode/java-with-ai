package cn.geekslife.designpattern.template;

/**
 * MongoDB数据库操作 - 具体模板类
 */
public class MongoDBDatabase extends DatabaseTemplate {
    
    @Override
    protected void connect() {
        System.out.println("连接MongoDB数据库...");
        System.out.println("使用MongoDB驱动连接...");
    }
    
    @Override
    protected void prepareData() {
        System.out.println("准备MongoDB数据...");
        System.out.println("构建BSON文档...");
    }
    
    @Override
    protected void executeOperation() {
        System.out.println("执行MongoDB数据库操作...");
        System.out.println("执行查询: db.users.find({})");
    }
    
    // 重写钩子方法，MongoDB不需要处理结果（驱动自动处理）
    @Override
    protected boolean needProcessResult() {
        return false;
    }
    
    @Override
    protected void processResult() {
        // MongoDB不需要手动处理结果
        System.out.println("MongoDB自动处理结果...");
    }
    
    @Override
    protected void close() {
        System.out.println("关闭MongoDB数据库连接...");
        System.out.println("释放资源...");
    }
    
    // 重写钩子方法，MongoDB需要准备数据
    @Override
    protected boolean needPrepareData() {
        return true;
    }
}