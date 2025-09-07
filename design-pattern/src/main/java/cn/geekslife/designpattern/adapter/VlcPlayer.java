package cn.geekslife.designpattern.adapter;

/**
 * VLC播放器实现 - 具体的适配者
 */
public class VlcPlayer implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        System.out.println("Playing VLC file: " + fileName);
    }
    
    @Override
    public void playMp4(String fileName) {
        // VLC播放器不支持MP4
        System.out.println("VLC player cannot play MP4 files");
    }
}