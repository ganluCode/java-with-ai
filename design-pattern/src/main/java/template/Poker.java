package template;

/**
 * 纸牌游戏 - 具体模板类
 */
public class Poker extends Game {
    
    @Override
    protected void initialize() {
        System.out.println("纸牌游戏初始化");
        System.out.println("洗牌...");
        System.out.println("发牌...");
    }
    
    @Override
    protected void startPlay() {
        System.out.println("开始纸牌游戏");
        System.out.println("确定庄家...");
    }
    
    @Override
    protected void runGame() {
        System.out.println("运行纸牌游戏...");
        System.out.println("玩家下注...");
        System.out.println("比较牌型...");
    }
    
    @Override
    protected void endPlay() {
        System.out.println("纸牌游戏结束");
        System.out.println("显示胜利者: 玩家3获胜");
    }
    
    // 重写钩子方法，纸牌游戏总是显示胜利者
    @Override
    protected boolean showWinner() {
        return true;
    }
}