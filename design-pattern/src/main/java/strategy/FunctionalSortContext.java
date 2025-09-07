package strategy;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 函数式策略上下文
 */
public class FunctionalSortContext {
    private FunctionalSortStrategy sortStrategy;
    
    // 设置排序策略
    public void setSortStrategy(FunctionalSortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }
    
    // 执行排序
    public int[] executeSort(int[] array) {
        if (sortStrategy == null) {
            System.out.println("请先选择排序算法");
            return array;
        }
        
        System.out.println("开始执行函数式排序...使用算法: " + sortStrategy.getAlgorithmName());
        int[] result = sortStrategy.sort(array);
        System.out.println("排序完成！");
        System.out.println("---");
        return result;
    }
}