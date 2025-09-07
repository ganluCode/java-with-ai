package cn.geekslife.designpattern.adapter;

/**
 * 高级媒体播放器接口 - 需要被适配的接口
 */
public interface AdvancedMediaPlayer {
    void playVlc(String fileName);
    void playMp4(String fileName);
}