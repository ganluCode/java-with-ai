package com.example.command;

/**
 * 关电视命令类
 * 实现Command接口的具体命令
 */
public class TVOffCommand implements Command {
    private Television tv;
    private boolean previousState;
    
    public TVOffCommand(Television tv) {
        this.tv = tv;
    }
    
    @Override
    public void execute() {
        // 保存之前的状态用于撤销
        previousState = tv.isOn();
        tv.off();
    }
    
    @Override
    public void undo() {
        if (previousState) {
            tv.on();
        } else {
            tv.off();
        }
    }
}