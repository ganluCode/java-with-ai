package cn.geekslife.designpattern.template;

/**
 * 操作执行回调接口
 */
@FunctionalInterface
public interface OperationCallback {
    void onOperationExecuted(Object result);
}