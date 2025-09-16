package cn.geekslife.designpattern.proxy

import spock.lang.Specification

/**
 * 虚拟代理测试类
 */
class VirtualProxySpec extends Specification {
    
    def "应该能够创建虚拟代理实例"() {
        given:
        VirtualProxy proxy = new VirtualProxy()
        
        expect:
        proxy != null
    }
    
    def "虚拟代理应该延迟创建真实对象"() {
        given:
        VirtualProxy proxy = new VirtualProxy()
        
        when:
        // 通过反射检查真实对象是否为null
        def realSubjectField = VirtualProxy.getDeclaredField("realSubject")
        realSubjectField.setAccessible(true)
        def realSubject = realSubjectField.get(proxy)
        
        then:
        realSubject == null
    }
    
    def "虚拟代理应该在需要时创建真实对象"() {
        given:
        VirtualProxy proxy = new VirtualProxy()
        
        when:
        proxy.request()
        
        then:
        // 验证方法执行不抛出异常
        noExceptionThrown()
    }
    
    def "虚拟代理应该正确返回数据"() {
        given:
        VirtualProxy proxy = new VirtualProxy()
        
        when:
        String data = proxy.getData()
        
        then:
        data != null
        data == "真实数据"
    }
}