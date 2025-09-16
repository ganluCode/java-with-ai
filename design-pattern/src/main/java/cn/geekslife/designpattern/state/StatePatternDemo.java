package cn.geekslife.designpattern.state;

/**
 * 状态模式演示类
 */
public class StatePatternDemo {
    public static void main(String[] args) {
        System.out.println("=== 多媒体播放器状态模式演示 ===");
        
        // 创建播放器
        MediaPlayer player = new MediaPlayer();
        player.showState();
        System.out.println("---");
        
        // 测试各种状态转换
        player.play();   // 停止 -> 播放
        player.play();   // 播放 -> 播放
        player.pause();  // 播放 -> 暂停
        player.pause();  // 暂停 -> 暂停
        player.play();   // 暂停 -> 播放
        player.stop();   // 播放 -> 停止
        player.stop();   // 停止 -> 停止
        player.pause();  // 停止 -> 无法暂停
    }
}