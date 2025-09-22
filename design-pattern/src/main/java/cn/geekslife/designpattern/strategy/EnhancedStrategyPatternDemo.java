package cn.geekslife.designpattern.strategy;

import java.util.Arrays;

/**
 * 策略模式增强特性演示类
 */
public class EnhancedStrategyPatternDemo {
    public static void main(String[] args) {
        System.out.println("=== 增强版策略模式演示 ===");
        
        System.out.println("\n1. 使用策略工厂:");
        StrategyFactory factory = StrategyFactory.getInstance();
        PaymentContext paymentContext = new PaymentContext();
        
        // 使用工厂获取支付策略
        PaymentStrategy alipay = factory.getPaymentStrategy("alipay");
        paymentContext.setPaymentStrategy(alipay);
        paymentContext.executePayment(200.0);
        
        System.out.println("\n2. 使用策略选择器:");
        // 根据金额自动选择支付策略
        PaymentStrategy autoSelected = StrategySelector.selectPaymentStrategy(500.0);
        paymentContext.setPaymentStrategy(autoSelected);
        paymentContext.executePayment(500.0);
        
        // 根据数组大小自动选择排序策略
        SortContext sortContext = new SortContext();
        SortStrategy autoSort = StrategySelector.selectSortStrategy(50);
        sortContext.setSortStrategy(autoSort);
        int[] array = {64, 34, 25, 12, 22, 11, 90};
        sortContext.executeSort(array);
        
        System.out.println("\n3. 使用无状态策略单例:");
        // 使用单例策略
        SortStrategy singletonSort = StatelessStrategyManager.getSortStrategy("quick");
        sortContext.setSortStrategy(singletonSort);
        sortContext.executeSort(array);
        
        System.out.println("\n4. 使用函数式策略:");
        FunctionalSortContext functionalContext = new FunctionalSortContext();
        
        // 使用Lambda表达式作为策略
        FunctionalSortStrategy lambdaSort = (arr) -> {
            System.out.println("使用Lambda表达式排序");
            int[] result = arr.clone();
            Arrays.sort(result);
            return result;
        };
        functionalContext.setSortStrategy(lambdaSort);
        functionalContext.executeSort(array);
        
        // 使用方法引用作为策略
        FunctionalSortStrategy methodRefSort = (arr) -> {
            System.out.println("使用方法引用排序");
            int[] result = arr.clone();
            Arrays.sort(result);
            return result;
        };
        functionalContext.setSortStrategy(methodRefSort);
        functionalContext.executeSort(array);
    }
}