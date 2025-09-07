package com.example.iterator

import spock.lang.Specification

/**
 * 具体迭代器测试类
 */
class ConcreteIteratorSpec extends Specification {
    
    def "具体迭代器应该能够正确遍历聚合对象"() {
        given:
        com.example.iterator.ConcreteAggregate<String> aggregate = new com.example.iterator.ConcreteAggregate<>()
        aggregate.add("第一个")
        aggregate.add("第二个")
        aggregate.add("第三个")
        
        com.example.iterator.ConcreteIterator<String> iterator = new com.example.iterator.ConcreteIterator<>(aggregate)
        
        when:
        iterator.first()
        
        then:
        iterator.isFirst() == true
        iterator.hasNext() == true
        iterator.currentItem() == "第一个"
        
        when:
        iterator.next()
        
        then:
        iterator.isFirst() == false
        iterator.hasNext() == true
        iterator.currentItem() == "第二个"
        
        when:
        iterator.next()
        iterator.next()
        
        then:
        iterator.hasNext() == false
        iterator.currentItem() == null
    }
    
    def "具体迭代器应该能够正确判断边界条件"() {
        given:
        com.example.iterator.ConcreteAggregate<String> aggregate = new com.example.iterator.ConcreteAggregate<>()
        com.example.iterator.ConcreteIterator<String> iterator = new com.example.iterator.ConcreteIterator<>(aggregate)
        
        when:
        iterator.first()
        
        then:
        iterator.isFirst() == true
        iterator.isLast() == true  // 空集合时既是第一个也是最后一个
        iterator.hasNext() == false
        
        when:
        aggregate.add("唯一元素")
        iterator.first()
        
        then:
        iterator.isFirst() == true
        iterator.isLast() == true  // 只有一个元素时既是第一个也是最后一个
        iterator.hasNext() == true
        iterator.currentItem() == "唯一元素"
    }
    
    def "具体迭代器应该能够处理多个元素的边界条件"() {
        given:
        com.example.iterator.ConcreteAggregate<String> aggregate = new com.example.iterator.ConcreteAggregate<>()
        aggregate.add("第一个")
        aggregate.add("第二个")
        aggregate.add("第三个")
        
        com.example.iterator.ConcreteIterator<String> iterator = new com.example.iterator.ConcreteIterator<>(aggregate)
        
        when:
        iterator.first()
        
        then:
        iterator.isFirst() == true
        iterator.isLast() == false
        iterator.hasNext() == true
        
        when:
        iterator.next()
        iterator.next()
        
        then:
        iterator.isFirst() == false
        iterator.isLast() == true
        iterator.hasNext() == true
        
        when:
        iterator.next()
        
        then:
        iterator.isFirst() == false
        iterator.isLast() == true
        iterator.hasNext() == false
    }
}