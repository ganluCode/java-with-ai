package cn.geekslife.designpattern.factorymethod;

/**
 * 兽人敌人
 * 实现敌人接口
 */
public class Orc implements Enemy {
    
    private static final String TYPE = "兽人";
    private static final int HEALTH = 100;
    
    @Override
    public void attack() {
        System.out.println(TYPE + "挥舞着巨大的战斧攻击！");
    }
    
    @Override
    public void move() {
        System.out.println(TYPE + "沉重地踏步移动");
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