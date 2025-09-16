package cn.geekslife.designpattern.mediator

import cn.geekslife.designpattern.mediator.ChatRoomMediator
import cn.geekslife.designpattern.mediator.User
import spock.lang.Specification

/**
 * 聊天室中介者模式测试类
 */
class ChatRoomMediatorSpec extends Specification {
    
    def "聊天室应该能够注册和移除用户"() {
        given:
        ChatRoomMediator chatRoom = new ChatRoomMediator()
        User user1 = new User("Alice", chatRoom)
        User user2 = new User("Bob", chatRoom)
        
        when:
        chatRoom.registerUser(user1)
        chatRoom.registerUser(user2)
        
        then:
        chatRoom.getOnlineUserCount() == 2
        chatRoom.isUserOnline("Alice") == true
        chatRoom.isUserOnline("Bob") == true
        
        when:
        user1.leaveRoom()
        
        then:
        chatRoom.getOnlineUserCount() == 1
        chatRoom.isUserOnline("Alice") == false
        chatRoom.isUserOnline("Bob") == true
    }
    
    def "用户应该能够发送和接收消息"() {
        given:
        ChatRoomMediator chatRoom = new ChatRoomMediator()
        User user1 = new User("Alice", chatRoom)
        User user2 = new User("Bob", chatRoom)
        chatRoom.registerUser(user1)
        chatRoom.registerUser(user2)
        
        when:
        user1.sendMessage("Hello everyone!")
        
        then:
        user1.getMessageCount() >= 1
        user2.getMessageCount() >= 1
        user1.getMessages().contains("我: Hello everyone!")
        user2.getMessages().find { it.contains("Alice: Hello everyone!") } != null
    }
    
    def "用户应该能够发送私信"() {
        given:
        ChatRoomMediator chatRoom = new ChatRoomMediator()
        User user1 = new User("Alice", chatRoom)
        User user2 = new User("Bob", chatRoom)
        chatRoom.registerUser(user1)
        chatRoom.registerUser(user2)
        
        when:
        user1.sendPrivateMessage("Bob", "This is a private message")
        
        then:
        user1.getMessageCount() >= 1
        user2.getMessageCount() >= 1
        user1.getMessages().find { it.contains("[私信已发送] 你对 Bob 说: This is a private message") } != null
        user2.getMessages().find { it.contains("[私信] Alice 对你说: This is a private message") } != null
    }
    
    def "用户应该能够获取在线用户列表"() {
        given:
        ChatRoomMediator chatRoom = new ChatRoomMediator()
        User user1 = new User("Alice", chatRoom)
        User user2 = new User("Bob", chatRoom)
        User user3 = new User("Charlie", chatRoom)
        chatRoom.registerUser(user1)
        chatRoom.registerUser(user2)
        chatRoom.registerUser(user3)
        
        when:
        user1.requestUserList()
        
        then:
        user1.getMessageCount() >= 1
        user1.getMessages().find { it.contains("当前在线用户: ") } != null
        user1.getMessages().find { it.contains("Alice") } != null
        user1.getMessages().find { it.contains("Bob") } != null
        user1.getMessages().find { it.contains("Charlie") } != null
    }
    
    def "聊天室应该维护聊天历史"() {
        given:
        ChatRoomMediator chatRoom = new ChatRoomMediator()
        User user1 = new User("Alice", chatRoom)
        User user2 = new User("Bob", chatRoom)
        chatRoom.registerUser(user1)
        chatRoom.registerUser(user2)
        
        when:
        user1.sendMessage("Message 1")
        user2.sendMessage("Message 2")
        user1.sendMessage("Message 3")
        
        then:
        chatRoom.getChatHistory().size() == 3
        chatRoom.getChatHistory().get(0).contains("Alice: Message 1")
        chatRoom.getChatHistory().get(1).contains("Bob: Message 2")
        chatRoom.getChatHistory().get(2).contains("Alice: Message 3")
    }
    
    def "聊天室应该能够清空聊天历史"() {
        given:
        ChatRoomMediator chatRoom = new ChatRoomMediator()
        User user1 = new User("Alice", chatRoom)
        chatRoom.registerUser(user1)
        user1.sendMessage("Test message")
        
        when:
        chatRoom.clearChatHistory()
        
        then:
        chatRoom.getChatHistory().size() == 0
    }
    
    def "向不存在的用户发送私信应该返回错误信息"() {
        given:
        ChatRoomMediator chatRoom = new ChatRoomMediator()
        User user1 = new User("Alice", chatRoom)
        chatRoom.registerUser(user1)
        
        when:
        user1.sendPrivateMessage("NonExistentUser", "Test message")
        
        then:
        user1.getMessageCount() >= 1
        user1.getMessages().find { it.contains("错误: 用户 NonExistentUser 不存在或已离线") } != null
    }
}