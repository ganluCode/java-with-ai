package com.example.flyweight;

/**
 * 字符享元类 - 模拟文本编辑器中的字符格式化
 * 在文本编辑器中，字符的字体、大小、颜色等格式信息可以作为内部状态共享
 */
public class CharacterFlyweight extends Flyweight {
    // 内部状态 - 字符的格式信息
    private String font;
    private int size;
    private String color;
    
    public CharacterFlyweight(String font, int size, String color) {
        this.font = font;
        this.size = size;
        this.color = color;
    }
    
    /**
     * 显示字符
     * @param character 字符内容（外部状态）
     * @param position 字符位置（外部状态）
     */
    @Override
    public void operation(String extrinsicState) {
        // extrinsicState 包含字符内容和位置信息
        System.out.println("字符: " + extrinsicState + 
                          ", 字体: " + font + 
                          ", 大小: " + size + 
                          ", 颜色: " + color);
    }
    
    // Getter方法
    public String getFont() {
        return font;
    }
    
    public int getSize() {
        return size;
    }
    
    public String getColor() {
        return color;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        CharacterFlyweight that = (CharacterFlyweight) obj;
        
        if (size != that.size) return false;
        if (!font.equals(that.font)) return false;
        return color.equals(that.color);
    }
    
    @Override
    public int hashCode() {
        int result = font.hashCode();
        result = 31 * result + size;
        result = 31 * result + color.hashCode();
        return result;
    }
}