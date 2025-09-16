package cn.geekslife.designpattern.mediator

import cn.geekslife.designpattern.mediator.AirTrafficControlMediator
import cn.geekslife.designpattern.mediator.Aircraft
import cn.geekslife.designpattern.mediator.ChatRoomMediator
import cn.geekslife.designpattern.mediator.User
import spock.lang.Specification

/**
 * 中介者模式核心特性综合测试类
 */
class MediatorPatternSpec extends Specification {
    
    def "中介者模式应该降低组件间的耦合度"() {
        given:
        // 测试聊天室场景
        ChatRoomMediator chatRoom = new ChatRoomMediator()
        User user1 = new User("Alice", chatRoom)
        User user2 = new User("Bob", chatRoom)
        
        when:
        // 用户不需要直接引用其他用户，只需要通过中介者通信
        chatRoom.registerUser(user1)
        chatRoom.registerUser(user2)
        user1.sendMessage("Hello")
        
        then:
        // 验证消息被正确传递
        user2.getMessages().find { it.contains("Alice: Hello") } != null
        // 验证用户之间没有直接引用
        user1.mediator == chatRoom
        user2.mediator == chatRoom
    }
    
    def "中介者模式应该集中处理复杂的交互逻辑"() {
        given:
        AirTrafficControlMediator atc = new AirTrafficControlMediator(2)
        Aircraft aircraft1 = new Aircraft("CA123", "Air China", false)
        Aircraft aircraft2 = new Aircraft("MU456", "China Eastern", false)
        Aircraft aircraft3 = new Aircraft("CZ789", "China Southern", false)
        
        when:
        // 所有飞机都通过同一个中介者注册
        atc.registerAircraft(aircraft1)
        atc.registerAircraft(aircraft2)
        atc.registerAircraft(aircraft3)
        
        // 多架飞机同时请求起飞
        aircraft1.requestTakeoff()
        aircraft2.requestTakeoff()
        aircraft3.requestTakeoff()
        
        then:
        // 验证中介者正确处理了复杂的跑道分配逻辑
        atc.getBusyRunwayCount() >= 1
        atc.getFreeRunwayCount() <= 1
        // 验证至少有一架飞机获得了起飞许可
        (aircraft1.getMessages().find { it.contains("起飞许可:") } != null ||
         aircraft2.getMessages().find { it.contains("起飞许可:") } != null ||
         aircraft3.getMessages().find { it.contains("起飞许可:") } != null) == true
    }
    
    def "中介者模式应该支持多种类型的组件"() {
        given:
        // 创建一个通用的中介者可以处理不同类型的组件
        ChatRoomMediator chatRoom = new ChatRoomMediator()
        User user = new User("Alice", chatRoom)
        
        when:
        chatRoom.registerUser(user)
        
        then:
        // 验证中介者可以处理User组件
        chatRoom.getOnlineUserCount() == 1
        chatRoom.isUserOnline("Alice") == true
    }
    
    def "中介者模式应该支持组件间的松耦合通信"() {
        given:
        ChatRoomMediator chatRoom1 = new ChatRoomMediator()
        ChatRoomMediator chatRoom2 = new ChatRoomMediator()
        User user1 = new User("Alice", chatRoom1)
        User user2 = new User("Bob", chatRoom2)
        
        when:
        // 同一个用户类可以在不同的中介者中使用
        chatRoom1.registerUser(user1)
        chatRoom2.registerUser(user2)
        user1.sendMessage("Hello from room 1")
        user2.sendMessage("Hello from room 2")
        
        then:
        // 验证用户1的消息不会发送到聊天室2
        user2.getMessages().find { it.contains("Alice: Hello from room 1") } == null
        // 验证用户2的消息不会发送到聊天室1
        user1.getMessages().find { it.contains("Bob: Hello from room 2") } == null
    }
    
    def "中介者模式应该易于扩展新的交互类型"() {
        given:
        ChatRoomMediator chatRoom = new ChatRoomMediator()
        User user1 = new User("Alice", chatRoom)
        User user2 = new User("Bob", chatRoom)
        chatRoom.registerUser(user1)
        chatRoom.registerUser(user2)
        
        when:
        // 测试新增的私信功能
        user1.sendPrivateMessage("Bob", "Private message")
        
        then:
        // 验证新功能正常工作
        user1.getMessages().find { it.contains("[私信已发送]") } != null
    }
    
    def "中介者模式应该能够处理组件生命周期"() {
        given:
        ChatRoomMediator chatRoom = new ChatRoomMediator()
        User user = new User("Alice", chatRoom)
        
        when:
        chatRoom.registerUser(user)
        int userCountAfterRegister = chatRoom.getOnlineUserCount()
        user.leaveRoom()
        int userCountAfterLeave = chatRoom.getOnlineUserCount()
        
        then:
        userCountAfterRegister == 1
        userCountAfterLeave == 0
        chatRoom.isUserOnline("Alice") == false
    }
}