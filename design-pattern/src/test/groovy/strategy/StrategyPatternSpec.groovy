package strategy

import spock.lang.Specification

class StrategyPatternSpec extends Specification {

    def "should switch between different strategies correctly"() {
        given:
        PaymentContext context = new PaymentContext()
        PaymentStrategy creditCard = new CreditCardPayment("1234-5678-9012-3456", "张三", "123", "12/25")
        PaymentStrategy alipay = new AlipayPayment("zhangsan@example.com")
        
        when:
        context.setPaymentStrategy(creditCard)
        PaymentStrategy strategy1 = context.getPaymentStrategy()
        
        context.setPaymentStrategy(alipay)
        PaymentStrategy strategy2 = context.getPaymentStrategy()
        
        then:
        strategy1 instanceof CreditCardPayment
        strategy2 instanceof AlipayPayment
    }
    
    def "should handle null strategy gracefully"() {
        given:
        PaymentContext context = new PaymentContext()
        
        when:
        PaymentStrategy strategy = context.getPaymentStrategy()
        boolean result = context.executePayment(100.0)
        
        then:
        strategy == null
        result == false
    }
    
    def "should allow strategy replacement at runtime"() {
        given:
        SortContext context = new SortContext()
        SortStrategy bubbleSort = new BubbleSort()
        SortStrategy quickSort = new QuickSort()
        int[] array = [5, 2, 8, 1, 9]
        
        when:
        context.setSortStrategy(bubbleSort)
        int[] result1 = context.executeSort(array)
        
        context.setSortStrategy(quickSort)
        int[] result2 = context.executeSort(array)
        
        then:
        result1 != null
        result2 != null
        result1 == result2 // 两种排序算法结果应该相同
    }
}