package template;

/**
 * MySQL数据库操作 - 具体模板类
 */
public class MySQLDatabase extends DatabaseTemplate {
    
    @Override
    protected void connect() {
        System.out.println("连接MySQL数据库...");
        System.out.println("使用JDBC驱动连接...");
    }
    
    @Override
    protected void prepareData() {
        System.out.println("准备MySQL数据...");
        System.out.println("设置MySQL特定参数...");
    }
    
    @Override
    protected void executeOperation() {
        System.out.println("执行MySQL数据库操作...");
        System.out.println("执行SQL查询: SELECT * FROM users");
    }
    
    @Override
    protected void processResult() {
        System.out.println("处理MySQL查询结果...");
        System.out.println("解析ResultSet...");
    }
    
    @Override
    protected void close() {
        System.out.println("关闭MySQL数据库连接...");
        System.out.println("释放资源...");
    }
    
    // 重写钩子方法，MySQL需要准备数据和处理结果
    @Override
    protected boolean needPrepareData() {
        return true;
    }
    
    @Override
    protected boolean needProcessResult() {
        return true;
    }
}