package cn.geekslife.designpattern.factorymethod;

/**
 * 抽象敌人工厂
 * 定义创建敌人的接口
 */
public abstract class EnemyFactory {
    
    /**
     * 工厂方法
     * 由具体工厂实现，用于创建具体敌人
     * @return 敌人实例
     */
    public abstract Enemy createEnemy();
    
    /**
     * 生成敌人
     * 使用工厂方法创建敌人并让它行动
     */
    public void spawnEnemy() {
        Enemy enemy = createEnemy();
        System.out.println("生成了" + enemy.getType() + "，生命值：" + enemy.getHealth());
        enemy.move();
        enemy.attack();
    }
    
    /**
     * 获取敌人信息
     * @return 敌人信息字符串
     */
    public String getEnemyInfo() {
        Enemy enemy = createEnemy();
        return "敌人类型: " + enemy.getType() + ", 生命值: " + enemy.getHealth();
    }
}