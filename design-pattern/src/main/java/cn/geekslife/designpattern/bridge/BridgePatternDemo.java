package cn.geekslife.designpattern.bridge;

/**
 * 桥接模式演示类
 */
public class BridgePatternDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 桥接模式演示 ===\n");
        
        // 1. 图形绘制系统演示
        demonstrateGraphicsSystem();
        
        // 2. 设备控制系统演示
        demonstrateDeviceControlSystem();
    }
    
    /**
     * 演示图形绘制系统
     */
    private static void demonstrateGraphicsSystem() {
        System.out.println("1. 图形绘制系统演示:");
        
        // Windows平台上的图形
        Shape redCircle = new Circle(100, 100, 10, new WindowsDrawAPI());
        Shape greenRectangle = new Rectangle(10, 20, 100, 200, new WindowsDrawAPI());
        
        redCircle.draw();
        greenRectangle.draw();
        
        System.out.println();
        
        // Linux平台上的图形
        Shape blueCircle = new Circle(50, 50, 5, new LinuxDrawAPI());
        Shape yellowRectangle = new Rectangle(15, 25, 150, 250, new LinuxDrawAPI());
        
        blueCircle.draw();
        yellowRectangle.draw();
        
        System.out.println();
        
        // Mac平台上的图形
        Shape purpleCircle = new Circle(75, 75, 7, new MacDrawAPI());
        Shape orangeRectangle = new Rectangle(12, 22, 120, 220, new MacDrawAPI());
        
        purpleCircle.draw();
        orangeRectangle.draw();
        
        System.out.println();
    }
    
    /**
     * 演示设备控制系统
     */
    private static void demonstrateDeviceControlSystem() {
        System.out.println("2. 设备控制系统演示:");
        
        // 控制电视机
        System.out.println("控制电视机:");
        Device tv = new Tv();
        RemoteControl basicRemote = new BasicRemote(tv);
        
        basicRemote.togglePower();
        basicRemote.volumeUp();
        basicRemote.channelUp();
        tv.printStatus();
        
        // 控制收音机
        System.out.println("控制收音机:");
        Device radio = new Radio();
        AdvancedRemote advancedRemote = new AdvancedRemote(radio);
        
        advancedRemote.togglePower();
        advancedRemote.volumeDown();
        advancedRemote.playNextChannel();
        radio.printStatus();
        
        System.out.println();
    }
}