package cn.geekslife.designpattern.factorymethod;

/**
 * 龙敌人
 * 实现敌人接口
 */
public class Dragon implements Enemy {
    
    private static final String TYPE = "龙";
    private static final int HEALTH = 500;
    
    @Override
    public void attack() {
        System.out.println(TYPE + "喷射出炽热的火焰攻击！");
    }
    
    @Override
    public void move() {
        System.out.println(TYPE + "展开巨大的翅膀飞翔");
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