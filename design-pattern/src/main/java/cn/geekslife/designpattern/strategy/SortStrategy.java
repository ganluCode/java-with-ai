package cn.geekslife.designpattern.strategy;

/**
 * 抽象排序策略接口
 */
public interface SortStrategy {
    /**
     * 对数组进行排序
     * @param array 待排序数组
     * @return 排序后的数组
     */
    int[] sort(int[] array);
    
    /**
     * 获取排序算法名称
     * @return 算法名称
     */
    String getAlgorithmName();
}