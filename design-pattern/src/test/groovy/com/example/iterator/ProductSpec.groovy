package com.example.iterator

import spock.lang.Specification

/**
 * 产品类测试类
 */
class ProductSpec extends Specification {
    
    def "产品类应该正确初始化和访问属性"() {
        given:
        com.example.iterator.Product product = new com.example.iterator.Product("笔记本电脑", "电子产品", 5999.0, 10)
        
        expect:
        product.getName() == "笔记本电脑"
        product.getCategory() == "电子产品"
        product.getPrice() == 5999.0
        product.getStock() == 10
    }
    
    def "产品类应该支持属性修改"() {
        given:
        com.example.iterator.Product product = new com.example.iterator.Product("笔记本电脑", "电子产品", 5999.0, 10)
        
        when:
        product.setName("台式机")
        product.setCategory("电脑")
        product.setPrice(3999.0)
        product.setStock(5)
        
        then:
        product.getName() == "台式机"
        product.getCategory() == "电脑"
        product.getPrice() == 3999.0
        product.getStock() == 5
    }
    
    def "产品类应该正确实现equals和hashCode方法"() {
        given:
        com.example.iterator.Product product1 = new com.example.iterator.Product("笔记本电脑", "电子产品", 5999.0, 10)
        com.example.iterator.Product product2 = new com.example.iterator.Product("笔记本电脑", "电子产品", 5999.0, 10)
        com.example.iterator.Product product3 = new com.example.iterator.Product("手机", "电子产品", 3999.0, 20)
        
        expect:
        product1.equals(product2)
        product1.hashCode() == product2.hashCode()
        !product1.equals(product3)
        product1.hashCode() != product3.hashCode()
        product1.equals(product1)  // 自反性
        !product1.equals(null)     // 空值比较
    }
    
    def "产品类应该正确实现toString方法"() {
        given:
        com.example.iterator.Product product = new com.example.iterator.Product("笔记本电脑", "电子产品", 5999.0, 10)
        
        when:
        String str = product.toString()
        
        then:
        str.contains("笔记本电脑")
        str.contains("电子产品")
        str.contains("5999.0")
        str.contains("10")
    }
}