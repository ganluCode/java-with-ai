package com.example.flyweight;

/**
 * 享元模式功能测试类
 * 验证享元模式的正确性
 */
public class FlyweightFunctionalTest {
    public static void main(String[] args) {
        System.out.println("开始享元模式功能测试...");
        
        // 测试1: 验证享元对象的复用
        System.out.println("\n=== 测试1: 享元对象复用 ===");
        CharacterFlyweightFactory.clear();
        
        // 获取相同的享元对象多次
        CharacterFlyweight flyweight1 = CharacterFlyweightFactory.getCharacterFlyweight("Arial", 12, "Black");
        CharacterFlyweight flyweight2 = CharacterFlyweightFactory.getCharacterFlyweight("Arial", 12, "Black");
        CharacterFlyweight flyweight3 = CharacterFlyweightFactory.getCharacterFlyweight("Arial", 12, "Black");
        
        System.out.println("flyweight1 == flyweight2: " + (flyweight1 == flyweight2));
        System.out.println("flyweight2 == flyweight3: " + (flyweight2 == flyweight3));
        System.out.println("享元对象池大小: " + CharacterFlyweightFactory.getFlyweightCount());
        
        // 测试2: 验证不同参数创建不同的享元对象
        System.out.println("\n=== 测试2: 不同参数创建不同对象 ===");
        CharacterFlyweight flyweight4 = CharacterFlyweightFactory.getCharacterFlyweight("Times New Roman", 14, "Red");
        CharacterFlyweight flyweight5 = CharacterFlyweightFactory.getCharacterFlyweight("Courier", 16, "Blue");
        
        System.out.println("flyweight1 == flyweight4: " + (flyweight1 == flyweight4));
        System.out.println("flyweight4 == flyweight5: " + (flyweight4 == flyweight5));
        System.out.println("享元对象池大小: " + CharacterFlyweightFactory.getFlyweightCount());
        
        // 测试3: 验证文档中字符的正确创建和显示
        System.out.println("\n=== 测试3: 文档字符处理 ===");
        Document document = new Document();
        document.addCharacter('A', 1, 1, "Arial", 12, "Black");
        document.addCharacter('B', 2, 1, "Arial", 12, "Black");
        document.addCharacter('C', 3, 1, "Times New Roman", 14, "Red");
        
        System.out.println("文档字符数: " + document.getCharacterCount());
        System.out.println("享元对象池大小: " + CharacterFlyweightFactory.getFlyweightCount());
        
        // 测试4: 验证字符显示功能
        System.out.println("\n=== 测试4: 字符显示功能 ===");
        document.display();
        
        System.out.println("\n所有测试完成！");
    }
}