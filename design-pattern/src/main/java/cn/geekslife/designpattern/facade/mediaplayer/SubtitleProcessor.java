package cn.geekslife.designpattern.facade.mediaplayer;

/**
 * 字幕处理器子系统类
 */
public class SubtitleProcessor {
    public void loadSubtitle(String subtitleFile) {
        System.out.println("字幕处理器: 加载字幕文件 " + subtitleFile);
    }
    
    public void showSubtitle(String text) {
        System.out.println("字幕处理器: 显示字幕: " + text);
    }
    
    public void hideSubtitle() {
        System.out.println("字幕处理器: 隐藏字幕");
    }
}