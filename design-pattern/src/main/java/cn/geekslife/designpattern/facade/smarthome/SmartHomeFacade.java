package cn.geekslife.designpattern.facade.smarthome;

/**
 * 智能家居控制器外观类
 */
public class SmartHomeFacade {
    private LightSystem lightSystem;
    private AirConditioningSystem acSystem;
    private SecuritySystem securitySystem;
    private EntertainmentSystem entertainmentSystem;
    private CurtainSystem curtainSystem;
    
    public SmartHomeFacade() {
        this.lightSystem = new LightSystem();
        this.acSystem = new AirConditioningSystem();
        this.securitySystem = new SecuritySystem();
        this.entertainmentSystem = new EntertainmentSystem();
        this.curtainSystem = new CurtainSystem();
    }
    
    // 一键回家模式
    public void activateHomeMode() {
        System.out.println("=== 激活回家模式 ===");
        lightSystem.turnOnAllLights();
        acSystem.turnOn();
        acSystem.setTemperature(22);
        curtainSystem.openAllCurtains();
        entertainmentSystem.turnOnTV();
        entertainmentSystem.setVolume(30);
        securitySystem.disarm();
        System.out.println("=== 回家模式激活完成 ===\n");
    }
    
    // 一键离家模式
    public void activateAwayMode() {
        System.out.println("=== 激活离家模式 ===");
        lightSystem.turnOffAllLights();
        acSystem.turnOff();
        curtainSystem.closeAllCurtains();
        entertainmentSystem.turnOffTV();
        securitySystem.arm();
        securitySystem.activateCameras();
        System.out.println("=== 离家模式激活完成 ===\n");
    }
    
    // 一键睡眠模式
    public void activateSleepMode() {
        System.out.println("=== 激活睡眠模式 ===");
        lightSystem.setBrightness(10);
        acSystem.setTemperature(18);
        curtainSystem.closeAllCurtains();
        entertainmentSystem.setVolume(10);
        System.out.println("=== 睡眠模式激活完成 ===\n");
    }
    
    // 一键娱乐模式
    public void activateEntertainmentMode() {
        System.out.println("=== 激活娱乐模式 ===");
        lightSystem.createAmbientLighting();
        acSystem.setMode("静音");
        curtainSystem.closeAllCurtains();
        entertainmentSystem.turnOnTV();
        entertainmentSystem.setVolume(50);
        entertainmentSystem.playMusic("流行");
        System.out.println("=== 娱乐模式激活完成 ===\n");
    }
    
    // 单独控制系统
    public void controlLights(String action) {
        switch (action.toLowerCase()) {
            case "on":
                lightSystem.turnOnAllLights();
                break;
            case "off":
                lightSystem.turnOffAllLights();
                break;
            case "dim":
                lightSystem.setBrightness(50);
                break;
            default:
                System.out.println("未知的灯光控制命令");
        }
    }
    
    public void controlAC(String action, int temperature) {
        switch (action.toLowerCase()) {
            case "on":
                acSystem.turnOn();
                acSystem.setTemperature(temperature);
                break;
            case "off":
                acSystem.turnOff();
                break;
            case "set":
                acSystem.setTemperature(temperature);
                break;
            default:
                System.out.println("未知的空调控制命令");
        }
    }
    
    public void controlSecurity(String action) {
        switch (action.toLowerCase()) {
            case "arm":
                securitySystem.arm();
                break;
            case "disarm":
                securitySystem.disarm();
                break;
            case "alarm":
                securitySystem.triggerAlarm();
                break;
            default:
                System.out.println("未知的安全控制命令");
        }
    }
}