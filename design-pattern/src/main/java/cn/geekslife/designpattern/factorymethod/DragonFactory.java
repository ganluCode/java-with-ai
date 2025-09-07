package cn.geekslife.designpattern.factorymethod;

/**
 * 龙工厂
 * 实现工厂方法，创建龙敌人
 */
public class DragonFactory extends EnemyFactory {
    
    @Override
    public Enemy createEnemy() {
        return new Dragon();
    }
}