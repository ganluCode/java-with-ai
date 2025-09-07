package template;

/**
 * 连接回调接口
 */
@FunctionalInterface
public interface ConnectionCallback {
    void onConnected(Object connection);
}