package cn.geekslife.designpattern.strategy;

/**
 * 排序上下文类
 */
public class SortContext {
    private SortStrategy sortStrategy;
    
    // 设置排序策略
    public void setSortStrategy(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }
    
    // 执行排序
    public int[] executeSort(int[] array) {
        if (sortStrategy == null) {
            System.out.println("请先选择排序算法");
            return array;
        }
        
        System.out.println("开始执行排序...使用算法: " + sortStrategy.getAlgorithmName());
        int[] result = sortStrategy.sort(array);
        System.out.println("排序完成！");
        System.out.println("---");
        return result;
    }
    
    // 获取当前排序策略
    public SortStrategy getSortStrategy() {
        return sortStrategy;
    }
}