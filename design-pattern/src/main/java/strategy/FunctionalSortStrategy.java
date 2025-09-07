package strategy;

/**
 * 函数式策略接口 - 使用函数式编程替代策略类
 */
@FunctionalInterface
public interface FunctionalSortStrategy {
    /**
     * 对数组进行排序
     * @param array 待排序数组
     * @return 排序后的数组
     */
    int[] sort(int[] array);
    
    /**
     * 默认方法：获取算法名称
     * @return 算法名称
     */
    default String getAlgorithmName() {
        return "函数式排序";
    }
}