package cn.geekslife.designpattern.proxy

import spock.lang.Specification

/**
 * 代理模式综合测试类
 */
class ProxyPatternSpec extends Specification {
    
    def "所有代理类型都应该实现相同的接口"() {
        given:
        com.example.proxy.Subject virtualProxy = new com.example.proxy.VirtualProxy()
        com.example.proxy.Subject protectionProxy = new com.example.proxy.ProtectionProxy("admin")
        com.example.proxy.Subject smartProxy = new com.example.proxy.SmartReferenceProxy()
        com.example.proxy.Subject cacheProxy = new com.example.proxy.CacheProxy()
        com.example.proxy.Subject loggingProxy = new com.example.proxy.LoggingProxy()
        
        expect:
        virtualProxy instanceof com.example.proxy.Subject
        protectionProxy instanceof com.example.proxy.Subject
        smartProxy instanceof com.example.proxy.Subject
        cacheProxy instanceof com.example.proxy.Subject
        loggingProxy instanceof com.example.proxy.Subject
    }
    
    def "所有代理都应该能够处理请求"() {
        given:
        com.example.proxy.Subject virtualProxy = new com.example.proxy.VirtualProxy()
        com.example.proxy.Subject protectionProxy = new com.example.proxy.ProtectionProxy("admin")
        com.example.proxy.Subject smartProxy = new com.example.proxy.SmartReferenceProxy()
        com.example.proxy.Subject cacheProxy = new com.example.proxy.CacheProxy()
        com.example.proxy.Subject loggingProxy = new com.example.proxy.LoggingProxy()
        
        when:
        virtualProxy.request()
        protectionProxy.request()
        smartProxy.request()
        cacheProxy.request()
        loggingProxy.request()
        
        then:
        // 验证所有方法执行不抛出异常
        noExceptionThrown()
    }
    
    def "所有代理都应该能够返回数据"() {
        given:
        com.example.proxy.Subject virtualProxy = new com.example.proxy.VirtualProxy()
        com.example.proxy.Subject protectionProxy = new com.example.proxy.ProtectionProxy("admin")
        com.example.proxy.Subject smartProxy = new com.example.proxy.SmartReferenceProxy()
        com.example.proxy.Subject cacheProxy = new com.example.proxy.CacheProxy()
        com.example.proxy.Subject loggingProxy = new com.example.proxy.LoggingProxy()
        
        when:
        String virtualData = virtualProxy.getData()
        String protectionData = protectionProxy.getData()
        String smartData = smartProxy.getData()
        String cacheData = cacheProxy.getData()
        String loggingData = loggingProxy.getData()
        
        then:
        virtualData != null
        protectionData != null
        smartData != null
        cacheData != null
        loggingData != null
    }
}