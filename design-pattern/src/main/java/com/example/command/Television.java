package com.example.command;

/**
 * 电视接收者类
 * 知道如何实施与执行一个请求相关的操作
 */
public class Television {
    private String location;
    private boolean isOn = false;
    private int channel = 1;
    private int volume = 10;
    
    public Television(String location) {
        this.location = location;
    }
    
    public void on() {
        isOn = true;
        System.out.println(location + " 电视已打开");
    }
    
    public void off() {
        isOn = false;
        System.out.println(location + " 电视已关闭");
    }
    
    public void setChannel(int channel) {
        this.channel = channel;
        System.out.println(location + " 电视频道设置为: " + channel);
    }
    
    public void setVolume(int volume) {
        this.volume = volume;
        System.out.println(location + " 电视音量设置为: " + volume);
    }
    
    public boolean isOn() {
        return isOn;
    }
    
    public int getChannel() {
        return channel;
    }
    
    public int getVolume() {
        return volume;
    }
    
    public String getLocation() {
        return location;
    }
}