package cn.geekslife.designpattern.command;

/**
 * 灯光接收者类
 * 知道如何实施与执行一个请求相关的操作
 */
public class Light {
    private String location;
    private boolean isOn = false;
    
    public Light(String location) {
        this.location = location;
    }
    
    public void on() {
        isOn = true;
        System.out.println(location + " 灯已打开");
    }
    
    public void off() {
        isOn = false;
        System.out.println(location + " 灯已关闭");
    }
    
    public boolean isOn() {
        return isOn;
    }
    
    public String getLocation() {
        return location;
    }
}