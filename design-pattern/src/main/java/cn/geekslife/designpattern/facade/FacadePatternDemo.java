package cn.geekslife.designpattern.facade;

import cn.geekslife.designpattern.facade.computer.ComputerFacade;
import cn.geekslife.designpattern.facade.database.DatabaseFacade;
import cn.geekslife.designpattern.facade.mediaplayer.MediaPlayerFacade;
import cn.geekslife.designpattern.facade.smarthome.SmartHomeFacade;

/**
 * 外观模式演示类
 */
public class FacadePatternDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 外观模式演示 ===\n");
        
        // 1. 计算机启动系统演示
        demonstrateComputerSystem();
        
        // 2. 数据库访问系统演示
        demonstrateDatabaseSystem();
        
        // 3. 多媒体播放系统演示
        demonstrateMediaPlayerSystem();
        
        // 4. 智能家居系统演示
        demonstrateSmartHomeSystem();
    }
    
    /**
     * 演示计算机启动系统
     */
    private static void demonstrateComputerSystem() {
        System.out.println("1. 计算机启动系统演示:");
        
        ComputerFacade computer = new ComputerFacade();
        computer.start();
        computer.shutdown();
    }
    
    /**
     * 演示数据库访问系统
     */
    private static void demonstrateDatabaseSystem() {
        System.out.println("2. 数据库访问系统演示:");
        
        DatabaseFacade database = new DatabaseFacade();
        database.connectToDatabase("jdbc:mysql://localhost:3306/test", "user", "password");
        database.executeSimpleQuery("SELECT * FROM users");
        database.executeTransaction(new String[]{
            "INSERT INTO users (name, email) VALUES ('张三', 'zhangsan@example.com')",
            "UPDATE users SET email = 'zhangsan_new@example.com' WHERE name = '张三'"
        });
        database.disconnect();
    }
    
    /**
     * 演示多媒体播放系统
     */
    private static void demonstrateMediaPlayerSystem() {
        System.out.println("3. 多媒体播放系统演示:");
        
        MediaPlayerFacade player = new MediaPlayerFacade();
        player.playMovie("movie.mp4", "audio.mp3", "subtitle.srt");
        player.setVolume(80);
        player.pauseMovie();
        player.resumeMovie();
        player.fullscreen();
        player.stopMovie();
    }
    
    /**
     * 演示智能家居系统
     */
    private static void demonstrateSmartHomeSystem() {
        System.out.println("4. 智能家居系统演示:");
        
        SmartHomeFacade smartHome = new SmartHomeFacade();
        
        // 激活回家模式
        smartHome.activateHomeMode();
        
        // 激活娱乐模式
        smartHome.activateEntertainmentMode();
        
        // 激活睡眠模式
        smartHome.activateSleepMode();
        
        // 激活离家模式
        smartHome.activateAwayMode();
        
        // 单独控制系统
        smartHome.controlLights("on");
        smartHome.controlAC("set", 25);
        smartHome.controlSecurity("arm");
    }
}