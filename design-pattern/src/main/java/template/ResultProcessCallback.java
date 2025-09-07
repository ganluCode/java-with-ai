package template;

/**
 * 结果处理回调接口
 */
@FunctionalInterface
public interface ResultProcessCallback {
    void onResultProcessed(Object result);
}