package cn.geekslife.designpattern.bridge;

/**
 * 电视机实现 - 具体实现类
 */
public class Tv implements Device {
    private boolean on = false;
    private int volume = 30;
    private int channel = 1;
    
    @Override
    public boolean isEnabled() {
        return on;
    }
    
    @Override
    public void enable() {
        on = true;
        System.out.println("TV is turned on");
    }
    
    @Override
    public void disable() {
        on = false;
        System.out.println("TV is turned off");
    }
    
    @Override
    public int getVolume() {
        return volume;
    }
    
    @Override
    public void setVolume(int percent) {
        if (percent > 100) {
            volume = 100;
        } else if (percent < 0) {
            volume = 0;
        } else {
            volume = percent;
        }
        System.out.println("TV volume set to " + volume + "%");
    }
    
    @Override
    public int getChannel() {
        return channel;
    }
    
    @Override
    public void setChannel(int channel) {
        this.channel = channel;
        System.out.println("TV channel set to " + channel);
    }
    
    @Override
    public void printStatus() {
        System.out.println("------------------------------------");
        System.out.println("| I'm TV set.");
        System.out.println("| I'm " + (on ? "enabled" : "disabled"));
        System.out.println("| Current volume is " + volume + "%");
        System.out.println("| Current channel is " + channel);
        System.out.println("------------------------------------\n");
    }
}