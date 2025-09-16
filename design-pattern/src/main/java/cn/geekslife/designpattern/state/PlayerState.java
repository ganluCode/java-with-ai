package cn.geekslife.designpattern.state;

/**
 * 抽象状态类
 * 定义了播放器在不同状态下的行为
 */
public abstract class PlayerState {
    protected MediaPlayer player;
    
    public PlayerState(MediaPlayer player) {
        this.player = player;
    }
    
    // 播放操作
    public abstract void play();
    
    // 暂停操作
    public abstract void pause();
    
    // 停止操作
    public abstract void stop();
    
    // 获取状态名称
    public abstract String getName();
}