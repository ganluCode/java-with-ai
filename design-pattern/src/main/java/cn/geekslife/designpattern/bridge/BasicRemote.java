package cn.geekslife.designpattern.bridge;

/**
 * 基本遥控器 - 扩充抽象类
 */
public class BasicRemote extends RemoteControl {
    public BasicRemote(Device device) {
        super(device);
    }
    
    @Override
    public void mute() {
        device.setVolume(0);
        System.out.println("Device muted");
    }
}