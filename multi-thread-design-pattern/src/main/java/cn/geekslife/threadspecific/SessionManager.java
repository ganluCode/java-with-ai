package cn.geekslife.threadspecific;

/**
 * 会话管理器类 - Thread-Specific Storage模式在会话管理中的应用
 */
public class SessionManager {
    // 线程本地存储的会话信息
    private static final ThreadLocal<SessionInfo> sessionHolder = new ThreadLocal<SessionInfo>();
    
    /**
     * 会话信息类
     */
    public static class SessionInfo {
        private final String sessionId;
        private final long createTime;
        private String userData;
        private long lastAccessTime;
        
        public SessionInfo(String sessionId) {
            this.sessionId = sessionId;
            this.createTime = System.currentTimeMillis();
            this.lastAccessTime = createTime;
        }
        
        public String getSessionId() {
            return sessionId;
        }
        
        public long getCreateTime() {
            return createTime;
        }
        
        public String getUserData() {
            return userData;
        }
        
        public void setUserData(String userData) {
            this.userData = userData;
        }
        
        public long getLastAccessTime() {
            return lastAccessTime;
        }
        
        public void updateLastAccessTime() {
            this.lastAccessTime = System.currentTimeMillis();
        }
        
        public long getSessionAge() {
            return System.currentTimeMillis() - createTime;
        }
        
        public long getIdleTime() {
            return System.currentTimeMillis() - lastAccessTime;
        }
        
        @Override
        public String toString() {
            return "SessionInfo{" +
                    "sessionId='" + sessionId + '\'' +
                    ", createTime=" + createTime +
                    ", userData='" + userData + '\'' +
                    ", lastAccessTime=" + lastAccessTime +
                    ", sessionAge=" + getSessionAge() + "ms" +
                    ", idleTime=" + getIdleTime() + "ms" +
                    '}';
        }
    }
    
    /**
     * 创建会话
     * @param sessionId 会话ID
     */
    public static void createSession(String sessionId) {
        SessionInfo session = new SessionInfo(sessionId);
        sessionHolder.set(session);
        System.out.println(Thread.currentThread().getName() + "：创建会话 " + sessionId);
    }
    
    /**
     * 获取当前会话
     * @return 会话信息
     */
    public static SessionInfo getCurrentSession() {
        SessionInfo session = sessionHolder.get();
        if (session != null) {
            session.updateLastAccessTime();
            System.out.println(Thread.currentThread().getName() + "：访问会话 " + session.getSessionId());
        }
        return session;
    }
    
    /**
     * 设置用户数据
     * @param userData 用户数据
     */
    public static void setUserData(String userData) {
        SessionInfo session = sessionHolder.get();
        if (session != null) {
            session.setUserData(userData);
            session.updateLastAccessTime();
            System.out.println(Thread.currentThread().getName() + "：设置用户数据到会话 " + session.getSessionId());
        }
    }
    
    /**
     * 获取用户数据
     * @return 用户数据
     */
    public static String getUserData() {
        SessionInfo session = sessionHolder.get();
        if (session != null) {
            session.updateLastAccessTime();
            return session.getUserData();
        }
        return null;
    }
    
    /**
     * 检查是否有活动会话
     * @return 是否有活动会话
     */
    public static boolean hasActiveSession() {
        return sessionHolder.get() != null;
    }
    
    /**
     * 清理会话
     */
    public static void clearSession() {
        SessionInfo session = sessionHolder.get();
        if (session != null) {
            System.out.println(Thread.currentThread().getName() + "：清理会话 " + session.getSessionId());
            sessionHolder.remove();
        }
    }
}