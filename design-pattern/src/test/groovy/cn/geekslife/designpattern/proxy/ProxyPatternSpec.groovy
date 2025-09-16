package cn.geekslife.designpattern.proxy

import spock.lang.Specification

/**
 * 代理模式综合测试类
 */
class ProxyPatternSpec extends Specification {
    
    def "所有代理类型都应该实现相同的接口"() {
        given:
        Subject virtualProxy = new VirtualProxy()
        Subject protectionProxy = new ProtectionProxy("admin")
        Subject smartProxy = new SmartReferenceProxy()
        Subject cacheProxy = new CacheProxy()
        Subject loggingProxy = new LoggingProxy()
        
        expect:
        virtualProxy instanceof Subject
        protectionProxy instanceof Subject
        smartProxy instanceof Subject
        cacheProxy instanceof Subject
        loggingProxy instanceof Subject
    }
    
    def "所有代理都应该能够处理请求"() {
        given:
        Subject virtualProxy = new VirtualProxy()
        Subject protectionProxy = new ProtectionProxy("admin")
        Subject smartProxy = new SmartReferenceProxy()
        Subject cacheProxy = new CacheProxy()
        Subject loggingProxy = new LoggingProxy()
        
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
        Subject virtualProxy = new VirtualProxy()
        Subject protectionProxy = new ProtectionProxy("admin")
        Subject smartProxy = new SmartReferenceProxy()
        Subject cacheProxy = new CacheProxy()
        Subject loggingProxy = new LoggingProxy()
        
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