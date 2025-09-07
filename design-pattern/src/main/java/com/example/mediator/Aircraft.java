package com.example.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * 飞机类
 * 机场管制系统中的具体飞机组件
 */
public class Aircraft extends Component {
    private String flightNumber;
    private String airline;
    private boolean inAir;
    private int altitude; // 高度(米)
    private int speed;    // 速度(公里/小时)
    private List<String> messages = new ArrayList<>();
    
    public Aircraft(String flightNumber, String airline, boolean inAir) {
        super(null);
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.inAir = inAir;
        this.altitude = inAir ? 10000 : 0; // 在空中时默认高度10000米
        this.speed = inAir ? 800 : 0;      // 在空中时默认速度800公里/小时
    }
    
    @Override
    public String getName() {
        return flightNumber;
    }
    
    public String getFlightNumber() {
        return flightNumber;
    }
    
    public String getAirline() {
        return airline;
    }
    
    public boolean isInAir() {
        return inAir;
    }
    
    public void setInAir(boolean inAir) {
        this.inAir = inAir;
    }
    
    public int getAltitude() {
        return altitude;
    }
    
    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    /**
     * 请求起飞
     */
    public void requestTakeoff() {
        messages.add("请求起飞");
        mediator.notify(this, "request_takeoff");
    }
    
    /**
     * 请求降落
     */
    public void requestLanding() {
        messages.add("请求降落");
        mediator.notify(this, "request_landing");
    }
    
    /**
     * 紧急降落
     */
    public void emergencyLanding() {
        messages.add("紧急降落请求");
        mediator.notify(this, "emergency_landing");
    }
    
    /**
     * 离开管制区域
     */
    public void leaveAirspace() {
        messages.add("离开管制区域");
        mediator.notify(this, "leave_airspace");
    }
    
    /**
     * 请求跑道状态
     */
    public void requestRunwayStatus() {
        messages.add("请求跑道状态");
        mediator.notify(this, "request_runway_status");
    }
    
    /**
     * 接收消息
     * @param message 消息内容
     */
    public void receiveMessage(String message) {
        messages.add(message);
        System.out.println("[" + flightNumber + "] " + message);
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
    
    /**
     * 模拟起飞完成
     */
    public void takeoffCompleted() {
        inAir = true;
        altitude = 1000; // 起飞后高度1000米
        speed = 300;     // 起飞后速度300公里/小时
        messages.add("起飞完成，已升空");
        System.out.println("[" + flightNumber + "] 起飞完成，已升空");
        
        // 释放跑道（简化处理，实际系统会更复杂）
        if (mediator instanceof AirTrafficControlMediator) {
            // 这里简化处理，实际应该由中介者管理
        }
    }
    
    /**
     * 模拟降落完成
     */
    public void landingCompleted() {
        inAir = false;
        altitude = 0;
        speed = 0;
        messages.add("降落完成，已着陆");
        System.out.println("[" + flightNumber + "] 降落完成，已着陆");
        
        // 释放跑道（简化处理，实际系统会更复杂）
        if (mediator instanceof AirTrafficControlMediator) {
            // 这里简化处理，实际应该由中介者管理
        }
    }
}