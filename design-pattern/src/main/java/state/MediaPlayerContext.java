package state;

/**
 * 多媒体播放器上下文类 - 与枚举状态配合使用
 */
public class MediaPlayerContext {
    private SimplePlayerState state;
    
    public MediaPlayerContext() {
        // 初始状态为停止状态
        this.state = SimplePlayerState.STOPPED;
    }
    
    // 设置状态
    public void setState(SimplePlayerState state) {
        this.state = state;
    }
    
    // 获取当前状态
    public SimplePlayerState getState() {
        return state;
    }
    
    // 播放操作
    public void play() {
        System.out.println("执行播放操作:");
        state.play(this);
        System.out.println("当前状态: " + state);
        System.out.println("---");
    }
    
    // 暂停操作
    public void pause() {
        System.out.println("执行暂停操作:");
        state.pause(this);
        System.out.println("当前状态: " + state);
        System.out.println("---");
    }
    
    // 停止操作
    public void stop() {
        System.out.println("执行停止操作:");
        state.stop(this);
        System.out.println("当前状态: " + state);
        System.out.println("---");
    }
    
    // 显示当前状态
    public void showState() {
        System.out.println("当前播放器状态: " + state);
    }
}