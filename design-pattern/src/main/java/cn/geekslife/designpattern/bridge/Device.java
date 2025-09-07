package cn.geekslife.designpattern.bridge;

/**
 * 设备接口 - 实现类接口
 */
public interface Device {
    boolean isEnabled();
    void enable();
    void disable();
    int getVolume();
    void setVolume(int percent);
    int getChannel();
    void setChannel(int channel);
    void printStatus();
}