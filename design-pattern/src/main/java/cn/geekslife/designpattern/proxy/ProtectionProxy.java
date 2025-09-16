package cn.geekslife.designpattern.proxy;

/**
 * 保护代理类
 * 控制对原始对象的访问
 */
public class ProtectionProxy implements Subject {
    private RealSubject realSubject;
    private String role;
    
    public ProtectionProxy(String role) {
        this.role = role;
    }
    
    @Override
    public void request() {
        if (hasAccess()) {
            if (realSubject == null) {
                realSubject = new RealSubject();
            }
            realSubject.request();
        } else {
            System.out.println("权限不足，无法访问");
        }
    }
    
    @Override
    public String getData() {
        if (hasAccess()) {
            if (realSubject == null) {
                realSubject = new RealSubject();
            }
            return realSubject.getData();
        } else {
            System.out.println("权限不足，无法获取数据");
            return null;
        }
    }
    
    private boolean hasAccess() {
        return "admin".equals(role) || "user".equals(role);
    }
}