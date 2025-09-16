package cn.geekslife.designpattern.strategy;

import java.util.Arrays;

/**
 * 策略模式排序演示类
 */
public class SortStrategyDemo {
    public static void main(String[] args) {
        System.out.println("=== 排序策略模式演示 ===");
        
        // 创建待排序数组
        int[] array = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("原始数组: " + Arrays.toString(array));
        System.out.println("---");
        
        // 创建排序上下文
        SortContext context = new SortContext();
        
        // 使用冒泡排序
        System.out.println("1. 使用冒泡排序:");
        SortStrategy bubbleSort = new BubbleSort();
        context.setSortStrategy(bubbleSort);
        int[] sortedArray1 = context.executeSort(array);
        System.out.println("排序结果: " + Arrays.toString(sortedArray1));
        
        // 使用快速排序
        System.out.println("2. 使用快速排序:");
        SortStrategy quickSort = new QuickSort();
        context.setSortStrategy(quickSort);
        int[] sortedArray2 = context.executeSort(array);
        System.out.println("排序结果: " + Arrays.toString(sortedArray2));
        
        // 使用归并排序
        System.out.println("3. 使用归并排序:");
        SortStrategy mergeSort = new MergeSort();
        context.setSortStrategy(mergeSort);
        int[] sortedArray3 = context.executeSort(array);
        System.out.println("排序结果: " + Arrays.toString(sortedArray3));
        
        // 使用选择排序
        System.out.println("4. 使用选择排序:");
        SortStrategy selectionSort = new SelectionSort();
        context.setSortStrategy(selectionSort);
        int[] sortedArray4 = context.executeSort(array);
        System.out.println("排序结果: " + Arrays.toString(sortedArray4));
    }
}