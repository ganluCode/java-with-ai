package cn.geekslife.designpattern.factorymethod;

/**
 * 敌人接口
 * 定义敌人的通用行为
 */
public interface Enemy {
    
    /**
     * 攻击行为
     */
    void attack();
    
    /**
     * 移动行为
     */
    void move();
    
    /**
     * 获取敌人类型
     * @return 敌人类型
     */
    String getType();
    
    /**
     * 获取敌人生命值
     * @return 生命值
     */
    int getHealth();
}