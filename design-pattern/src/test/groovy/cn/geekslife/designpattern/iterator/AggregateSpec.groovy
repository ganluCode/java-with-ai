package cn.geekslife.designpattern.iterator

import cn.geekslife.designpattern.iterator.Aggregate
import cn.geekslife.designpattern.iterator.Iterator
import spock.lang.Specification

/**
 * 聚合接口测试类
 */
class AggregateSpec extends Specification {
    
    def "聚合接口应该定义基本的集合操作方法"() {
        given:
        Aggregate aggregate = Mock(Aggregate)
        
        when:
        Iterator iterator = aggregate.createIterator()
        int count = aggregate.count()
        
        then:
        1 * aggregate.createIterator() >> Mock(Iterator)
        1 * aggregate.count() >> 5
    }
    
    def "聚合接口应该定义元素访问和修改方法"() {
        given:
        Aggregate aggregate = Mock(Aggregate)
        Object testItem = new Object()
        
        when:
        Object item = aggregate.get(0)
        aggregate.set(0, testItem)
        aggregate.add(testItem)
        boolean removed = aggregate.remove(testItem)
        aggregate.clear()
        
        then:
        1 * aggregate.get(0) >> testItem
        1 * aggregate.set(0, testItem)
        1 * aggregate.add(testItem)
        1 * aggregate.remove(testItem) >> true
        1 * aggregate.clear()
    }
}