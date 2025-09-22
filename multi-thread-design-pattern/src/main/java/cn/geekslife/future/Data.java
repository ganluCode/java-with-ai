package cn.geekslife.future;

/**
 * 数据接口 - Future模式中的数据载体
 */
public interface Data {
    /**
     * 获取数据内容
     * @return 数据内容
     */
    String getContent();
    
    /**
     * 检查数据是否准备就绪
     * @return 是否准备就绪
     */
    boolean isReady();
}