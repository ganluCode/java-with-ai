package cn.geekslife.designpattern.facade.database;

/**
 * 连接管理子系统类
 */
public class ConnectionManager {
    public void connect(String url, String username, String password) {
        System.out.println("连接到数据库: " + url + " 用户: " + username);
    }
    
    public void disconnect() {
        System.out.println("断开数据库连接");
    }
    
    public boolean isConnected() {
        // 简化实现
        return true;
    }
}