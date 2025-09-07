package cn.geekslife.designpattern.factorymethod;

/**
 * 哥布林工厂
 * 实现工厂方法，创建哥布林敌人
 */
public class GoblinFactory extends EnemyFactory {
    
    @Override
    public Enemy createEnemy() {
        return new Goblin();
    }
}