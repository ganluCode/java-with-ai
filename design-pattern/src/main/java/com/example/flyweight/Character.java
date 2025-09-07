package com.example.flyweight;

/**
 * 文本编辑器中的字符类
 * 包含字符内容和位置等外部状态
 */
public class Character {
    // 外部状态 - 字符内容
    private char value;
    // 外部状态 - 字符位置
    private int position;
    // 外部状态 - 字符所在行
    private int line;
    // 引用享元对象
    private CharacterFlyweight flyweight;
    
    public Character(char value, int position, int line, CharacterFlyweight flyweight) {
        this.value = value;
        this.position = position;
        this.line = line;
        this.flyweight = flyweight;
    }
    
    /**
     * 显示字符
     */
    public void display() {
        // 将字符内容和位置作为外部状态传递给享元对象
        String extrinsicState = "'" + value + "' 位置(" + line + "," + position + ")";
        flyweight.operation(extrinsicState);
    }
    
    // Getter和Setter方法
    public char getValue() {
        return value;
    }
    
    public void setValue(char value) {
        this.value = value;
    }
    
    public int getPosition() {
        return position;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }
    
    public int getLine() {
        return line;
    }
    
    public void setLine(int line) {
        this.line = line;
    }
    
    public CharacterFlyweight getFlyweight() {
        return flyweight;
    }
    
    public void setFlyweight(CharacterFlyweight flyweight) {
        this.flyweight = flyweight;
    }
}