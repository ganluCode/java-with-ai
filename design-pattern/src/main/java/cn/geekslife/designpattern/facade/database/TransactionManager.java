package cn.geekslife.designpattern.facade.database;

/**
 * 事务管理器子系统类
 */
public class TransactionManager {
    public void beginTransaction() {
        System.out.println("开始事务");
    }
    
    public void commit() {
        System.out.println("提交事务");
    }
    
    public void rollback() {
        System.out.println("回滚事务");
    }
}