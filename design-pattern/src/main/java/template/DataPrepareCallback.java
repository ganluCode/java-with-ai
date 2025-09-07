package template;

/**
 * 数据准备回调接口
 */
@FunctionalInterface
public interface DataPrepareCallback {
    void onDataPrepared(Object data);
}