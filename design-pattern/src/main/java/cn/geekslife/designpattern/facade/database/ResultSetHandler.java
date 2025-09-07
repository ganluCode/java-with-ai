package cn.geekslife.designpattern.facade.database;

/**
 * 结果集处理器子系统类
 */
public class ResultSetHandler {
    public void processResults() {
        System.out.println("处理查询结果");
    }
    
    public void closeResultSet() {
        System.out.println("关闭结果集");
    }
}