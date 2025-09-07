package cn.geekslife.designpattern.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档样式原型
 * 演示原型模式在实际应用中的使用
 */
public class DocumentStyle implements Prototype, Cloneable {
    private String fontFamily;
    private int fontSize;
    private String color;
    private List<String> effects;
    
    public DocumentStyle(String fontFamily, int fontSize, String color) {
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.color = color;
        this.effects = new ArrayList<>();
    }
    
    @Override
    public Prototype clone() {
        try {
            DocumentStyle cloned = (DocumentStyle) super.clone();
            cloned.effects = new ArrayList<>(this.effects);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("样式克隆失败", e);
        }
    }
    
    // 添加特效
    public DocumentStyle addEffect(String effect) {
        this.effects.add(effect);
        return this;
    }
    
    // 移除特效
    public DocumentStyle removeEffect(String effect) {
        this.effects.remove(effect);
        return this;
    }
    
    // getter和setter方法
    public String getFontFamily() {
        return fontFamily;
    }
    
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }
    
    public int getFontSize() {
        return fontSize;
    }
    
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public List<String> getEffects() {
        return new ArrayList<>(effects); // 返回副本以保护内部状态
    }
    
    public void setEffects(List<String> effects) {
        this.effects = new ArrayList<>(effects);
    }
    
    @Override
    public String toString() {
        return "DocumentStyle{fontFamily='" + fontFamily + "', fontSize=" + fontSize + 
               ", color='" + color + "', effects=" + effects + "}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DocumentStyle that = (DocumentStyle) obj;
        return fontSize == that.fontSize && 
               fontFamily.equals(that.fontFamily) && 
               color.equals(that.color) && 
               effects.equals(that.effects);
    }
    
    @Override
    public int hashCode() {
        int result = fontFamily.hashCode();
        result = 31 * result + fontSize;
        result = 31 * result + color.hashCode();
        result = 31 * result + effects.hashCode();
        return result;
    }
}