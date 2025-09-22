package cn.geekslife.designpattern.command;

/**
 * 开音响命令类
 * 实现Command接口的具体命令
 */
public class StereoOnCommand implements Command {
    private Stereo stereo;
    private boolean previousState;
    private int previousVolume;
    private String previousMode;
    
    public StereoOnCommand(Stereo stereo) {
        this.stereo = stereo;
    }
    
    @Override
    public void execute() {
        // 保存之前的状态用于撤销
        previousState = stereo.isOn();
        previousVolume = stereo.getVolume();
        previousMode = stereo.getMode();
        stereo.on();
        stereo.setCD();
        stereo.setVolume(11);
    }
    
    @Override
    public void undo() {
        if (previousState) {
            stereo.on();
            stereo.setVolume(previousVolume);
            stereo.setCD(); // 简化处理，实际应该恢复到之前的模式
        } else {
            stereo.off();
        }
    }
}