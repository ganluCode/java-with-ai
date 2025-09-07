package cn.geekslife.designpattern.proxy

import spock.lang.Specification

/**
 * 智能引用代理测试类
 */
class SmartReferenceProxySpec extends Specification {
    
    def "应该能够创建智能引用代理实例"() {
        given:
        com.example.proxy.SmartReferenceProxy proxy = new com.example.proxy.SmartReferenceProxy()
        
        expect:
        proxy != null
    }
    
    def "智能引用代理应该正确计数访问次数"() {
        given:
        com.example.proxy.SmartReferenceProxy proxy = new com.example.proxy.SmartReferenceProxy()
        
        when:
        proxy.request()
        proxy.request()
        proxy.getData()
        
        then:
        proxy.getReferenceCount() == 3
    }
    
    def "智能引用代理应该正确返回数据"() {
        given:
        com.example.proxy.SmartReferenceProxy proxy = new com.example.proxy.SmartReferenceProxy()
        
        when:
        String data = proxy.getData()
        
        then:
        data != null
        data == "真实数据"
    }
}