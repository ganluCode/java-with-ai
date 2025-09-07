package cn.geekslife.designpattern.adapter;

/**
 * MP3播放器 - 已有的音频播放器
 */
public class Mp3Player {
    public void playMp3(String fileName) {
        System.out.println("Playing MP3 file: " + fileName);
    }
}