package cn.geekslife.designpattern.adapter;

/**
 * MP4播放器实现 - 具体的适配者
 */
public class Mp4Player implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        // MP4播放器不支持VLC
        System.out.println("MP4 player cannot play VLC files");
    }
    
    @Override
    public void playMp4(String fileName) {
        System.out.println("Playing MP4 file: " + fileName);
    }
}