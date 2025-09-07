package com.example.iterator

import spock.lang.Specification

/**
 * 跳过迭代器测试类
 */
class SkipIteratorSpec extends Specification {
    
    def "跳过迭代器应该支持设置步长"() {
        given:
        com.example.iterator.ConcreteAggregate<Integer> aggregate = new com.example.iterator.ConcreteAggregate<>()
        (1..10).each { aggregate.add(it) }
        
        com.example.iterator.ConcreteSkipIterator<Integer> iterator = new com.example.iterator.ConcreteSkipIterator<>(aggregate, 2)
        
        when:
        iterator.first()
        List<Integer> results = []
        while (iterator.hasNext()) {
            results.add(iterator.currentItem())
            iterator.next()
        }
        
        then:
        iterator.getStep() == 2
        results == [1, 3, 5, 7, 9]
    }
    
    def "跳过迭代器应该支持手动跳过元素"() {
        given:
        com.example.iterator.ConcreteAggregate<Integer> aggregate = new com.example.iterator.ConcreteAggregate<>()
        (1..10).each { aggregate.add(it) }
        
        com.example.iterator.ConcreteSkipIterator<Integer> iterator = new com.example.iterator.ConcreteSkipIterator<>(aggregate)
        
        when:
        iterator.first()
        iterator.skip(3)
        Integer currentItem = iterator.currentItem()
        
        then:
        currentItem == 4
    }
    
    def "跳过迭代器应该正确处理边界条件"() {
        given:
        com.example.iterator.ConcreteAggregate<Integer> aggregate = new com.example.iterator.ConcreteAggregate<>()
        (1..5).each { aggregate.add(it) }
        
        com.example.iterator.ConcreteSkipIterator<Integer> iterator = new com.example.iterator.ConcreteSkipIterator<>(aggregate, 3)
        
        when:
        iterator.first()
        
        then:
        iterator.isFirst() == true
        iterator.hasNext() == true
        iterator.currentItem() == 1
        
        when:
        iterator.next()
        
        then:
        iterator.isFirst() == false
        iterator.hasNext() == true
        iterator.currentItem() == 4
        
        when:
        iterator.next()
        
        then:
        iterator.isFirst() == false
        iterator.isLast() == true
        iterator.hasNext() == false
    }
    
    def "跳过迭代器应该支持修改步长"() {
        given:
        com.example.iterator.ConcreteAggregate<Integer> aggregate = new com.example.iterator.ConcreteAggregate<>()
        (1..10).each { aggregate.add(it) }
        
        com.example.iterator.ConcreteSkipIterator<Integer> iterator = new com.example.iterator.ConcreteSkipIterator<>(aggregate, 1)
        
        when:
        iterator.setStep(3)
        iterator.first()
        List<Integer> results = []
        while (iterator.hasNext()) {
            results.add(iterator.currentItem())
            iterator.next()
        }
        
        then:
        iterator.getStep() == 3
        results == [1, 4, 7, 10]
    }
}