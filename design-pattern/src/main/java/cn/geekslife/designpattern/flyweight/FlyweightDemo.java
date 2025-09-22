package cn.geekslife.designpattern.flyweight;

/**
 * 享元模式演示类
 * 展示享元模式在文本编辑器中的应用
 */
public class FlyweightDemo {
    public static void main(String[] args) {
        // 创建文档
        Document document = new Document();
        
        // 添加字符到文档中
        document.addCharacter('H', 1, 1, "Arial", 12, "Black");
        document.addCharacter('e', 2, 1, "Arial", 12, "Black");
        document.addCharacter('l', 3, 1, "Arial", 12, "Black");
        document.addCharacter('l', 4, 1, "Arial", 12, "Black");
        document.addCharacter('o', 5, 1, "Arial", 12, "Black");
        document.addCharacter(' ', 6, 1, "Arial", 12, "Black");
        document.addCharacter('W', 7, 1, "Arial", 12, "Black");
        document.addCharacter('o', 8, 1, "Arial", 12, "Black");
        document.addCharacter('r', 9, 1, "Arial", 12, "Black");
        document.addCharacter('l', 10, 1, "Arial", 12, "Black");
        document.addCharacter('d', 11, 1, "Arial", 12, "Black");
        document.addCharacter('!', 12, 1, "Arial", 12, "Black");
        
        // 显示文档内容
        document.display();
        
        // 输出享元对象池大小
        System.out.println("享元对象池大小: " + CharacterFlyweightFactory.getFlyweightCount());
        System.out.println("文档字符数: " + document.getCharacterCount());
    }
}