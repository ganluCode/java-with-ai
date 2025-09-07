package cn.geekslife.designpattern.factorymethod;

/**
 * 工厂方法模式演示类
 * 展示各种工厂方法模式的使用方法
 */
public class FactoryMethodDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 工厂方法模式演示 ===");
        
        // 1. 基本工厂方法模式
        System.out.println("\n1. 基本工厂方法模式:");
        demonstrateBasicFactoryMethod();
        
        // 2. 参数化工厂方法
        System.out.println("\n2. 参数化工厂方法:");
        demonstrateParameterizedFactoryMethod();
        
        // 3. 静态工厂方法
        System.out.println("\n3. 静态工厂方法:");
        demonstrateStaticFactoryMethod();
        
        // 4. 游戏场景应用
        System.out.println("\n4. 游戏场景应用:");
        demonstrateGameScenario();
    }
    
    /**
     * 演示基本工厂方法模式
     */
    private static void demonstrateBasicFactoryMethod() {
        // 创建产品A
        Creator creatorA = new ConcreteCreatorA();
        creatorA.anOperation();
        System.out.println("产品A信息: " + creatorA.getProductInfo());
        
        // 创建产品B
        Creator creatorB = new ConcreteCreatorB();
        creatorB.anOperation();
        System.out.println("产品B信息: " + creatorB.getProductInfo());
        
        // 创建产品C
        Creator creatorC = new ConcreteCreatorC();
        creatorC.anOperation();
        System.out.println("产品C信息: " + creatorC.getProductInfo());
    }
    
    /**
     * 演示参数化工厂方法
     */
    private static void demonstrateParameterizedFactoryMethod() {
        ParameterizedCreator creator = new ConcreteParameterizedCreator();
        
        // 创建产品A
        Product productA = creator.createProduct("A");
        productA.use();
        
        // 创建产品B
        Product productB = creator.createProduct("B");
        productB.use();
        
        // 批量创建产品C
        Product[] productsC = creator.createProducts("C", 3);
        System.out.println("批量创建了" + productsC.length + "个产品C");
        for (Product product : productsC) {
            System.out.println("产品名称: " + product.getName() + ", 价格: " + product.getPrice());
        }
    }
    
    /**
     * 演示静态工厂方法
     */
    private static void demonstrateStaticFactoryMethod() {
        // 创建产品A
        Product productA = StaticFactory.createProduct("A");
        productA.use();
        System.out.println("产品A描述: " + StaticFactory.getProductDescription("A"));
        
        // 创建产品B
        Product productB = StaticFactory.createProduct("B");
        productB.use();
        System.out.println("产品B描述: " + StaticFactory.getProductDescription("B"));
        
        // 批量创建产品C
        Product[] productsC = StaticFactory.createProducts("C", 2);
        System.out.println("批量创建了" + productsC.length + "个产品C");
        for (Product product : productsC) {
            System.out.println("产品名称: " + product.getName() + ", 价格: " + product.getPrice());
        }
    }
    
    /**
     * 演示游戏场景应用
     */
    private static void demonstrateGameScenario() {
        // 生成哥布林
        EnemyFactory goblinFactory = new GoblinFactory();
        goblinFactory.spawnEnemy();
        System.out.println("哥布林信息: " + goblinFactory.getEnemyInfo());
        
        // 生成兽人
        EnemyFactory orcFactory = new OrcFactory();
        orcFactory.spawnEnemy();
        System.out.println("兽人信息: " + orcFactory.getEnemyInfo());
        
        // 生成龙
        EnemyFactory dragonFactory = new DragonFactory();
        dragonFactory.spawnEnemy();
        System.out.println("龙信息: " + dragonFactory.getEnemyInfo());
        
        // 参数化生成敌人
        System.out.println("\n参数化生成敌人:");
        Enemy goblin = ParameterizedEnemyFactory.createEnemy("goblin");
        goblin.move();
        goblin.attack();
        
        // 批量生成兽人
        Enemy[] orcs = ParameterizedEnemyFactory.createEnemies("orc", 3);
        System.out.println("批量生成了" + orcs.length + "个兽人");
        for (Enemy orc : orcs) {
            System.out.println("兽人类型: " + orc.getType() + ", 生命值: " + orc.getHealth());
        }
    }
}