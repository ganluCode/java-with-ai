package cn.geekslife.designpattern.iterator

import cn.geekslife.designpattern.iterator.Iterator
import spock.lang.Specification

/**
 * 迭代器接口测试类
 */
class IteratorSpec extends Specification {
    
    def "迭代器接口应该定义基本的遍历方法"() {
        given:
        Iterator iterator = Mock(Iterator)
        
        when:
        iterator.first()
        iterator.next()
        boolean hasNext = iterator.hasNext()
        Object item = iterator.currentItem()
        
        then:
        1 * iterator.first()
        1 * iterator.next()
        1 * iterator.hasNext() >> true
        1 * iterator.currentItem() >> new Object()
    }
    
    def "迭代器接口应该定义状态检查方法"() {
        given:
        Iterator iterator = Mock(Iterator)
        
        when:
        boolean isFirst = iterator.isFirst()
        boolean isLast = iterator.isLast()
        
        then:
        1 * iterator.isFirst() >> true
        1 * iterator.isLast() >> false
    }
}