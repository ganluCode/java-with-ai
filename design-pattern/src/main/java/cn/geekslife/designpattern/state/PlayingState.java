package cn.geekslife.designpattern.state;

/**
 * 播放状态
 */
public class PlayingState extends PlayerState {
    
    public PlayingState(MediaPlayer player) {
        super(player);
    }
    
    @Override
    public void play() {
        System.out.println("已经在播放状态");
    }
    
    @Override
    public void pause() {
        System.out.println("从播放状态暂停");
        player.setState(new PausedState(player));
    }
    
    @Override
    public void stop() {
        System.out.println("从播放状态停止");
        player.setState(new StoppedState(player));
    }
    
    @Override
    public String getName() {
        return "播放状态";
    }
}