package cn.geekslife.designpattern.facade.smarthome;

/**
 * 窗帘控制系统子系统类
 */
public class CurtainSystem {
    public void openAllCurtains() {
        System.out.println("窗帘系统: 打开所有窗帘");
    }
    
    public void closeAllCurtains() {
        System.out.println("窗帘系统: 关闭所有窗帘");
    }
    
    public void openCurtain(String room) {
        System.out.println("窗帘系统: 打开 " + room + " 的窗帘");
    }
    
    public void closeCurtain(String room) {
        System.out.println("窗帘系统: 关闭 " + room + " 的窗帘");
    }
}