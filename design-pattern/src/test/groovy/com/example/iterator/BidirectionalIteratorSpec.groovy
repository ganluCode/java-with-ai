package com.example.iterator

import spock.lang.Specification

/**
 * 双向迭代器测试类
 */
class BidirectionalIteratorSpec extends Specification {
    
    def "双向迭代器应该支持正向遍历"() {
        given:
        com.example.iterator.ConcreteAggregate<String> aggregate = new com.example.iterator.ConcreteAggregate<>()
        aggregate.add("A")
        aggregate.add("B")
        aggregate.add("C")
        
        com.example.iterator.ConcreteBidirectionalIterator<String> iterator = new com.example.iterator.ConcreteBidirectionalIterator<>(aggregate)
        
        when:
        iterator.first()
        List<String> forwardResults = []
        while (iterator.hasNext()) {
            forwardResults.add(iterator.currentItem())
            iterator.next()
        }
        
        then:
        forwardResults == ["A", "B", "C"]
    }
    
    def "双向迭代器应该支持反向遍历"() {
        given:
        com.example.iterator.ConcreteAggregate<String> aggregate = new com.example.iterator.ConcreteAggregate<>()
        aggregate.add("A")
        aggregate.add("B")
        aggregate.add("C")
        
        com.example.iterator.ConcreteBidirectionalIterator<String> iterator = new com.example.iterator.ConcreteBidirectionalIterator<>(aggregate)
        
        when:
        iterator.last()
        List<String> backwardResults = []
        while (iterator.hasPrevious()) {
            backwardResults.add(iterator.currentItem())
            iterator.previous()
        }
        
        then:
        backwardResults == ["C", "B", "A"]
    }
    
    def "双向迭代器应该正确处理边界条件"() {
        given:
        com.example.iterator.ConcreteAggregate<String> aggregate = new com.example.iterator.ConcreteAggregate<>()
        aggregate.add("唯一元素")
        
        com.example.iterator.ConcreteBidirectionalIterator<String> iterator = new com.example.iterator.ConcreteBidirectionalIterator<>(aggregate)
        
        when:
        iterator.first()
        
        then:
        iterator.isFirst() == true
        iterator.isLast() == true
        iterator.hasNext() == true
        iterator.hasPrevious() == false
        
        when:
        iterator.last()
        
        then:
        iterator.isFirst() == true
        iterator.isLast() == true
        iterator.hasNext() == false
        iterator.hasPrevious() == true
    }
    
    def "双向迭代器应该支持空集合"() {
        given:
        com.example.iterator.ConcreteAggregate<String> aggregate = new com.example.iterator.ConcreteAggregate<>()
        com.example.iterator.ConcreteBidirectionalIterator<String> iterator = new com.example.iterator.ConcreteBidirectionalIterator<>(aggregate)
        
        when:
        iterator.first()
        
        then:
        iterator.hasNext() == false
        iterator.hasPrevious() == false
        iterator.isFirst() == true
        iterator.isLast() == true
    }
}