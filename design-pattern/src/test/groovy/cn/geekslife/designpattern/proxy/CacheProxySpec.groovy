package cn.geekslife.designpattern.proxy

import spock.lang.Specification

/**
 * 缓存代理测试类
 */
class CacheProxySpec extends Specification {
    
    def "应该能够创建缓存代理实例"() {
        given:
        com.example.proxy.CacheProxy proxy = new com.example.proxy.CacheProxy()
        
        expect:
        proxy != null
    }
    
    def "缓存代理应该在第一次访问时创建真实对象并缓存数据"() {
        given:
        com.example.proxy.CacheProxy proxy = new com.example.proxy.CacheProxy()
        
        when:
        String data = proxy.getData()
        
        then:
        data != null
        data == "真实数据"
    }
    
    def "缓存代理应该在后续访问时从缓存返回数据"() {
        given:
        com.example.proxy.CacheProxy proxy = new com.example.proxy.CacheProxy()
        
        when:
        String firstData = proxy.getData()
        String secondData = proxy.getData()
        
        then:
        firstData == secondData
        firstData == "真实数据"
    }
    
    def "缓存代理应该能够清空缓存"() {
        given:
        com.example.proxy.CacheProxy proxy = new com.example.proxy.CacheProxy()
        proxy.getData() // 填充缓存
        
        when:
        proxy.clearCache()
        
        then:
        proxy.getCacheSize() == 0
    }
    
    def "缓存代理应该正确维护缓存大小"() {
        given:
        com.example.proxy.CacheProxy proxy = new com.example.proxy.CacheProxy()
        
        when:
        proxy.getData() // 填充缓存
        
        then:
        proxy.getCacheSize() == 1
    }
}