package cn.geekslife.designpattern.command;

/**
 * 开电视命令类
 * 实现Command接口的具体命令
 */
public class TVOnCommand implements Command {
    private Television tv;
    private boolean previousState;
    
    public TVOnCommand(Television tv) {
        this.tv = tv;
    }
    
    @Override
    public void execute() {
        // 保存之前的状态用于撤销
        previousState = tv.isOn();
        tv.on();
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