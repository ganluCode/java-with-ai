package cn.geekslife.immutable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 包含可变集合的不可变类
 * 演示如何处理包含集合字段的不可变对象
 */
public final class ImmutablePersonWithHobbies {
    private final String name;
    private final int age;
    private final List<String> hobbies;
    
    /**
     * 构造函数
     * @param name 姓名
     * @param age 年龄
     * @param hobbies 爱好列表
     */
    public ImmutablePersonWithHobbies(String name, int age, List<String> hobbies) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        if (age < 0) {
            throw new IllegalArgumentException("年龄不能为负数");
        }
        
        this.name = name;
        this.age = age;
        
        // 防御性拷贝 - 对于可变集合参数需要拷贝
        this.hobbies = hobbies != null ? 
            Collections.unmodifiableList(new ArrayList<>(hobbies)) : 
            Collections.emptyList();
    }
    
    /**
     * 获取姓名
     * @return 姓名
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取年龄
     * @return 年龄
     */
    public int getAge() {
        return age;
    }
    
    /**
     * 获取爱好列表 - 返回不可变副本
     * @return 爱好列表的不可变视图
     */
    public List<String> getHobbies() {
        // 返回不可变视图以防止外部修改内部状态
        return hobbies;
    }
    
    /**
     * 检查是否包含某个爱好
     * @param hobby 爱好
     * @return 是否包含
     */
    public boolean hasHobby(String hobby) {
        return hobbies.contains(hobby);
    }
    
    /**
     * 创建新的Person对象并添加爱好
     * @param newHobby 新爱好
     * @return 新的ImmutablePersonWithHobbies对象
     */
    public ImmutablePersonWithHobbies addHobby(String newHobby) {
        List<String> newHobbies = new ArrayList<>(this.hobbies);
        newHobbies.add(newHobby);
        return new ImmutablePersonWithHobbies(this.name, this.age, newHobbies);
    }
    
    /**
     * 创建新的Person对象并移除爱好
     * @param hobby 要移除的爱好
     * @return 新的ImmutablePersonWithHobbies对象
     */
    public ImmutablePersonWithHobbies removeHobby(String hobby) {
        List<String> newHobbies = new ArrayList<>(this.hobbies);
        newHobbies.remove(hobby);
        return new ImmutablePersonWithHobbies(this.name, this.age, newHobbies);
    }
    
    /**
     * 重写equals方法
     * @param o 比较对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutablePersonWithHobbies that = (ImmutablePersonWithHobbies) o;
        return age == that.age &&
                Objects.equals(name, that.name) &&
                Objects.equals(hobbies, that.hobbies);
    }
    
    /**
     * 重写hashCode方法
     * @return 哈希码
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, age, hobbies);
    }
    
    /**
     * 重写toString方法
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return "ImmutablePersonWithHobbies{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", hobbies=" + hobbies +
                '}';
    }
}