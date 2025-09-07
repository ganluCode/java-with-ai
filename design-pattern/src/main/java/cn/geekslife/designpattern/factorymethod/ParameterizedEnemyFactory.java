package cn.geekslife.designpattern.factorymethod;

/**
 * 参数化敌人工厂
 * 通过参数决定创建哪种敌人
 */
public class ParameterizedEnemyFactory {
    
    /**
     * 参数化工厂方法
     * @param type 敌人类型
     * @return 敌人实例
     */
    public static Enemy createEnemy(String type) {
        switch (type.toLowerCase()) {
            case "goblin":
                return new Goblin();
            case "orc":
                return new Orc();
            case "dragon":
                return new Dragon();
            default:
                throw new IllegalArgumentException("未知的敌人类型: " + type);
        }
    }
    
    /**
     * 批量创建敌人
     * @param type 敌人类型
     * @param count 数量
     * @return 敌人数组
     */
    public static Enemy[] createEnemies(String type, int count) {
        Enemy[] enemies = new Enemy[count];
        for (int i = 0; i < count; i++) {
            enemies[i] = createEnemy(type);
        }
        return enemies;
    }
}