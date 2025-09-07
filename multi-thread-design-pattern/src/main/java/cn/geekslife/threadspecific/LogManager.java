package cn.geekslife.threadspecific;

/**
 * 日志管理器类 - Thread-Specific Storage模式在日志记录中的应用
 */
public class LogManager {
    // 线程本地存储的日志缓冲区
    private static final ThreadLocal<StringBuilder> logBuffer = new ThreadLocal<StringBuilder>() {
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder();
        }
    };
    
    // 线程本地存储的日志级别
    private static final ThreadLocal<LogLevel> logLevel = new ThreadLocal<LogLevel>() {
        @Override
        protected LogLevel initialValue() {
            return LogLevel.INFO;
        }
    };
    
    /**
     * 日志级别枚举
     */
    public enum LogLevel {
        DEBUG, INFO, WARN, ERROR
    }
    
    /**
     * 记录日志
     * @param level 日志级别
     * @param message 日志消息
     */
    public static void log(LogLevel level, String message) {
        if (level.ordinal() >= logLevel.get().ordinal()) {
            StringBuilder buffer = logBuffer.get();
            String logEntry = String.format("[%s] [%s] %s: %s%n", 
                java.time.LocalDateTime.now(), 
                level, 
                Thread.currentThread().getName(), 
                message);
            buffer.append(logEntry);
            System.out.print(logEntry);
        }
    }
    
    /**
     * 记录调试日志
     * @param message 日志消息
     */
    public static void debug(String message) {
        log(LogLevel.DEBUG, message);
    }
    
    /**
     * 记录信息日志
     * @param message 日志消息
     */
    public static void info(String message) {
        log(LogLevel.INFO, message);
    }
    
    /**
     * 记录警告日志
     * @param message 日志消息
     */
    public static void warn(String message) {
        log(LogLevel.WARN, message);
    }
    
    /**
     * 记录错误日志
     * @param message 日志消息
     */
    public static void error(String message) {
        log(LogLevel.ERROR, message);
    }
    
    /**
     * 刷新日志缓冲区
     * @return 缓冲区内容
     */
    public static String flush() {
        StringBuilder buffer = logBuffer.get();
        String content = buffer.toString();
        buffer.setLength(0); // 清空缓冲区
        System.out.println(Thread.currentThread().getName() + "：刷新日志缓冲区");
        return content;
    }
    
    /**
     * 设置当前线程的日志级别
     * @param level 日志级别
     */
    public static void setLogLevel(LogLevel level) {
        logLevel.set(level);
        System.out.println(Thread.currentThread().getName() + "：设置日志级别为 " + level);
    }
    
    /**
     * 获取当前线程的日志级别
     * @return 日志级别
     */
    public static LogLevel getLogLevel() {
        return logLevel.get();
    }
    
    /**
     * 清理日志资源
     */
    public static void clear() {
        logBuffer.remove();
        logLevel.remove();
        System.out.println(Thread.currentThread().getName() + "：清理日志资源");
    }
}