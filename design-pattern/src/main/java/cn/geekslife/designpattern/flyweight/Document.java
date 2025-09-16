package cn.geekslife.designpattern.flyweight;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档类 - 模拟文本编辑器中的文档
 */
public class Document {
    private List<Character> characters = new ArrayList<>();
    
    /**
     * 添加字符到文档中
     * @param value 字符值
     * @param position 位置
     * @param line 行号
     * @param font 字体
     * @param size 大小
     * @param color 颜色
     */
    public void addCharacter(char value, int position, int line, String font, int size, String color) {
        // 从工厂获取享元对象
        CharacterFlyweight flyweight = CharacterFlyweightFactory.getCharacterFlyweight(font, size, color);
        // 创建字符对象
        Character character = new Character(value, position, line, flyweight);
        characters.add(character);
    }
    
    /**
     * 显示文档内容
     */
    public void display() {
        System.out.println("=== 文档内容 ===");
        for (Character character : characters) {
            character.display();
        }
        System.out.println("=== 文档结束 ===");
    }
    
    /**
     * 获取文档中的字符数
     * @return 字符数
     */
    public int getCharacterCount() {
        return characters.size();
    }
}