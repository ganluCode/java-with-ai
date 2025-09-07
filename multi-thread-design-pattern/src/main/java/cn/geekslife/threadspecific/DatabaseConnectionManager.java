package cn.geekslife.threadspecific;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接管理器类 - Thread-Specific Storage模式在数据库连接中的应用
 */
public class DatabaseConnectionManager {
    // 线程本地存储的数据库连接
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();
    // 数据库URL
    private static final String DB_URL = "jdbc:h2:mem:testdb";
    // 数据库用户名
    private static final String DB_USER = "sa";
    // 数据库密码
    private static final String DB_PASSWORD = "";
    
    /**
     * 获取当前线程的数据库连接
     * @return 数据库连接
     * @throws SQLException SQL异常
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = connectionHolder.get();
        if (conn == null || conn.isClosed()) {
            System.out.println(Thread.currentThread().getName() + "：创建新的数据库连接");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            connectionHolder.set(conn);
        } else {
            System.out.println(Thread.currentThread().getName() + "：使用现有数据库连接");
        }
        return conn;
    }
    
    /**
     * 关闭当前线程的数据库连接
     */
    public static void closeConnection() {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println(Thread.currentThread().getName() + "：关闭数据库连接");
                }
            } catch (SQLException e) {
                System.err.println(Thread.currentThread().getName() + "：关闭数据库连接时发生错误: " + e.getMessage());
            } finally {
                connectionHolder.remove();
            }
        }
    }
    
    /**
     * 获取当前线程的连接状态
     * @return 连接状态
     */
    public static boolean isConnected() {
        Connection conn = connectionHolder.get();
        try {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}