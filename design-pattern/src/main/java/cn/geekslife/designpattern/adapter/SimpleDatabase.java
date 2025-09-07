package cn.geekslife.designpattern.adapter;

/**
 * 简单数据库实现 - 只需要重写需要的方法
 */
public class SimpleDatabase extends DatabaseAdapter {
    @Override
    public void connect() {
        System.out.println("Simple database connecting...");
    }
    
    @Override
    public void executeQuery(String sql) {
        System.out.println("Simple database executing query: " + sql);
    }
    
    @Override
    public void executeUpdate(String sql) {
        System.out.println("Simple database executing update: " + sql);
    }
}