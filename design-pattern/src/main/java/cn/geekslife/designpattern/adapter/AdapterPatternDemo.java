package cn.geekslife.designpattern.adapter;

/**
 * 适配器模式演示类
 */
public class AdapterPatternDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 适配器模式演示 ===\n");
        
        // 1. 媒体播放器演示
        demonstrateMediaPlayer();
        
        // 2. 双向适配器演示
        demonstrateBidirectionalAdapter();
        
        // 3. 接口适配器演示
        demonstrateInterfaceAdapter();
    }
    
    /**
     * 演示媒体播放器适配器
     */
    private static void demonstrateMediaPlayer() {
        System.out.println("1. 媒体播放器适配器演示:");
        
        AudioPlayer audioPlayer = new AudioPlayer();
        
        audioPlayer.play("mp3", "song.mp3");
        audioPlayer.play("mp4", "movie.mp4");
        audioPlayer.play("vlc", "video.vlc");
        audioPlayer.play("avi", "clip.avi");
        
        System.out.println();
    }
    
    /**
     * 演示双向适配器
     */
    private static void demonstrateBidirectionalAdapter() {
        System.out.println("2. 双向适配器演示:");
        
        Rectangle rectangle = new Rectangle(50, 30);
        ShapeAdapter shapeAdapter = new ShapeAdapter(rectangle);
        
        // 通过Shape接口使用
        System.out.println("通过Shape接口使用:");
        shapeAdapter.draw();
        shapeAdapter.resize();
        
        // 通过GeometricShape接口使用
        System.out.println("\n通过GeometricShape接口使用:");
        shapeAdapter.render();
        shapeAdapter.scale(1.5);
        
        System.out.println();
    }
    
    /**
     * 演示接口适配器
     */
    private static void demonstrateInterfaceAdapter() {
        System.out.println("3. 接口适配器演示:");
        
        SimpleDatabase simpleDatabase = new SimpleDatabase();
        
        simpleDatabase.connect();
        simpleDatabase.executeQuery("SELECT * FROM users");
        simpleDatabase.executeUpdate("UPDATE users SET name = 'John' WHERE id = 1");
        simpleDatabase.disconnect();
        
        System.out.println();
    }
}