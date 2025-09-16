package cn.geekslife.designpattern.state;

/**
 * 枚举状态 - 解决类数量膨胀问题
 * 适用于简单状态管理
 */
public enum SimplePlayerState {
    STOPPED {
        @Override
        public void play(MediaPlayerContext context) {
            System.out.println("从停止状态开始播放");
            context.setState(PLAYING);
        }
        
        @Override
        public void pause(MediaPlayerContext context) {
            System.out.println("在停止状态下无法暂停");
        }
        
        @Override
        public void stop(MediaPlayerContext context) {
            System.out.println("已经是停止状态");
        }
    },
    PLAYING {
        @Override
        public void play(MediaPlayerContext context) {
            System.out.println("已经在播放状态");
        }
        
        @Override
        public void pause(MediaPlayerContext context) {
            System.out.println("从播放状态暂停");
            context.setState(PAUSED);
        }
        
        @Override
        public void stop(MediaPlayerContext context) {
            System.out.println("从播放状态停止");
            context.setState(STOPPED);
        }
    },
    PAUSED {
        @Override
        public void play(MediaPlayerContext context) {
            System.out.println("从暂停状态恢复播放");
            context.setState(PLAYING);
        }
        
        @Override
        public void pause(MediaPlayerContext context) {
            System.out.println("已经是暂停状态");
        }
        
        @Override
        public void stop(MediaPlayerContext context) {
            System.out.println("从暂停状态停止");
            context.setState(STOPPED);
        }
    };
    
    // 抽象方法
    public abstract void play(MediaPlayerContext context);
    public abstract void pause(MediaPlayerContext context);
    public abstract void stop(MediaPlayerContext context);
}