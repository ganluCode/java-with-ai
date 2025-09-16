package cn.geekslife.designpattern.iterator

import cn.geekslife.designpattern.iterator.ConcreteAggregate
import cn.geekslife.designpattern.iterator.ConcreteFilterIterator
import spock.lang.Specification

/**
 * 过滤迭代器测试类
 */
class FilterIteratorSpec extends Specification {
    
    def "过滤迭代器应该支持基于条件的过滤"() {
        given:
        ConcreteAggregate<Integer> aggregate = new ConcreteAggregate<>()
        (1..10).each { aggregate.add(it) }
        
        java.util.function.Predicate<Integer> evenFilter = { it % 2 == 0 }
        ConcreteFilterIterator<Integer> iterator = new ConcreteFilterIterator<>(aggregate, evenFilter)
        
        when:
        iterator.first()
        List<Integer> results = []
        while (iterator.hasNext()) {
            results.add(iterator.currentItem())
            iterator.next()
        }
        
        then:
        results == [2, 4, 6, 8, 10]
    }
    
    def "过滤迭代器应该支持修改过滤条件"() {
        given:
        ConcreteAggregate<Integer> aggregate = new ConcreteAggregate<>()
        (1..10).each { aggregate.add(it) }
        
        ConcreteFilterIterator<Integer> iterator = new ConcreteFilterIterator<>(aggregate)
        
        when:
        java.util.function.Predicate<Integer> oddFilter = { it % 2 == 1 }
        iterator.setFilter(oddFilter)
        iterator.reset()
        iterator.first()
        List<Integer> results = []
        while (iterator.hasNext()) {
            results.add(iterator.currentItem())
            iterator.next()
        }
        
        then:
        iterator.getFilter() == oddFilter
        results == [1, 3, 5, 7, 9]
    }
    
    def "过滤迭代器应该正确处理边界条件"() {
        given:
        ConcreteAggregate<Integer> aggregate = new ConcreteAggregate<>()
        aggregate.add(1)
        aggregate.add(2)
        aggregate.add(3)
        
        java.util.function.Predicate<Integer> greaterThanFilter = { it > 1 }
        ConcreteFilterIterator<Integer> iterator = new ConcreteFilterIterator<>(aggregate, greaterThanFilter)
        
        when:
        iterator.first()
        
        then:
        iterator.isFirst() == true
        iterator.isLast() == false
        iterator.hasNext() == true
        iterator.currentItem() == 2
        
        when:
        iterator.next()
        
        then:
        iterator.isFirst() == false
        iterator.isLast() == true
        iterator.hasNext() == true
        iterator.currentItem() == 3
        
        when:
        iterator.next()
        
        then:
        iterator.isFirst() == false
        iterator.isLast() == true
        iterator.hasNext() == false
        iterator.currentItem() == null
    }
    
    def "过滤迭代器应该支持空集合"() {
        given:
        ConcreteAggregate<Integer> aggregate = new ConcreteAggregate<>()
        java.util.function.Predicate<Integer> filter = { it > 0 }
        ConcreteFilterIterator<Integer> iterator = new ConcreteFilterIterator<>(aggregate, filter)
        
        when:
        iterator.first()
        
        then:
        iterator.hasNext() == false
        iterator.isFirst() == true
        iterator.isLast() == true
    }
    
    def "过滤迭代器应该支持无匹配元素的情况"() {
        given:
        ConcreteAggregate<Integer> aggregate = new ConcreteAggregate<>()
        (1..5).each { aggregate.add(it) }
        
        java.util.function.Predicate<Integer> noMatchFilter = { it > 10 }
        ConcreteFilterIterator<Integer> iterator = new ConcreteFilterIterator<>(aggregate, noMatchFilter)
        
        when:
        iterator.first()
        
        then:
        iterator.hasNext() == false
        iterator.isFirst() == true
        iterator.isLast() == true
    }
}