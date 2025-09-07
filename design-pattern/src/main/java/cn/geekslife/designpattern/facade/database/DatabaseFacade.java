package cn.geekslife.designpattern.facade.database;

/**
 * 数据库访问门面类
 */
public class DatabaseFacade {
    private ConnectionManager connectionManager;
    private SQLExecutor sqlExecutor;
    private ResultSetHandler resultSetHandler;
    private TransactionManager transactionManager;
    
    public DatabaseFacade() {
        this.connectionManager = new ConnectionManager();
        this.sqlExecutor = new SQLExecutor();
        this.resultSetHandler = new ResultSetHandler();
        this.transactionManager = new TransactionManager();
    }
    
    public void connectToDatabase(String url, String username, String password) {
        System.out.println("=== 连接数据库 ===");
        connectionManager.connect(url, username, password);
        System.out.println("=== 数据库连接完成 ===\n");
    }
    
    public void executeSimpleQuery(String sql) {
        System.out.println("=== 执行简单查询 ===");
        if (connectionManager.isConnected()) {
            sqlExecutor.executeQuery(sql);
            resultSetHandler.processResults();
            resultSetHandler.closeResultSet();
        } else {
            System.out.println("错误: 数据库未连接");
        }
        System.out.println("=== 查询执行完成 ===\n");
    }
    
    public void executeTransaction(String[] sqls) {
        System.out.println("=== 执行事务 ===");
        if (connectionManager.isConnected()) {
            transactionManager.beginTransaction();
            sqlExecutor.executeBatch(sqls);
            transactionManager.commit();
        } else {
            System.out.println("错误: 数据库未连接");
        }
        System.out.println("=== 事务执行完成 ===\n");
    }
    
    public void disconnect() {
        System.out.println("=== 断开数据库连接 ===");
        connectionManager.disconnect();
        System.out.println("=== 连接已断开 ===\n");
    }
}