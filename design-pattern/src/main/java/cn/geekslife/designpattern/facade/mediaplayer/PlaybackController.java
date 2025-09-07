package cn.geekslife.designpattern.facade.mediaplayer;

/**
 * 播放控制器子系统类
 */
public class PlaybackController {
    public void play() {
        System.out.println("播放控制器: 开始播放");
    }
    
    public void pause() {
        System.out.println("播放控制器: 暂停播放");
    }
    
    public void stop() {
        System.out.println("播放控制器: 停止播放");
    }
    
    public void seek(int seconds) {
        System.out.println("播放控制器: 跳转到 " + seconds + " 秒");
    }
}