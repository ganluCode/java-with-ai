package cn.geekslife.designpattern.facade.mediaplayer;

/**
 * 多媒体播放器外观类
 */
public class MediaPlayerFacade {
    private AudioDecoder audioDecoder;
    private VideoDecoder videoDecoder;
    private SubtitleProcessor subtitleProcessor;
    private PlaybackController playbackController;
    
    public MediaPlayerFacade() {
        this.audioDecoder = new AudioDecoder();
        this.videoDecoder = new VideoDecoder();
        this.subtitleProcessor = new SubtitleProcessor();
        this.playbackController = new PlaybackController();
    }
    
    public void playMovie(String videoFile, String audioFile, String subtitleFile) {
        System.out.println("=== 播放电影 ===");
        videoDecoder.decodeVideo(videoFile);
        audioDecoder.decodeAudio(audioFile);
        if (subtitleFile != null && !subtitleFile.isEmpty()) {
            subtitleProcessor.loadSubtitle(subtitleFile);
        }
        playbackController.play();
        System.out.println("=== 电影播放开始 ===\n");
    }
    
    public void pauseMovie() {
        System.out.println("=== 暂停电影 ===");
        playbackController.pause();
        System.out.println("=== 电影已暂停 ===\n");
    }
    
    public void resumeMovie() {
        System.out.println("=== 继续播放电影 ===");
        playbackController.play();
        System.out.println("=== 电影继续播放 ===\n");
    }
    
    public void stopMovie() {
        System.out.println("=== 停止播放电影 ===");
        playbackController.stop();
        subtitleProcessor.hideSubtitle();
        System.out.println("=== 电影播放停止 ===\n");
    }
    
    public void setVolume(int volume) {
        System.out.println("=== 设置音量 ===");
        audioDecoder.setVolume(volume);
        System.out.println("=== 音量设置完成 ===\n");
    }
    
    public void mute() {
        System.out.println("=== 静音 ===");
        audioDecoder.mute();
        System.out.println("=== 已静音 ===\n");
    }
    
    public void fullscreen() {
        System.out.println("=== 全屏 ===");
        videoDecoder.fullscreen();
        System.out.println("=== 已全屏 ===\n");
    }
}