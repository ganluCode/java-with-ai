package cn.geekslife.immutable;

import java.util.Objects;

/**
 * 不可变人员类 - 演示Immutable模式
 * 该类展示了如何正确实现不可变对象
 */
public final class ImmutablePerson {
    // 使用final关键字确保字段不可变
    private final String name;
    private final int age;
    private final ImmutableAddress address;
    
    /**
     * 构造函数
     * @param name 姓名
     * @param age 年龄
     * @param address 地址
     */
    public ImmutablePerson(String name, int age, ImmutableAddress address) {
        // 参数验证
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        if (age < 0) {
            throw new IllegalArgumentException("年龄不能为负数");
        }
        if (address == null) {
            throw new IllegalArgumentException("地址不能为空");
        }
        
        // 防御性拷贝 - 对于可变对象参数需要拷贝
        this.name = name;
        this.age = age;
        this.address = new ImmutableAddress(address.getStreet(), address.getCity(), address.getZipCode());
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
     * 获取地址 - 返回防御性拷贝
     * @return 地址副本
     */
    public ImmutableAddress getAddress() {
        // 返回副本以防止外部修改内部状态
        return new ImmutableAddress(address.getStreet(), address.getCity(), address.getZipCode());
    }
    
    /**
     * 创建新的Person对象并修改姓名
     * @param newName 新姓名
     * @return 新的ImmutablePerson对象
     */
    public ImmutablePerson withName(String newName) {
        return new ImmutablePerson(newName, this.age, this.address);
    }
    
    /**
     * 创建新的Person对象并修改年龄
     * @param newAge 新年龄
     * @return 新的ImmutablePerson对象
     */
    public ImmutablePerson withAge(int newAge) {
        return new ImmutablePerson(this.name, newAge, this.address);
    }
    
    /**
     * 创建新的Person对象并修改地址
     * @param newAddress 新地址
     * @return 新的ImmutablePerson对象
     */
    public ImmutablePerson withAddress(ImmutableAddress newAddress) {
        return new ImmutablePerson(this.name, this.age, newAddress);
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
        ImmutablePerson that = (ImmutablePerson) o;
        return age == that.age &&
                Objects.equals(name, that.name) &&
                Objects.equals(address, that.address);
    }
    
    /**
     * 重写hashCode方法
     * @return 哈希码
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, age, address);
    }
    
    /**
     * 重写toString方法
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return "ImmutablePerson{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                '}';
    }
}