package cn.geekslife.designpattern.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户类
 * 聊天室中的具体用户组件
 */
public class User extends Component {
    private String name;
    private List<String> messages = new ArrayList<>();
    
    public User(String name, Mediator mediator) {
        super(mediator);
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * 发送消息到聊天室
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        messages.add("我: " + message);
        mediator.notify(this, "send_message", message);
    }
    
    /**
     * 发送私信
     * @param targetUser 目标用户
     * @param message 消息内容
     */
    public void sendPrivateMessage(String targetUser, String message) {
        messages.add("我私信 " + targetUser + ": " + message);
        mediator.notify(this, "private_message", targetUser, message);
    }
    
    /**
     * 离开聊天室
     */
    public void leaveRoom() {
        mediator.notify(this, "leave_room");
    }
    
    /**
     * 请求用户列表
     */
    public void requestUserList() {
        mediator.notify(this, "request_user_list");
    }
    
    /**
     * 接收消息
     * @param message 消息内容
     */
    public void receiveMessage(String message) {
        messages.add(message);
        System.out.println(name + " 收到消息: " + message);
    }
    
    /**
     * 获取消息历史
     * @return 消息历史列表
     */
    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }
    
    /**
     * 清空消息历史
     */
    public void clearMessages() {
        messages.clear();
    }
    
    /**
     * 获取消息数量
     * @return 消息数量
     */
    public int getMessageCount() {
        return messages.size();
    }
}