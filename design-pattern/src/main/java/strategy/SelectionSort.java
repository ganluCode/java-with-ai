package strategy;

/**
 * 选择排序策略
 */
public class SelectionSort implements SortStrategy {
    
    @Override
    public int[] sort(int[] array) {
        System.out.println("使用选择排序算法");
        int[] result = array.clone();
        int n = result.length;
        
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (result[j] < result[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = result[minIndex];
            result[minIndex] = result[i];
            result[i] = temp;
        }
        
        return result;
    }
    
    @Override
    public String getAlgorithmName() {
        return "选择排序";
    }
}