package cn.geekslife.designpattern.adapter;

/**
 * 数据库适配器 - 提供默认实现的抽象类
 */
public abstract class DatabaseAdapter implements DatabaseOperations {
    @Override
    public void connect() {
        // 默认实现
        System.out.println("Connecting to database...");
    }
    
    @Override
    public void disconnect() {
        // 默认实现
        System.out.println("Disconnecting from database...");
    }
    
    @Override
    public void executeQuery(String sql) {
        // 默认实现
        System.out.println("Executing query: " + sql);
    }
    
    @Override
    public void executeUpdate(String sql) {
        // 默认实现
        System.out.println("Executing update: " + sql);
    }
    
    @Override
    public void beginTransaction() {
        // 默认实现
        System.out.println("Beginning transaction...");
    }
    
    @Override
    public void commit() {
        // 默认实现
        System.out.println("Committing transaction...");
    }
    
    @Override
    public void rollback() {
        // 默认实现
        System.out.println("Rolling back transaction...");
    }
    
    @Override
    public void createIndex(String tableName, String columnName) {
        // 默认实现
        System.out.println("Creating index on " + tableName + "." + columnName);
    }
    
    @Override
    public void dropIndex(String tableName, String columnName) {
        // 默认实现
        System.out.println("Dropping index on " + tableName + "." + columnName);
    }
}