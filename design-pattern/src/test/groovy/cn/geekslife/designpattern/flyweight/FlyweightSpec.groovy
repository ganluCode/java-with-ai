package cn.geekslife.designpattern.flyweight

import spock.lang.Specification
import spock.lang.Subject

/**
 * 享元模式测试类
 */
class FlyweightSpec extends Specification {
    
    @Subject
    FlyweightFactory factory
    
    def setup() {
        factory = new FlyweightFactory()
    }
    
    def "应该能够创建享元工厂实例"() {
        given:
        FlyweightFactory factory = new FlyweightFactory()
        
        expect:
        factory != null
    }
    
    def "应该能够获取享元对象"() {
        given:
        String key = "testKey"
        
        when:
        Flyweight flyweight = factory.getFlyweight(key)
        
        then:
        flyweight != null
        flyweight instanceof ConcreteFlyweight
    }
    
    def "应该能够复用相同的享元对象"() {
        given:
        String key = "sharedKey"
        Flyweight flyweight1 = factory.getFlyweight(key)
        
        when:
        Flyweight flyweight2 = factory.getFlyweight(key)
        
        then:
        flyweight1.is(flyweight2)  // 检查是否是同一个对象实例
    }
    
    def "应该正确管理享元对象池大小"() {
        given:
        factory.getFlyweight("key1")
        factory.getFlyweight("key2")
        factory.getFlyweight("key1")  // 重复key，不应该增加计数
        
        expect:
        factory.getFlyweightCount() == 2
    }
    
    def "具体享元对象应该正确处理操作"() {
        given:
        String intrinsicState = "内部状态"
        String extrinsicState = "外部状态"
        ConcreteFlyweight flyweight = new ConcreteFlyweight(intrinsicState)
        
        when:
        flyweight.operation(extrinsicState)
        
        then:
        // 验证操作执行，这里主要是确保不抛出异常
        noExceptionThrown()
    }
    
    def "非共享具体享元对象应该正确处理操作"() {
        given:
        String allState = "全部状态"
        String extrinsicState = "外部状态"
        UnsharedConcreteFlyweight flyweight = new UnsharedConcreteFlyweight(allState)
        
        when:
        flyweight.operation(extrinsicState)
        
        then:
        // 验证操作执行，这里主要是确保不抛出异常
        noExceptionThrown()
    }
}