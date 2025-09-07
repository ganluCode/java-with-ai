package cn.geekslife.immutable;

import java.util.Objects;

/**
 * 可变人员类 - 用于对比演示Immutable模式的优势
 */
public class MutablePerson {
    // 非final字段，可被修改
    private String name;
    private int age;
    private MutableAddress address;
    
    /**
     * 构造函数
     * @param name 姓名
     * @param age 年龄
     * @param address 地址
     */
    public MutablePerson(String name, int age, MutableAddress address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }
    
    /**
     * 获取姓名
     * @return 姓名
     */
    public String getName() {
        return name;
    }
    
    /**
     * 设置姓名
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 获取年龄
     * @return 年龄
     */
    public int getAge() {
        return age;
    }
    
    /**
     * 设置年龄
     * @param age 年龄
     */
    public void setAge(int age) {
        this.age = age;
    }
    
    /**
     * 获取地址 - 直接返回引用，不安全
     * @return 地址
     */
    public MutableAddress getAddress() {
        return address;
    }
    
    /**
     * 设置地址
     * @param address 地址
     */
    public void setAddress(MutableAddress address) {
        this.address = address;
    }
    
    /**
     * 重写toString方法
     * @return 字符串表示
     */
    @Override
    public String toString() {
        return "MutablePerson{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                '}';
    }
    
    /**
     * 可变地址类 - 作为MutablePerson的组成部分
     */
    public static class MutableAddress {
        private String street;
        private String city;
        private String zipCode;
        
        /**
         * 构造函数
         * @param street 街道
         * @param city 城市
         * @param zipCode 邮编
         */
        public MutableAddress(String street, String city, String zipCode) {
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
         * 设置街道
         * @param street 街道
         */
        public void setStreet(String street) {
            this.street = street;
        }
        
        /**
         * 获取城市
         * @return 城市
         */
        public String getCity() {
            return city;
        }
        
        /**
         * 设置城市
         * @param city 城市
         */
        public void setCity(String city) {
            this.city = city;
        }
        
        /**
         * 获取邮编
         * @return 邮编
         */
        public String getZipCode() {
            return zipCode;
        }
        
        /**
         * 设置邮编
         * @param zipCode 邮编
         */
        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }
        
        /**
         * 重写toString方法
         * @return 字符串表示
         */
        @Override
        public String toString() {
            return "MutableAddress{" +
                    "street='" + street + '\'' +
                    ", city='" + city + '\'' +
                    ", zipCode='" + zipCode + '\'' +
                    '}';
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
            MutableAddress that = (MutableAddress) o;
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
    }
}