package cn.geekslife.designpattern.facade.mediaplayer;

/**
 * 音频解码器子系统类
 */
public class AudioDecoder {
    public void decodeAudio(String fileName) {
        System.out.println("音频解码器: 解码音频文件 " + fileName);
    }
    
    public void setVolume(int volume) {
        System.out.println("音频解码器: 设置音量为 " + volume + "%");
    }
    
    public void mute() {
        System.out.println("音频解码器: 静音");
    }
}