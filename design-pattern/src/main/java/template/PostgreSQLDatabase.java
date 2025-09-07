package template;

/**
 * PostgreSQL数据库操作 - 具体模板类
 */
public class PostgreSQLDatabase extends DatabaseTemplate {
    
    @Override
    protected void connect() {
        System.out.println("连接PostgreSQL数据库...");
        System.out.println("使用PostgreSQL JDBC驱动连接...");
    }
    
    @Override
    protected void prepareData() {
        System.out.println("准备PostgreSQL数据...");
        System.out.println("设置PostgreSQL特定参数...");
    }
    
    @Override
    protected void executeOperation() {
        System.out.println("执行PostgreSQL数据库操作...");
        System.out.println("执行SQL查询: SELECT * FROM users WHERE id = $1");
    }
    
    @Override
    protected void processResult() {
        System.out.println("处理PostgreSQL查询结果...");
        System.out.println("解析ResultSet...");
    }
    
    @Override
    protected void close() {
        System.out.println("关闭PostgreSQL数据库连接...");
        System.out.println("释放资源...");
    }
    
    // 重写钩子方法，PostgreSQL需要准备数据和处理结果
    @Override
    protected boolean needPrepareData() {
        return true;
    }
    
    @Override
    protected boolean needProcessResult() {
        return true;
    }
}