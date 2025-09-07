package strategy

import spock.lang.Specification

class FunctionalSortStrategySpec extends Specification {

    def "should execute lambda sort strategy"() {
        given:
        FunctionalSortContext context = new FunctionalSortContext()
        int[] array = [64, 34, 25, 12, 22, 11, 90]
        int[] expected = [11, 12, 22, 25, 34, 64, 90]
        
        when:
        FunctionalSortStrategy lambdaSort = { arr ->
            int[] result = arr.clone()
            Arrays.sort(result)
            return result
        }
        context.setSortStrategy(lambdaSort)
        int[] result = context.executeSort(array)
        
        then:
        result == expected
    }
    
    def "should execute method reference sort strategy"() {
        given:
        FunctionalSortContext context = new FunctionalSortContext()
        int[] array = [64, 34, 25, 12, 22, 11, 90]
        int[] expected = [11, 12, 22, 25, 34, 64, 90]
        
        when:
        FunctionalSortStrategy methodRefSort = { arr ->
            int[] result = arr.clone()
            Arrays.sort(result)
            return result
        }
        context.setSortStrategy(methodRefSort)
        int[] result = context.executeSort(array)
        
        then:
        result == expected
    }
    
    def "should return original array when no strategy is set"() {
        given:
        FunctionalSortContext context = new FunctionalSortContext()
        int[] array = [64, 34, 25, 12, 22, 11, 90]
        
        when:
        int[] result = context.executeSort(array)
        
        then:
        result == array
    }
}