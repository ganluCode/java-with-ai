package cn.geekslife.designpattern.prototype;

import java.io.Serializable;

/**
 * 地址类 - 可克隆
 */
public class Address implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private String street;
    private String city;
    
    public Address(String street, String city) {
        this.street = street;
        this.city = city;
    }
    
    public Address clone() {
        try {
            return (Address) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("地址克隆失败", e);
        }
    }
    
    // getter和setter方法
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    @Override
    public String toString() {
        return "Address{street='" + street + "', city='" + city + "'}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Address address = (Address) obj;
        return street.equals(address.street) && city.equals(address.city);
    }
    
    @Override
    public int hashCode() {
        return street.hashCode() * 31 + city.hashCode();
    }
}