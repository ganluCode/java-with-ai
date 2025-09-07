package cn.geekslife.designpattern.factorymethod;

/**
 * 哥布林敌人
 * 实现敌人接口
 */
public class Goblin implements Enemy {
    
    private static final String TYPE = "哥布林";
    private static final int HEALTH = 50;
    
    @Override
    public void attack() {
        System.out.println(TYPE + "挥舞着小刀攻击！");
    }
    
    @Override
    public void move() {
        System.out.println(TYPE + "快速地跳跃移动");
    }
    
    @Override
    public String getType() {
        return TYPE;
    }
    
    @Override
    public int getHealth() {
        return HEALTH;
    }
}