package cn.geekslife.designpattern.singleton;

/**
 * 枚举单例模式
 * 最安全的单例实现方式，防止反射和序列化攻击
 */
public enum EnumSingleton {
    INSTANCE;
    
    public void doSomething() {
        System.out.println("枚举单例执行业务逻辑");
    }
    
    public void doAnotherThing() {
        System.out.println("枚举单例执行其他业务逻辑");
    }
}