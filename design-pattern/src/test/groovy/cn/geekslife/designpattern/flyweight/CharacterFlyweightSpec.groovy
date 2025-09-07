package cn.geekslife.designpattern.flyweight

import spock.lang.Specification
import spock.lang.Subject

/**
 * 字符享元模式测试类 - 模拟文本编辑器场景
 */
class CharacterFlyweightSpec extends Specification {
    
    def setup() {
        // 清空享元对象池，确保测试独立性
        com.example.flyweight.CharacterFlyweightFactory.clear()
    }
    
    def "应该能够创建字符享元工厂实例"() {
        expect:
        com.example.flyweight.CharacterFlyweightFactory != null
    }
    
    def "应该能够获取字符享元对象"() {
        given:
        String font = "Arial"
        int size = 12
        String color = "Black"
        
        when:
        com.example.flyweight.CharacterFlyweight flyweight = com.example.flyweight.CharacterFlyweightFactory.getCharacterFlyweight(font, size, color)
        
        then:
        flyweight != null
        flyweight.getFont() == font
        flyweight.getSize() == size
        flyweight.getColor() == color
    }
    
    def "应该能够复用相同的字符享元对象"() {
        given:
        String font = "Arial"
        int size = 12
        String color = "Black"
        com.example.flyweight.CharacterFlyweight flyweight1 = com.example.flyweight.CharacterFlyweightFactory.getCharacterFlyweight(font, size, color)
        
        when:
        com.example.flyweight.CharacterFlyweight flyweight2 = com.example.flyweight.CharacterFlyweightFactory.getCharacterFlyweight(font, size, color)
        
        then:
        flyweight1.is(flyweight2)  // 检查是否是同一个对象实例
    }
    
    def "应该正确管理字符享元对象池大小"() {
        given:
        com.example.flyweight.CharacterFlyweightFactory.getCharacterFlyweight("Arial", 12, "Black")
        com.example.flyweight.CharacterFlyweightFactory.getCharacterFlyweight("Times New Roman", 14, "Red")
        com.example.flyweight.CharacterFlyweightFactory.getCharacterFlyweight("Arial", 12, "Black")  // 重复，不应该增加计数
        
        expect:
        com.example.flyweight.CharacterFlyweightFactory.getFlyweightCount() == 2
    }
    
    def "字符享元对象应该正确实现equals和hashCode方法"() {
        given:
        com.example.flyweight.CharacterFlyweight flyweight1 = new com.example.flyweight.CharacterFlyweight("Arial", 12, "Black")
        com.example.flyweight.CharacterFlyweight flyweight2 = new com.example.flyweight.CharacterFlyweight("Arial", 12, "Black")
        com.example.flyweight.CharacterFlyweight flyweight3 = new com.example.flyweight.CharacterFlyweight("Arial", 14, "Black")
        
        expect:
        flyweight1.equals(flyweight2)
        flyweight1.hashCode() == flyweight2.hashCode()
        !flyweight1.equals(flyweight3)
        flyweight1.hashCode() != flyweight3.hashCode()
    }
}