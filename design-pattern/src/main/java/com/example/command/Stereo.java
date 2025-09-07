package com.example.command;

/**
 * 音响接收者类
 * 知道如何实施与执行一个请求相关的操作
 */
public class Stereo {
    private String location;
    private boolean isOn = false;
    private int volume = 5;
    private String mode = "CD";
    
    public Stereo(String location) {
        this.location = location;
    }
    
    public void on() {
        isOn = true;
        System.out.println(location + " 音响已打开");
    }
    
    public void off() {
        isOn = false;
        System.out.println(location + " 音响已关闭");
    }
    
    public void setCD() {
        mode = "CD";
        System.out.println(location + " 音响模式设置为CD");
    }
    
    public void setDVD() {
        mode = "DVD";
        System.out.println(location + " 音响模式设置为DVD");
    }
    
    public void setRadio() {
        mode = "Radio";
        System.out.println(location + " 音响模式设置为Radio");
    }
    
    public void setVolume(int volume) {
        this.volume = volume;
        System.out.println(location + " 音响音量设置为: " + volume);
    }
    
    public boolean isOn() {
        return isOn;
    }
    
    public int getVolume() {
        return volume;
    }
    
    public String getMode() {
        return mode;
    }
    
    public String getLocation() {
        return location;
    }
}