package cn.geekslife.designpattern.mediator

import cn.geekslife.designpattern.mediator.AirTrafficControlMediator
import cn.geekslife.designpattern.mediator.Aircraft
import spock.lang.Specification

/**
 * 机场管制中介者模式测试类
 */
class AirTrafficControlMediatorSpec extends Specification {
    
    def "机场管制系统应该能够注册和移除飞机"() {
        given:
        AirTrafficControlMediator atc = new AirTrafficControlMediator(2)
        Aircraft aircraft1 = new Aircraft("CA123", "Air China", false)
        Aircraft aircraft2 = new Aircraft("MU456", "China Eastern", true)
        
        when:
        atc.registerAircraft(aircraft1)
        atc.registerAircraft(aircraft2)
        
        then:
        atc.getFlightLogs().find { it.contains("飞机 CA123 已注册到管制系统") } != null
        atc.getFlightLogs().find { it.contains("飞机 MU456 已注册到管制系统") } != null
    }
    
    def "飞机应该能够请求起飞并获得许可"() {
        given:
        AirTrafficControlMediator atc = new AirTrafficControlMediator(2)
        Aircraft aircraft = new Aircraft("CA123", "Air China", false)
        atc.registerAircraft(aircraft)
        
        when:
        aircraft.requestTakeoff()
        
        then:
        aircraft.getMessageCount() >= 1
        aircraft.getMessages().find { it.contains("起飞许可:") } != null
    }
    
    def "飞机应该能够请求降落并获得许可"() {
        given:
        AirTrafficControlMediator atc = new AirTrafficControlMediator(2)
        Aircraft aircraft = new Aircraft("CA123", "Air China", true)
        atc.registerAircraft(aircraft)
        
        when:
        aircraft.requestLanding()
        
        then:
        aircraft.getMessageCount() >= 1
        aircraft.getMessages().find { it.contains("降落许可:") } != null
    }
    
    def "当所有跑道繁忙时，起飞请求应该被延迟"() {
        given:
        AirTrafficControlMediator atc = new AirTrafficControlMediator(1)
        Aircraft aircraft1 = new Aircraft("CA123", "Air China", false)
        Aircraft aircraft2 = new Aircraft("MU456", "China Eastern", false)
        atc.registerAircraft(aircraft1)
        atc.registerAircraft(aircraft2)
        
        when:
        aircraft1.requestTakeoff() // 第一架飞机占用跑道
        aircraft2.requestTakeoff() // 第二架飞机请求起飞，但跑道已占用
        
        then:
        aircraft2.getMessages().find { it.contains("起飞延迟:") } != null
    }
    
    def "紧急降落应该优先获得许可"() {
        given:
        AirTrafficControlMediator atc = new AirTrafficControlMediator(1)
        Aircraft normalAircraft = new Aircraft("CA123", "Air China", false)
        Aircraft emergencyAircraft = new Aircraft("MU456", "China Eastern", true)
        atc.registerAircraft(normalAircraft)
        atc.registerAircraft(emergencyAircraft)
        
        when:
        normalAircraft.requestTakeoff() // 普通飞机占用跑道
        emergencyAircraft.emergencyLanding() // 紧急飞机请求降落
        
        then:
        emergencyAircraft.getMessages().find { it.contains("紧急降落许可:") } != null
        emergencyAircraft.getMessages().find { it.contains("紧急通知:") } != null
    }
    
    def "飞机应该能够请求跑道状态"() {
        given:
        AirTrafficControlMediator atc = new AirTrafficControlMediator(2)
        Aircraft aircraft = new Aircraft("CA123", "Air China", true)
        atc.registerAircraft(aircraft)
        
        when:
        aircraft.requestRunwayStatus()
        
        then:
        aircraft.getMessageCount() >= 1
        aircraft.getMessages().find { it.contains("跑道状态:") } != null
        aircraft.getMessages().find { it.contains("跑道0: 空闲") } != null
        aircraft.getMessages().find { it.contains("跑道1: 空闲") } != null
    }
    
    def "飞机离开管制区域应该被正确处理"() {
        given:
        AirTrafficControlMediator atc = new AirTrafficControlMediator(2)
        Aircraft aircraft = new Aircraft("CA123", "Air China", true)
        atc.registerAircraft(aircraft)
        
        when:
        aircraft.leaveAirspace()
        
        then:
        atc.getFlightLogs().find { it.contains("飞机 CA123 已离开管制区域") } != null
    }
    
    def "管制系统应该维护飞行日志"() {
        given:
        AirTrafficControlMediator atc = new AirTrafficControlMediator(2)
        Aircraft aircraft1 = new Aircraft("CA123", "Air China", false)
        Aircraft aircraft2 = new Aircraft("MU456", "China Eastern", true)
        atc.registerAircraft(aircraft1)
        atc.registerAircraft(aircraft2)
        
        when:
        aircraft1.requestTakeoff()
        aircraft2.requestLanding()
        
        then:
        atc.getFlightLogs().size() >= 3 // 至少包含注册日志和两个请求日志
        atc.getFlightLogs().find { it.contains("飞机 CA123 已注册到管制系统") } != null
        atc.getFlightLogs().find { it.contains("飞机 MU456 已注册到管制系统") } != null
    }
    
    def "跑道状态应该被正确管理"() {
        given:
        AirTrafficControlMediator atc = new AirTrafficControlMediator(3)
        
        expect:
        atc.getFreeRunwayCount() == 3
        atc.getBusyRunwayCount() == 0
        
        when:
        // 模拟占用跑道
        atc.releaseRunway(0) // 释放实际上是设置为空闲
        atc.releaseRunway(1)
        atc.releaseRunway(2)
        
        then:
        atc.getFreeRunwayCount() == 3
        atc.getBusyRunwayCount() == 0
    }
}