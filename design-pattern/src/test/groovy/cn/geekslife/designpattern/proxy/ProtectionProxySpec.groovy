package cn.geekslife.designpattern.proxy

import spock.lang.Specification

/**
 * 保护代理测试类
 */
class ProtectionProxySpec extends Specification {
    
    def "应该能够创建保护代理实例"() {
        given:
        ProtectionProxy proxy = new ProtectionProxy("admin")
        
        expect:
        proxy != null
    }
    
    def "管理员角色应该能够访问受保护资源"() {
        given:
        ProtectionProxy proxy = new ProtectionProxy("admin")
        
        when:
        proxy.request()
        
        then:
        // 验证方法执行不抛出异常
        noExceptionThrown()
    }
    
    def "普通用户角色应该能够访问受保护资源"() {
        given:
        ProtectionProxy proxy = new ProtectionProxy("user")
        
        when:
        proxy.request()
        
        then:
        // 验证方法执行不抛出异常
        noExceptionThrown()
    }
    
    def "访客角色不应该能够访问受保护资源"() {
        given:
        ProtectionProxy proxy = new ProtectionProxy("guest")
        
        when:
        proxy.request()
        
        then:
        // 验证方法执行不抛出异常（只是打印权限不足信息）
        noExceptionThrown()
    }
    
    def "保护代理应该正确返回数据给有权限的用户"() {
        given:
        ProtectionProxy proxy = new ProtectionProxy("admin")
        
        when:
        String data = proxy.getData()
        
        then:
        data != null
        data == "真实数据"
    }
    
    def "保护代理不应该返回数据给无权限的用户"() {
        given:
        ProtectionProxy proxy = new ProtectionProxy("guest")
        
        when:
        String data = proxy.getData()
        
        then:
        data == null
    }
}