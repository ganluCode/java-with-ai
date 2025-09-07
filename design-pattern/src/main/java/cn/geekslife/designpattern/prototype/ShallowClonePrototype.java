package cn.geekslife.designpattern.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * 具体原型类 - 浅克隆示例
 */
public class ShallowClonePrototype implements Prototype, Cloneable {
    private String name;
    private int value;
    
    public ShallowClonePrototype(String name, int value) {
        this.name = name;
        this.value = value;
    }
    
    @Override
    public Prototype clone() {
        try {
            return (ShallowClonePrototype) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("克隆失败", e);
        }
    }
    
    // getter和setter方法
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "ShallowClonePrototype{name='" + name + "', value=" + value + "}";
    }
}