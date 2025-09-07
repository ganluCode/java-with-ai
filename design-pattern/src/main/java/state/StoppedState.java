package state;

/**
 * 停止状态
 */
public class StoppedState extends PlayerState {
    
    public StoppedState(MediaPlayer player) {
        super(player);
    }
    
    @Override
    public void play() {
        System.out.println("从停止状态开始播放");
        player.setState(new PlayingState(player));
    }
    
    @Override
    public void pause() {
        System.out.println("在停止状态下无法暂停");
    }
    
    @Override
    public void stop() {
        System.out.println("已经是停止状态");
    }
    
    @Override
    public String getName() {
        return "停止状态";
    }
}