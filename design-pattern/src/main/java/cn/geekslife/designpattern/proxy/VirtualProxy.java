package cn.geekslife.designpattern.proxy;

/**
 * 虚拟代理类
 * 根据需要创建开销很大的对象
 */
public class VirtualProxy implements Subject {
    private RealSubject realSubject;
    
    @Override
    public void request() {
        if (realSubject == null) {
            realSubject = new RealSubject();
        }
        realSubject.request();
    }
    
    @Override
    public String getData() {
        if (realSubject == null) {
            realSubject = new RealSubject();
        }
        return realSubject.getData();
    }
}