package cn.geekslife.designpattern.strategy;

/**
 * 冒泡排序策略
 */
public class BubbleSort implements SortStrategy {
    
    @Override
    public int[] sort(int[] array) {
        System.out.println("使用冒泡排序算法");
        int[] result = array.clone();
        int n = result.length;
        
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (result[j] > result[j + 1]) {
                    // 交换元素
                    int temp = result[j];
                    result[j] = result[j + 1];
                    result[j + 1] = temp;
                }
            }
        }
        
        return result;
    }
    
    @Override
    public String getAlgorithmName() {
        return "冒泡排序";
    }
}