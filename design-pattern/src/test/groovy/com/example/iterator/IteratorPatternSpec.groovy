package com.example.iterator

import spock.lang.Specification

/**
 * 迭代器模式综合测试类
 */
class IteratorPatternSpec extends Specification {
    
    def "迭代器模式应该支持多种迭代器类型"() {
        given:
        com.example.iterator.ConcreteAggregate<String> aggregate = new com.example.iterator.ConcreteAggregate<>()
        aggregate.add("A")
        aggregate.add("B")
        aggregate.add("C")
        
        when:
        com.example.iterator.Iterator<String> basicIterator = new com.example.iterator.ConcreteIterator<>(aggregate)
        com.example.iterator.BidirectionalIterator<String> biIterator = new com.example.iterator.ConcreteBidirectionalIterator<>(aggregate)
        com.example.iterator.SkipIterator<String> skipIterator = new com.example.iterator.ConcreteSkipIterator<>(aggregate, 2)
        com.example.iterator.FilterIterator<String> filterIterator = new com.example.iterator.ConcreteFilterIterator<>(aggregate, { it == "B" })
        
        then:
        basicIterator instanceof com.example.iterator.ConcreteIterator
        biIterator instanceof com.example.iterator.ConcreteBidirectionalIterator
        skipIterator instanceof com.example.iterator.ConcreteSkipIterator
        filterIterator instanceof com.example.iterator.ConcreteFilterIterator
    }
    
    def "迭代器模式应该支持统一的遍历接口"() {
        given:
        com.example.iterator.ConcreteAggregate<Integer> aggregate = new com.example.iterator.ConcreteAggregate<>()
        (1..5).each { aggregate.add(it) }
        
        com.example.iterator.Iterator<Integer> basicIterator = new com.example.iterator.ConcreteIterator<>(aggregate)
        com.example.iterator.Iterator<Integer> skipIterator = new com.example.iterator.ConcreteSkipIterator<>(aggregate, 2)
        
        when:
        List<Integer> basicResults = collectElements(basicIterator)
        List<Integer> skipResults = collectElements(skipIterator)
        
        then:
        basicResults == [1, 2, 3, 4, 5]
        skipResults == [1, 3, 5]
    }
    
    def "迭代器模式应该支持不同类型的数据聚合"() {
        given:
        com.example.iterator.ConcreteAggregate<String> stringAggregate = new com.example.iterator.ConcreteAggregate<>()
        stringAggregate.add("字符串1")
        stringAggregate.add("字符串2")
        
        com.example.iterator.ConcreteAggregate<Integer> intAggregate = new com.example.iterator.ConcreteAggregate<>()
        intAggregate.add(1)
        intAggregate.add(2)
        intAggregate.add(3)
        
        when:
        List<String> stringResults = collectElements(stringAggregate.createIterator())
        List<Integer> intResults = collectElements(intAggregate.createIterator())
        
        then:
        stringResults == ["字符串1", "字符串2"]
        intResults == [1, 2, 3]
    }
    
    def "迭代器模式应该支持过滤和跳过功能的组合"() {
        given:
        com.example.iterator.ConcreteAggregate<Integer> aggregate = new com.example.iterator.ConcreteAggregate<>()
        (1..20).each { aggregate.add(it) }
        
        java.util.function.Predicate<Integer> evenFilter = { it % 2 == 0 }
        com.example.iterator.FilterIterator<Integer> filterIterator = new com.example.iterator.ConcreteFilterIterator<>(aggregate, evenFilter)
        com.example.iterator.SkipIterator<Integer> skipIterator = new com.example.iterator.ConcreteSkipIterator<>(aggregate, 3)
        
        when:
        filterIterator.first()
        List<Integer> evenResults = []
        while (filterIterator.hasNext()) {
            evenResults.add(filterIterator.currentItem())
            filterIterator.next()
        }
        
        skipIterator.first()
        List<Integer> skipResults = []
        while (skipIterator.hasNext()) {
            skipResults.add(skipIterator.currentItem())
            skipIterator.next()
        }
        
        then:
        evenResults == [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
        skipResults == [1, 4, 7, 10, 13, 16, 19]
    }
    
    def "迭代器模式应该支持多个迭代器同时遍历同一聚合"() {
        given:
        com.example.iterator.ConcreteAggregate<String> aggregate = new com.example.iterator.ConcreteAggregate<>()
        ["A", "B", "C", "D"].each { aggregate.add(it) }
        
        com.example.iterator.Iterator<String> iterator1 = new com.example.iterator.ConcreteIterator<>(aggregate)
        com.example.iterator.Iterator<String> iterator2 = new com.example.iterator.ConcreteIterator<>(aggregate)
        
        when:
        iterator1.first()
        iterator2.first()
        
        String item1 = iterator1.currentItem()
        iterator1.next()
        String item2 = iterator1.currentItem()
        
        String item3 = iterator2.currentItem()
        iterator2.next()
        iterator2.next()
        String item4 = iterator2.currentItem()
        
        then:
        item1 == "A"
        item2 == "B"
        item3 == "A"
        item4 == "C"
    }
    
    /**
     * 收集迭代器中的所有元素
     * @param iterator 迭代器
     * @return 元素列表
     */
    private <T> List<T> collectElements(com.example.iterator.Iterator<T> iterator) {
        List<T> results = []
        iterator.first()
        while (iterator.hasNext()) {
            results.add(iterator.currentItem())
            iterator.next()
        }
        return results
    }
}