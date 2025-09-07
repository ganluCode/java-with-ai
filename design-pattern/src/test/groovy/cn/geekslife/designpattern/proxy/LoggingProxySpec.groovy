package cn.geekslife.designpattern.proxy

import spock.lang.Specification

/**
 * 日志代理测试类
 */
class LoggingProxySpec extends Specification {
    
    def "应该能够创建日志代理实例"() {
        given:
        com.example.proxy.LoggingProxy proxy = new com.example.proxy.LoggingProxy()
        
        expect:
        proxy != null
    }
    
    def "日志代理应该正确处理请求方法"() {
        given:
        com.example.proxy.LoggingProxy proxy = new com.example.proxy.LoggingProxy()
        
        when:
        proxy.request()
        
        then:
        // 验证方法执行不抛出异常
        noExceptionThrown()
    }
    
    def "日志代理应该正确返回数据"() {
        given:
        com.example.proxy.LoggingProxy proxy = new com.example.proxy.LoggingProxy()
        
        when:
        String data = proxy.getData()
        
        then:
        data != null
        data == "真实数据"
    }
}