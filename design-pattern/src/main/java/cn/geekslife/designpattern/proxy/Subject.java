package cn.geekslife.designpattern.proxy;

/**
 * 抽象主题接口
 * 定义了RealSubject和Proxy的共同接口
 */
public interface Subject {
    /**
     * 请求方法
     */
    void request();
    
    /**
     * 获取数据方法
     * @return 数据
     */
    String getData();
}