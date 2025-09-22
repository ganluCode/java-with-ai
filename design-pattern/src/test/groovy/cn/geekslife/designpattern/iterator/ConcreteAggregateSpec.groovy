package cn.geekslife.designpattern.iterator

import cn.geekslife.designpattern.iterator.ConcreteAggregate
import cn.geekslife.designpattern.iterator.ConcreteIterator
import cn.geekslife.designpattern.iterator.Iterator
import spock.lang.Specification

/**
 * 具体聚合测试类
 */
class ConcreteAggregateSpec extends Specification {
    
    def "具体聚合应该能够正确管理元素"() {
        given:
        ConcreteAggregate<String> aggregate = new ConcreteAggregate<>()
        
        when:
        aggregate.add("第一个元素")
        aggregate.add("第二个元素")
        aggregate.add("第三个元素")
        
        then:
        aggregate.count() == 3
        aggregate.get(0) == "第一个元素"
        aggregate.get(1) == "第二个元素"
        aggregate.get(2) == "第三个元素"
    }
    
    def "具体聚合应该能够创建迭代器"() {
        given:
        ConcreteAggregate<String> aggregate = new ConcreteAggregate<>()
        aggregate.add("测试元素")
        
        when:
        Iterator<String> iterator = aggregate.createIterator()
        
        then:
        iterator != null
        iterator instanceof ConcreteIterator
    }
    
    def "具体聚合应该能够修改元素"() {
        given:
        ConcreteAggregate<String> aggregate = new ConcreteAggregate<>()
        aggregate.add("原始元素")
        
        when:
        aggregate.set(0, "修改后的元素")
        
        then:
        aggregate.get(0) == "修改后的元素"
        aggregate.count() == 1
    }
    
    def "具体聚合应该能够移除元素"() {
        given:
        ConcreteAggregate<String> aggregate = new ConcreteAggregate<>()
        aggregate.add("要移除的元素")
        aggregate.add("保留的元素")
        
        when:
        boolean removed = aggregate.remove("要移除的元素")
        
        then:
        removed == true
        aggregate.count() == 1
        aggregate.get(0) == "保留的元素"
    }
    
    def "具体聚合应该能够清空所有元素"() {
        given:
        ConcreteAggregate<String> aggregate = new ConcreteAggregate<>()
        aggregate.add("元素1")
        aggregate.add("元素2")
        aggregate.add("元素3")
        
        when:
        aggregate.clear()
        
        then:
        aggregate.count() == 0
        aggregate.createIterator().hasNext() == false
    }
    
    def "具体聚合应该能够处理边界条件"() {
        given:
        ConcreteAggregate<String> aggregate = new ConcreteAggregate<>()
        
        when:
        String item = aggregate.get(0)
        aggregate.set(0, "测试")
        
        then:
        item == null
        notThrown(Exception)
    }
}