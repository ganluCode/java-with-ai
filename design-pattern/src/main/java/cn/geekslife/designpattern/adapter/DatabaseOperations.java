package cn.geekslife.designpattern.adapter;

/**
 * 数据库操作接口 - 包含多个方法的复杂接口
 */
public interface DatabaseOperations {
    void connect();
    void disconnect();
    void executeQuery(String sql);
    void executeUpdate(String sql);
    void beginTransaction();
    void commit();
    void rollback();
    void createIndex(String tableName, String columnName);
    void dropIndex(String tableName, String columnName);
}