package cn.geekslife.designpattern.facade.smarthome;

/**
 * 空调控制系统子系统类
 */
public class AirConditioningSystem {
    public void turnOn() {
        System.out.println("空调系统: 开启空调");
    }
    
    public void turnOff() {
        System.out.println("空调系统: 关闭空调");
    }
    
    public void setTemperature(int temperature) {
        System.out.println("空调系统: 设置温度为 " + temperature + "°C");
    }
    
    public void setMode(String mode) {
        System.out.println("空调系统: 设置模式为 " + mode);
    }
}