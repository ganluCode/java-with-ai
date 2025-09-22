package cn.geekslife.designpattern.strategy;

/**
 * 无状态策略单例管理器
 * 解决策略类数量膨胀和对象创建开销问题
 */
public class StatelessStrategyManager {
    // 无状态的排序策略单例
    public static final SortStrategy BUBBLE_SORT = new BubbleSort();
    public static final SortStrategy QUICK_SORT = new QuickSort();
    public static final SortStrategy MERGE_SORT = new MergeSort();
    public static final SortStrategy SELECTION_SORT = new SelectionSort();
    
    // 无状态的促销策略单例
    public static final PromotionStrategy FREE_SHIPPING = new FreeShippingPromotion();
    
    // 私有构造函数，防止实例化
    private StatelessStrategyManager() {
    }
    
    /**
     * 获取排序策略
     * @param strategyType 策略类型
     * @return 排序策略
     */
    public static SortStrategy getSortStrategy(String strategyType) {
        switch (strategyType.toLowerCase()) {
            case "bubble":
                return BUBBLE_SORT;
            case "quick":
                return QUICK_SORT;
            case "merge":
                return MERGE_SORT;
            case "selection":
                return SELECTION_SORT;
            default:
                throw new IllegalArgumentException("未知的排序策略: " + strategyType);
        }
    }
    
    /**
     * 获取促销策略
     * @param strategyType 策略类型
     * @return 促销策略
     */
    public static PromotionStrategy getPromotionStrategy(String strategyType) {
        switch (strategyType.toLowerCase()) {
            case "free_shipping":
                return FREE_SHIPPING;
            default:
                throw new IllegalArgumentException("未知的促销策略: " + strategyType);
        }
    }
}