package cn.geekslife.designpattern.prototype;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用序列化实现深克隆
 */
public class SerializationPrototype implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private List<String> tags;
    private Address address;
    
    public SerializationPrototype(String name, List<String> tags, Address address) {
        this.name = name;
        this.tags = new ArrayList<>(tags);
        this.address = address;
    }
    
    // 通过序列化实现深克隆
    public SerializationPrototype deepClone() {
        try {
            // 序列化
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            
            // 反序列化
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (SerializationPrototype) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("序列化克隆失败", e);
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
        return "SerializationPrototype{name='" + name + "', tags=" + tags + ", address=" + address + "}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SerializationPrototype that = (SerializationPrototype) obj;
        return name.equals(that.name) && tags.equals(that.tags) && address.equals(that.address);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode() * 31 + tags.hashCode() * 31 + address.hashCode();
    }
}