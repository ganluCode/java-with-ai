package cn.geekslife.designpattern.facade.mediaplayer;

/**
 * 视频解码器子系统类
 */
public class VideoDecoder {
    public void decodeVideo(String fileName) {
        System.out.println("视频解码器: 解码视频文件 " + fileName);
    }
    
    public void setResolution(int width, int height) {
        System.out.println("视频解码器: 设置分辨率 " + width + "x" + height);
    }
    
    public void fullscreen() {
        System.out.println("视频解码器: 全屏显示");
    }
}