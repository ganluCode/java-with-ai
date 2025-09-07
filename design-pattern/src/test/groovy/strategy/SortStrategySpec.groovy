package strategy

import spock.lang.Specification

class SortStrategySpec extends Specification {

    def "should sort array correctly with bubble sort"() {
        given:
        SortContext context = new SortContext()
        SortStrategy bubbleSort = new BubbleSort()
        context.setSortStrategy(bubbleSort)
        int[] array = [64, 34, 25, 12, 22, 11, 90]
        int[] expected = [11, 12, 22, 25, 34, 64, 90]
        
        when:
        int[] result = context.executeSort(array)
        
        then:
        result == expected
    }
    
    def "should sort array correctly with quick sort"() {
        given:
        SortContext context = new SortContext()
        SortStrategy quickSort = new QuickSort()
        context.setSortStrategy(quickSort)
        int[] array = [64, 34, 25, 12, 22, 11, 90]
        int[] expected = [11, 12, 22, 25, 34, 64, 90]
        
        when:
        int[] result = context.executeSort(array)
        
        then:
        result == expected
    }
    
    def "should sort array correctly with merge sort"() {
        given:
        SortContext context = new SortContext()
        SortStrategy mergeSort = new MergeSort()
        context.setSortStrategy(mergeSort)
        int[] array = [64, 34, 25, 12, 22, 11, 90]
        int[] expected = [11, 12, 22, 25, 34, 64, 90]
        
        when:
        int[] result = context.executeSort(array)
        
        then:
        result == expected
    }
    
    def "should sort array correctly with selection sort"() {
        given:
        SortContext context = new SortContext()
        SortStrategy selectionSort = new SelectionSort()
        context.setSortStrategy(selectionSort)
        int[] array = [64, 34, 25, 12, 22, 11, 90]
        int[] expected = [11, 12, 22, 25, 34, 64, 90]
        
        when:
        int[] result = context.executeSort(array)
        
        then:
        result == expected
    }
    
    def "should return original array when no sort strategy is set"() {
        given:
        SortContext context = new SortContext()
        int[] array = [64, 34, 25, 12, 22, 11, 90]
        
        when:
        int[] result = context.executeSort(array)
        
        then:
        result == array
    }
}