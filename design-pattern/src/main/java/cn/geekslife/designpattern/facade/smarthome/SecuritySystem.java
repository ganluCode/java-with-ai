package cn.geekslife.designpattern.facade.smarthome;

/**
 * 安全系统子系统类
 */
public class SecuritySystem {
    public void arm() {
        System.out.println("安全系统: 启动安全模式");
    }
    
    public void disarm() {
        System.out.println("安全系统: 解除安全模式");
    }
    
    public void activateCameras() {
        System.out.println("安全系统: 激活摄像头");
    }
    
    public void triggerAlarm() {
        System.out.println("安全系统: 触发警报");
    }
}