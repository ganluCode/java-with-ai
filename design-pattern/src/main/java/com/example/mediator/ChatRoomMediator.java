package com.example.mediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天室中介者实现
 * 模拟聊天室系统，用户通过聊天室进行消息转发
 */
public class ChatRoomMediator implements Mediator {
    private Map<String, User> users = new HashMap<>();
    private List<String> chatHistory = new ArrayList<>();
    
    /**
     * 注册用户到聊天室
     * @param user 用户
     */
    public void registerUser(User user) {
        users.put(user.getName(), user);
        user.setMediator(this);
        System.out.println(user.getName() + " 加入了聊天室");
    }
    
    /**
     * 移除用户
     * @param user 用户
     */
    public void removeUser(User user) {
        users.remove(user.getName());
        System.out.println(user.getName() + " 离开了聊天室");
    }
    
    /**
     * 获取聊天历史
     * @return 聊天历史记录
     */
    public List<String> getChatHistory() {
        return new ArrayList<>(chatHistory);
    }
    
    /**
     * 清空聊天历史
     */
    public void clearChatHistory() {
        chatHistory.clear();
    }
    
    @Override
    public void notify(Component sender, String event, Object... data) {
        if (sender instanceof User) {
            User user = (User) sender;
            
            switch (event) {
                case "send_message":
                    if (data.length > 0 && data[0] instanceof String) {
                        String message = (String) data[0];
                        sendMessage(user, message);
                    }
                    break;
                    
                case "leave_room":
                    removeUser(user);
                    notifyAllUsers(user.getName() + " 离开了聊天室");
                    break;
                    
                case "request_user_list":
                    sendUserList(user);
                    break;
                    
                case "private_message":
                    if (data.length >= 2 && data[0] instanceof String && data[1] instanceof String) {
                        String targetUser = (String) data[0];
                        String message = (String) data[1];
                        sendPrivateMessage(user, targetUser, message);
                    }
                    break;
                    
                default:
                    System.out.println("未知事件: " + event);
            }
        }
    }
    
    /**
     * 发送消息给所有用户
     * @param user 发送消息的用户
     * @param message 消息内容
     */
    private void sendMessage(User user, String message) {
        String chatMessage = user.getName() + ": " + message;
        chatHistory.add(chatMessage);
        notifyAllUsers(chatMessage);
    }
    
    /**
     * 通知所有用户
     * @param message 消息内容
     */
    private void notifyAllUsers(String message) {
        for (User user : users.values()) {
            user.receiveMessage(message);
        }
    }
    
    /**
     * 发送私信
     * @param sender 发送者
     * @param targetUser 目标用户
     * @param message 消息内容
     */
    private void sendPrivateMessage(User sender, String targetUser, String message) {
        User receiver = users.get(targetUser);
        if (receiver != null) {
            String privateMessage = "[私信] " + sender.getName() + " 对你说: " + message;
            receiver.receiveMessage(privateMessage);
            
            // 同时通知发送者消息已发送
            String confirmation = "[私信已发送] 你对 " + targetUser + " 说: " + message;
            sender.receiveMessage(confirmation);
        } else {
            sender.receiveMessage("错误: 用户 " + targetUser + " 不存在或已离线");
        }
    }
    
    /**
     * 发送用户列表
     * @param user 请求用户列表的用户
     */
    private void sendUserList(User user) {
        StringBuilder userList = new StringBuilder("当前在线用户: ");
        for (String username : users.keySet()) {
            userList.append(username).append(", ");
        }
        if (users.size() > 0) {
            userList.delete(userList.length() - 2, userList.length()); // 移除最后的逗号和空格
        }
        user.receiveMessage(userList.toString());
    }
    
    /**
     * 获取在线用户数量
     * @return 在线用户数量
     */
    public int getOnlineUserCount() {
        return users.size();
    }
    
    /**
     * 检查用户是否在线
     * @param username 用户名
     * @return 是否在线
     */
    public boolean isUserOnline(String username) {
        return users.containsKey(username);
    }
}