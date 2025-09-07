package cn.geekslife.activeobject;

/**
 * 方法请求抽象类 - Active Object模式中的命令对象
 */
public abstract class MethodRequest {
    /**
     * 执行方法请求
     */
    public abstract void execute();
}