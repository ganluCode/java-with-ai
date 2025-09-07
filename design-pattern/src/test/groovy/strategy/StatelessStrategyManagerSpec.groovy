package strategy

import spock.lang.Specification

class StatelessStrategyManagerSpec extends Specification {

    def "should provide singleton sort strategies"() {
        when:
        SortStrategy bubble1 = StatelessStrategyManager.BUBBLE_SORT
        SortStrategy bubble2 = StatelessStrategyManager.BUBBLE_SORT
        SortStrategy quick1 = StatelessStrategyManager.QUICK_SORT
        SortStrategy quick2 = StatelessStrategyManager.QUICK_SORT
        
        then:
        bubble1.is(bubble2)
        quick1.is(quick2)
    }
    
    def "should get sort strategies by type"() {
        when:
        SortStrategy bubble = StatelessStrategyManager.getSortStrategy("bubble")
        SortStrategy quick = StatelessStrategyManager.getSortStrategy("quick")
        SortStrategy merge = StatelessStrategyManager.getSortStrategy("merge")
        SortStrategy selection = StatelessStrategyManager.getSortStrategy("selection")
        
        then:
        bubble instanceof BubbleSort
        quick instanceof QuickSort
        merge instanceof MergeSort
        selection instanceof SelectionSort
    }
    
    def "should get promotion strategies by type"() {
        when:
        PromotionStrategy freeShipping = StatelessStrategyManager.getPromotionStrategy("free_shipping")
        
        then:
        freeShipping instanceof FreeShippingPromotion
    }
    
    def "should throw exception for unknown strategy type"() {
        when:
        StatelessStrategyManager.getSortStrategy("unknown")
        
        then:
        thrown(IllegalArgumentException)
    }
}