package cn.geekslife.designpattern.template;

/**
 * 俄罗斯方块游戏 - 具体模板类
 */
public class Tetris extends Game {
    
    @Override
    protected void initialize() {
        System.out.println("俄罗斯方块游戏初始化");
        System.out.println("创建游戏区域...");
        System.out.println("生成初始方块...");
    }
    
    @Override
    protected void startPlay() {
        System.out.println("开始俄罗斯方块游戏");
        System.out.println("方块开始下落...");
    }
    
    @Override
    protected void runGame() {
        System.out.println("运行俄罗斯方块游戏...");
        System.out.println("玩家控制方块移动...");
        System.out.println("消除完整行...");
    }
    
    @Override
    protected void endPlay() {
        System.out.println("俄罗斯方块游戏结束");
        System.out.println("最终得分: 1500分");
    }
    
    // 重写钩子方法，俄罗斯方块游戏不显示胜利者，只显示得分
    @Override
    protected boolean showWinner() {
        return false;  // 不显示胜利者
    }
}