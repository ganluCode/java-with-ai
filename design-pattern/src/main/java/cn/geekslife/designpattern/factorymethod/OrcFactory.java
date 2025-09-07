package cn.geekslife.designpattern.factorymethod;

/**
 * 兽人工厂
 * 实现工厂方法，创建兽人敌人
 */
public class OrcFactory extends EnemyFactory {
    
    @Override
    public Enemy createEnemy() {
        return new Orc();
    }
}