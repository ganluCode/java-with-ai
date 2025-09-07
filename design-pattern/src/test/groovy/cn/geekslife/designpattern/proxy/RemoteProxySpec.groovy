package cn.geekslife.designpattern.proxy

import spock.lang.Specification

/**
 * 远程代理测试类
 */
class RemoteProxySpec extends Specification {
    
    def "应该能够创建远程代理实例"() {
        given:
        com.example.proxy.RemoteProxy proxy = new com.example.proxy.RemoteProxy()
        
        expect:
        proxy != null
    }
    
    def "远程代理应该正确处理远程请求"() {
        given:
        com.example.proxy.RemoteProxy proxy = new com.example.proxy.RemoteProxy()
        
        when:
        proxy.remoteRequest()
        
        then:
        // 验证方法执行不抛出异常
        noExceptionThrown()
    }
    
    def "远程代理应该正确获取远程数据"() {
        given:
        com.example.proxy.RemoteProxy proxy = new com.example.proxy.RemoteProxy()
        
        when:
        String data = proxy.getRemoteData()
        
        then:
        data != null
        data == "远程数据"
    }
}