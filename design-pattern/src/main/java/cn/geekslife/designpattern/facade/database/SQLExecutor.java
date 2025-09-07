package cn.geekslife.designpattern.facade.database;

/**
 * SQL执行器子系统类
 */
public class SQLExecutor {
    public void executeQuery(String sql) {
        System.out.println("执行查询: " + sql);
    }
    
    public void executeUpdate(String sql) {
        System.out.println("执行更新: " + sql);
    }
    
    public void executeBatch(String[] sqls) {
        System.out.println("执行批处理:");
        for (String sql : sqls) {
            System.out.println("  " + sql);
        }
    }
}