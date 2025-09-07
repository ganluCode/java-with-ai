package cn.geekslife.designpattern.adapter;

/**
 * 类适配器 - 继承适配者并实现目标接口
 */
public class Mp3Adapter extends Mp3Player implements MediaPlayer {
    @Override
    public void play(String audioType, String fileName) {
        if ("mp3".equalsIgnoreCase(audioType)) {
            playMp3(fileName);
        } else {
            System.out.println("Invalid media type: " + audioType);
        }
    }
}