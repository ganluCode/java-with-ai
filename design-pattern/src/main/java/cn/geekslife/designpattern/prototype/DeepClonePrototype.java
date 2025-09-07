package cn.geekslife.designpattern.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * 具体原型类 - 深克隆示例
 */
public class DeepClonePrototype implements Prototype, Cloneable {
    private String name;
    private List<String> tags;
    private Address address;
    
    public DeepClonePrototype(String name, List<String> tags, Address address) {
        this.name = name;
        this.tags = new ArrayList<>(tags);
        this.address = address;
    }
    
    @Override
    public Prototype clone() {
        try {
            // 浅克隆
            DeepClonePrototype cloned = (DeepClonePrototype) super.clone();
            
            // 深克隆 - 复制可变对象
            cloned.tags = new ArrayList<>(this.tags);
            
            // 深克隆 - 复制引用对象
            if (this.address != null) {
                cloned.address = this.address.clone();
            }
            
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("深克隆失败", e);
        }
    }
    
    // getter和setter方法
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    @Override
    public String toString() {
        return "DeepClonePrototype{name='" + name + "', tags=" + tags + ", address=" + address + "}";
    }
}