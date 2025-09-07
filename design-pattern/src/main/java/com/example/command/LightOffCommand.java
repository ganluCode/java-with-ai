package com.example.command;

/**
 * 关灯命令类
 * 实现Command接口的具体命令
 */
public class LightOffCommand implements Command {
    private Light light;
    private boolean previousState;
    
    public LightOffCommand(Light light) {
        this.light = light;
    }
    
    @Override
    public void execute() {
        // 保存之前的状态用于撤销
        previousState = light.isOn();
        light.off();
    }
    
    @Override
    public void undo() {
        if (previousState) {
            light.on();
        } else {
            light.off();
        }
    }
}