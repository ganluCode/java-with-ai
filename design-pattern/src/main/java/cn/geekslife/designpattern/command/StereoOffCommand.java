package cn.geekslife.designpattern.command;

/**
 * 关音响命令类
 * 实现Command接口的具体命令
 */
public class StereoOffCommand implements Command {
    private Stereo stereo;
    private boolean previousState;
    private int previousVolume;
    private String previousMode;
    
    public StereoOffCommand(Stereo stereo) {
        this.stereo = stereo;
    }
    
    @Override
    public void execute() {
        // 保存之前的状态用于撤销
        previousState = stereo.isOn();
        previousVolume = stereo.getVolume();
        previousMode = stereo.getMode();
        stereo.off();
    }
    
    @Override
    public void undo() {
        if (previousState) {
            stereo.on();
            stereo.setVolume(previousVolume);
            // 简化处理，实际应该恢复到之前的模式
            if ("CD".equals(previousMode)) {
                stereo.setCD();
            } else if ("DVD".equals(previousMode)) {
                stereo.setDVD();
            } else if ("Radio".equals(previousMode)) {
                stereo.setRadio();
            }
        } else {
            stereo.off();
        }
    }
}