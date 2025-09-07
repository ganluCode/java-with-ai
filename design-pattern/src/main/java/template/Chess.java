package template;

/**
 * 国际象棋游戏 - 具体模板类
 */
public class Chess extends Game {
    
    @Override
    protected void initialize() {
        System.out.println("国际象棋游戏初始化");
        System.out.println("设置棋盘...");
        System.out.println("放置棋子...");
    }
    
    @Override
    protected void startPlay() {
        System.out.println("开始国际象棋游戏");
        System.out.println("白方先行");
    }
    
    @Override
    protected void runGame() {
        System.out.println("运行国际象棋游戏...");
        System.out.println("玩家轮流走棋...");
        System.out.println("检查将军状态...");
    }
    
    @Override
    protected void endPlay() {
        System.out.println("国际象棋游戏结束");
        System.out.println("显示胜利者: 白方获胜");
    }
    
    // 重写钩子方法，国际象棋总是显示胜利者
    @Override
    protected boolean showWinner() {
        return true;
    }
}