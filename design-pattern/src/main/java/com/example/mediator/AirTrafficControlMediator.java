package com.example.mediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机场管制中介者
 * 模拟机场管制系统，飞机通过塔台进行协调
 */
public class AirTrafficControlMediator implements Mediator {
    private Map<String, Aircraft> aircrafts = new HashMap<>();
    private List<String> flightLogs = new ArrayList<>();
    private int runwayCount;
    private boolean[] runways; // 跑道使用状态，true表示占用，false表示空闲
    
    public AirTrafficControlMediator(int runwayCount) {
        this.runwayCount = runwayCount;
        this.runways = new boolean[runwayCount];
    }
    
    /**
     * 注册飞机到管制系统
     * @param aircraft 飞机
     */
    public void registerAircraft(Aircraft aircraft) {
        aircrafts.put(aircraft.getFlightNumber(), aircraft);
        aircraft.setMediator(this);
        String log = "飞机 " + aircraft.getFlightNumber() + " 已注册到管制系统";
        flightLogs.add(log);
        System.out.println(log);
    }
    
    /**
     * 移除飞机
     * @param aircraft 飞机
     */
    public void removeAircraft(Aircraft aircraft) {
        aircrafts.remove(aircraft.getFlightNumber());
        String log = "飞机 " + aircraft.getFlightNumber() + " 已离开管制区域";
        flightLogs.add(log);
        System.out.println(log);
    }
    
    /**
     * 获取飞行日志
     * @return 飞行日志
     */
    public List<String> getFlightLogs() {
        return new ArrayList<>(flightLogs);
    }
    
    /**
     * 清空飞行日志
     */
    public void clearFlightLogs() {
        flightLogs.clear();
    }
    
    @Override
    public void notify(Component sender, String event, Object... data) {
        if (sender instanceof Aircraft) {
            Aircraft aircraft = (Aircraft) sender;
            
            switch (event) {
                case "request_takeoff":
                    handleTakeoffRequest(aircraft);
                    break;
                    
                case "request_landing":
                    handleLandingRequest(aircraft);
                    break;
                    
                case "leave_airspace":
                    removeAircraft(aircraft);
                    broadcastMessage("管制通知: " + aircraft.getFlightNumber() + " 已离开管制区域");
                    break;
                    
                case "emergency_landing":
                    handleEmergencyLanding(aircraft);
                    break;
                    
                case "request_runway_status":
                    sendRunwayStatus(aircraft);
                    break;
                    
                default:
                    String log = "未知事件: " + event + " 来自飞机 " + aircraft.getFlightNumber();
                    flightLogs.add(log);
                    System.out.println(log);
            }
        }
    }
    
    /**
     * 处理起飞请求
     * @param aircraft 请求起飞的飞机
     */
    private void handleTakeoffRequest(Aircraft aircraft) {
        // 检查飞机是否已在空中
        if (aircraft.isInAir()) {
            String message = "起飞拒绝: " + aircraft.getFlightNumber() + " 已在空中";
            aircraft.receiveMessage(message);
            flightLogs.add(message);
            return;
        }
        
        // 寻找可用跑道
        int availableRunway = findAvailableRunway();
        if (availableRunway >= 0) {
            // 占用跑道
            runways[availableRunway] = true;
            String message = "起飞许可: " + aircraft.getFlightNumber() + " 可使用跑道 " + availableRunway;
            aircraft.receiveMessage(message);
            flightLogs.add(message);
            
            // 通知其他飞机
            broadcastMessage("管制通知: " + aircraft.getFlightNumber() + " 正在使用跑道 " + availableRunway + " 起飞");
        } else {
            String message = "起飞延迟: " + aircraft.getFlightNumber() + "，所有跑道繁忙，请等待";
            aircraft.receiveMessage(message);
            flightLogs.add(message);
        }
    }
    
