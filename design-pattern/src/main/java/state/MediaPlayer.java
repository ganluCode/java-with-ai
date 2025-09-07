package state;

/**
 * 多媒体播放器 - 环境类
 */
public class MediaPlayer {
    private PlayerState state;
    
    public MediaPlayer() {
        // 初始状态为停止状态
        this.state = new StoppedState(this);
    }
    
    // 设置状态
    public void setState(PlayerState state) {
        this.state = state;
    }
    
    // 获取当前状态
    public PlayerState getState() {
        return state;
    }
    
    // 播放操作
    public void play() {
        System.out.println("执行播放操作:");
        state.play();
        System.out.println("当前状态: " + state.getName());
        System.out.println("---");
    }
    
    // 暂停操作
    public void pause() {
        System.out.println("执行暂停操作:");
        state.pause();
        System.out.println("当前状态: " + state.getName());
        System.out.println("---");
    }
    
    // 停止操作
    public void stop() {
        System.out.println("执行停止操作:");
        state.stop();
        System.out.println("当前状态: " + state.getName());
        System.out.println("---");
    }
    
    // 显示当前状态
    public void showState() {
        System.out.println("当前播放器状态: " + state.getName());
    }
}