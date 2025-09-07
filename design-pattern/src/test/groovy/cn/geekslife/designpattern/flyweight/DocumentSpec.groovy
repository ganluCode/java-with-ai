package cn.geekslife.designpattern.flyweight

import spock.lang.Specification
import spock.lang.Subject

/**
 * 文档类测试 - 验证享元模式在文本编辑器中的应用
 */
class DocumentSpec extends Specification {
    
    def setup() {
        // 清空享元对象池，确保测试独立性
        com.example.flyweight.CharacterFlyweightFactory.clear()
    }
    
    def "应该能够创建文档实例"() {
        given:
        com.example.flyweight.Document document = new com.example.flyweight.Document()
        
        expect:
        document != null
    }
    
    def "应该能够向文档中添加字符"() {
        given:
        com.example.flyweight.Document document = new com.example.flyweight.Document()
        
        when:
        document.addCharacter((char)72, 1, 1, "Arial", 12, "Black")  // 'H'的ASCII码
        document.addCharacter((char)101, 2, 1, "Arial", 12, "Black")  // 'e'的ASCII码
        document.addCharacter((char)108, 3, 1, "Arial", 12, "Black")  // 'l'的ASCII码
        
        then:
        document.getCharacterCount() == 3
    }
    
    def "文档应该能够正确显示内容"() {
        given:
        com.example.flyweight.Document document = new com.example.flyweight.Document()
        document.addCharacter((char)72, 1, 1, "Arial", 12, "Black")  // 'H'的ASCII码
        document.addCharacter((char)101, 2, 1, "Arial", 12, "Black")  // 'e'的ASCII码
        
        when:
        document.display()
        
        then:
        // 验证操作执行，这里主要是确保不抛出异常
        noExceptionThrown()
    }
    
    def "文档中的相同格式字符应该共享享元对象"() {
        given:
        com.example.flyweight.Document document = new com.example.flyweight.Document()
        
        when:
        // 添加多个相同格式的字符
        document.addCharacter((char)72, 1, 1, "Arial", 12, "Black")  // 'H'的ASCII码
        document.addCharacter((char)101, 2, 1, "Arial", 12, "Black")  // 'e'的ASCII码
        document.addCharacter((char)108, 3, 1, "Arial", 12, "Black")  // 'l'的ASCII码
        document.addCharacter((char)108, 4, 1, "Arial", 12, "Black")  // 'l'的ASCII码
        document.addCharacter((char)111, 5, 1, "Arial", 12, "Black")  // 'o'的ASCII码
        
        then:
        // 验证享元对象池中只有一个对象
        com.example.flyweight.CharacterFlyweightFactory.getFlyweightCount() == 1
        document.getCharacterCount() == 5
    }
    
    def "文档中的不同格式字符应该创建不同的享元对象"() {
        given:
        com.example.flyweight.Document document = new com.example.flyweight.Document()
        
        when:
        // 添加不同格式的字符
        document.addCharacter((char)72, 1, 1, "Arial", 12, "Black")  // 'H'的ASCII码
        document.addCharacter((char)101, 2, 1, "Times New Roman", 14, "Red")  // 'e'的ASCII码
        document.addCharacter((char)108, 3, 1, "Courier", 16, "Blue")  // 'l'的ASCII码
        
        then:
        // 验证享元对象池中有三个对象
        com.example.flyweight.CharacterFlyweightFactory.getFlyweightCount() == 3
        document.getCharacterCount() == 3
    }
}