    /**
     * 处理降落请求
     * @param aircraft 请求降落的飞机
     */
    private void handleLandingRequest(Aircraft aircraft) {
        // 检查飞机是否在空中
        if (!aircraft.isInAir()) {
            String message = "降落拒绝: " + aircraft.getFlightNumber() + " 已在地面";
            aircraft.receiveMessage(message);
            flightLogs.add(message);
            return;
        }
        
        // 寻找可用跑道
        int availableRunway = findAvailableRunway();
        if (availableRunway >= 0) {
            // 占用跑道
            runways[availableRunway] = true;
            String message = "降落许可: " + aircraft.getFlightNumber() + " 可使用跑道 " + availableRunway;
            aircraft.receiveMessage(message);
            flightLogs.add(message);
            
            // 通知其他飞机
            broadcastMessage("管制通知: " + aircraft.getFlightNumber() + " 正在使用跑道 " + availableRunway + " 降落");
        } else {
            String message = "降落延迟: " + aircraft.getFlightNumber() + "，所有跑道繁忙，请盘旋等待";
            aircraft.receiveMessage(message);
            flightLogs.add(message);
        }
    }
    
    /**
     * 处理紧急降落
     * @param aircraft 请求紧急降落的飞机
     */
    private void handleEmergencyLanding(Aircraft aircraft) {
        // 紧急情况下优先分配跑道
        int availableRunway = findAvailableRunway();
        if (availableRunway < 0) {
            // 如果没有可用跑道，强制清空一个跑道
            availableRunway = 0; // 简化处理，实际系统会更复杂
            String emergencyMessage = "紧急情况: " + aircraft.getFlightNumber() + " 需要紧急降落，清空跑道 " + availableRunway;
            flightLogs.add(emergencyMessage);
            System.out.println(emergencyMessage);
        }
        
        // 占用跑道
        runways[availableRunway] = true;
        String message = "紧急降落许可: " + aircraft.getFlightNumber() + " 立即使用跑道 " + availableRunway;
        aircraft.receiveMessage(message);
        flightLogs.add(message);
        
        // 通知所有飞机紧急情况
        broadcastMessage("紧急通知: " + aircraft.getFlightNumber() + " 正在紧急降落，所有飞机请注意避让");
    }
    
    /**
     * 发送跑道状态
     * @param aircraft 请求跑道状态的飞机
     */
    private void sendRunwayStatus(Aircraft aircraft) {
        StringBuilder status = new StringBuilder("跑道状态: ");
        for (int i = 0; i < runways.length; i++) {
            status.append("跑道").append(i).append(": ").append(runways[i] ? "占用" : "空闲").append(", ");
        }
        if (runways.length > 0) {
            status.delete(status.length() - 2, status.length()); // 移除最后的逗号和空格
        }
        aircraft.receiveMessage(status.toString());
    }
    
    /**
     * 寻找可用跑道
     * @return 可用跑道索引，-1表示没有可用跑道
     */
    private int findAvailableRunway() {
        for (int i = 0; i < runways.length; i++) {
            if (!runways[i]) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * 广播消息给所有飞机
     * @param message 消息内容
     */
    private void broadcastMessage(String message) {
        for (Aircraft aircraft : aircrafts.values()) {
            aircraft.receiveMessage(message);
        }
    }
    
    /**
     * 释放跑道
     * @param runwayIndex 跑道索引
     */
    public void releaseRunway(int runwayIndex) {
        if (runwayIndex >= 0 && runwayIndex < runways.length) {
            runways[runwayIndex] = false;
            String log = "跑道 " + runwayIndex + " 已释放";
            flightLogs.add(log);
            System.out.println(log);
        }
    }
    
    /**
     * 获取繁忙跑道数量
     * @return 繁忙跑道数量
     */
    public int getBusyRunwayCount() {
        int count = 0;
        for (boolean runway : runways) {
            if (runway) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 获取空闲跑道数量
     * @return 空闲跑道数量
     */
    public int getFreeRunwayCount() {
        return runwayCount - getBusyRunwayCount();
    }
}