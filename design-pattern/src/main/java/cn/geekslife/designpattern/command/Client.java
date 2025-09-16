package cn.geekslife.designpattern.command;

/**
 * 客户端类
 * 演示命令模式的使用
 */
public class Client {
    public static void main(String[] args) {
        // 创建接收者
        Light livingRoomLight = new Light("客厅");
        Light kitchenLight = new Light("厨房");
        Television tv = new Television("卧室");
        Stereo stereo = new Stereo("客厅");
        
        // 创建命令
        LightOnCommand livingRoomLightOn = new LightOnCommand(livingRoomLight);
        LightOffCommand livingRoomLightOff = new LightOffCommand(livingRoomLight);
        LightOnCommand kitchenLightOn = new LightOnCommand(kitchenLight);
        LightOffCommand kitchenLightOff = new LightOffCommand(kitchenLight);
        TVOnCommand tvOn = new TVOnCommand(tv);
        TVOffCommand tvOff = new TVOffCommand(tv);
        StereoOnCommand stereoOn = new StereoOnCommand(stereo);
        StereoOffCommand stereoOff = new StereoOffCommand(stereo);
        
        // 创建遥控器
        RemoteControl remoteControl = new RemoteControl();
        
        // 设置命令
        remoteControl.setCommand(0, livingRoomLightOn, livingRoomLightOff);
        remoteControl.setCommand(1, kitchenLightOn, kitchenLightOff);
        remoteControl.setCommand(2, tvOn, tvOff);
        remoteControl.setCommand(3, stereoOn, stereoOff);
        
        // 打印遥控器状态
        System.out.println(remoteControl);
        
        // 测试按钮
        System.out.println("--- 测试按钮 ---");
        remoteControl.onButtonWasPushed(0);
        remoteControl.offButtonWasPushed(0);
        remoteControl.onButtonWasPushed(1);
        remoteControl.offButtonWasPushed(1);
        remoteControl.onButtonWasPushed(2);
        remoteControl.offButtonWasPushed(2);
        remoteControl.onButtonWasPushed(3);
        remoteControl.offButtonWasPushed(3);
        
        // 测试撤销功能
        System.out.println("\n--- 测试撤销功能 ---");
        remoteControl.onButtonWasPushed(0);
        remoteControl.undoButtonWasPushed();
        remoteControl.offButtonWasPushed(0);
        remoteControl.undoButtonWasPushed();
        
        // 测试宏命令
        System.out.println("\n--- 测试宏命令 ---");
        Command[] partyOn = {livingRoomLightOn, kitchenLightOn, tvOn, stereoOn};
        Command[] partyOff = {livingRoomLightOff, kitchenLightOff, tvOff, stereoOff};
        MacroCommand partyOnMacro = new MacroCommand(partyOn);
        MacroCommand partyOffMacro = new MacroCommand(partyOff);
        
        remoteControl.setCommand(4, partyOnMacro, partyOffMacro);
        System.out.println(remoteControl);
        remoteControl.onButtonWasPushed(4);
        remoteControl.undoButtonWasPushed();
        remoteControl.offButtonWasPushed(4);
        remoteControl.undoButtonWasPushed();
        
        // 测试增强版遥控器
        System.out.println("\n--- 测试增强版遥控器 ---");
        RemoteControlWithHistory advancedRemote = new RemoteControlWithHistory();
        advancedRemote.setCommand(0, livingRoomLightOn, livingRoomLightOff);
        advancedRemote.setCommand(1, tvOn, tvOff);
        
        advancedRemote.onButtonWasPushed(0);
        advancedRemote.onButtonWasPushed(1);
        advancedRemote.offButtonWasPushed(0);
        
        System.out.println(advancedRemote);
        
        // 测试多级撤销
        System.out.println("--- 多级撤销测试 ---");
        advancedRemote.undoButtonWasPushed();
        advancedRemote.undoButtonWasPushed();
        advancedRemote.undoButtonWasPushed();
        
        // 测试重做
        System.out.println("--- 重做测试 ---");
        advancedRemote.redoButtonWasPushed();
        advancedRemote.redoButtonWasPushed();
    }
}