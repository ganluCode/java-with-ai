package cn.geekslife.immutable;

import java.util.Objects;

/**
 * 不可变地址类 - 作为ImmutablePerson的组成部分
 */
public final class ImmutableAddress {
    // 使用final关键字确保字段不可变
    private final String street;
    private final String city;
    private final String zipCode;
    
    /**
     * 构造函数
     * @param street 街道
     * @param city 城市
     * @param zipCode 邮编
     */
    public ImmutableAddress(String street, String city, String zipCode) {
        // 参数验证
        if (street == null || street.isEmpty()) {
            throw new IllegalArgumentException("街道不能为空");
        }
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("城市不能为空");
        }
        if (zipCode == null || zipCode.isEmpty()) {
            throw new IllegalArgumentException("邮编不能为空");
        }
        
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }
    
    /**
     * 获取街道
     * @return 街道
     */
    public String getStreet() {
        return street;
    }
    
    /**
     * 获取城市
     * @return 城市
     */
    public String getCity() {
        return city;
    }
    
    /**
     * 获取邮编
     * @return 邮编
     */
    public String getZipCode() {
        return zipCode;
    }
    
    /**
     * 创建新的Address对象并修改街道
     * @param newStreet 新街道
     * @return 新的ImmutableAddress对象
     */
    public ImmutableAddress withStreet(String newStreet) {
        return new ImmutableAddress(newStreet, this.city, this.zipCode);
    }
    
    /**
     * 创建新的Address对象并修改城市
     * @param newCity 新城市
     * @return 新的ImmutableAddress对象
     */
    public ImmutableAddress withCity(String newCity) {
        return new ImmutableAddress(this.street, newCity, this.zipCode);
    }
    
    /**
     * 创建新的Address对象并修改邮编
     * @param newZipCode 新邮编
     * @return 新的ImmutableAddress对象
     */
    public ImmutableAddress withZipCode(String newZipCode) {
        return new ImmutableAddress(this.street, this.city, newZipCode);
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
        ImmutableAddress that = (ImmutableAddress) o;
        return Objects.equals(street, that.street) &&
                Objects.equals(city, that.city) &&
                Objects.equals(zipCode, that.zipCode);
    }
    
    /**
     * 重写hashCode方法
     * @return 哈希码
     */
    @Override
    public int hashCode() {
        return Objects.hash(street, city, zipCode);
    }
    
    /**
     * 重写toString方法
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return "ImmutableAddress{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}