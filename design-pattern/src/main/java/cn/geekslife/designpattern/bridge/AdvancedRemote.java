package cn.geekslife.designpattern.bridge;

/**
 * 高级遥控器 - 扩充抽象类
 */
public class AdvancedRemote extends RemoteControl {
    public AdvancedRemote(Device device) {
        super(device);
    }
    
    @Override
    public void mute() {
        device.setVolume(0);
        System.out.println("Device muted");
    }
    
    public void setChannel(int channel) {
        device.setChannel(channel);
    }
    
    public void playNextChannel() {
        device.setChannel(device.getChannel() + 1);
        System.out.println("Playing next channel");
    }
    
    public void playPreviousChannel() {
        device.setChannel(device.getChannel() - 1);
        System.out.println("Playing previous channel");
    }
}