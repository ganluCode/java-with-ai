package cn.geekslife.designpattern.facade.smarthome;

/**
 * 娱乐系统子系统类
 */
public class EntertainmentSystem {
    public void turnOnTV() {
        System.out.println("娱乐系统: 打开电视");
    }
    
    public void turnOffTV() {
        System.out.println("娱乐系统: 关闭电视");
    }
    
    public void setVolume(int volume) {
        System.out.println("娱乐系统: 设置音量为 " + volume + "%");
    }
    
    public void playMusic(String genre) {
        System.out.println("娱乐系统: 播放 " + genre + " 音乐");
    }
}