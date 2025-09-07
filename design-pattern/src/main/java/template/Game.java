package template;

/**
 * 抽象游戏模板类
 */
public abstract class Game {
    // 模板方法，定义游戏流程
    public final void play() {
        // 1. 初始化游戏
        initialize();
        
        // 2. 开始游戏
        startPlay();
        
        // 3. 运行游戏
        runGame();
        
        // 4. 调用钩子方法，判断是否需要显示胜利者
        if (showWinner()) {
            // 5. 显示胜利者
            endPlay();
        }
    }
    
    // 基本方法 - 初始化游戏
    protected abstract void initialize();
    
    // 基本方法 - 开始游戏
    protected abstract void startPlay();
    
    // 基本方法 - 运行游戏
    protected abstract void runGame();
    
    // 钩子方法 - 是否显示胜利者，默认显示
    protected boolean showWinner() {
        return true;
    }
    
    // 基本方法 - 结束游戏并显示胜利者
    protected abstract void endPlay();
}