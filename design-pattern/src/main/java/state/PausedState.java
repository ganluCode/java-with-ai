package state;

/**
 * 暂停状态
 */
public class PausedState extends PlayerState {
    
    public PausedState(MediaPlayer player) {
        super(player);
    }
    
    @Override
    public void play() {
        System.out.println("从暂停状态恢复播放");
        player.setState(new PlayingState(player));
    }
    
    @Override
    public void pause() {
        System.out.println("已经是暂停状态");
    }
    
    @Override
    public void stop() {
        System.out.println("从暂停状态停止");
        player.setState(new StoppedState(player));
    }
    
    @Override
    public String getName() {
        return "暂停状态";
    }
}