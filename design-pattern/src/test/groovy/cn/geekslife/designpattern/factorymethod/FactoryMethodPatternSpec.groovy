package cn.geekslife.designpattern.factorymethod

import spock.lang.Specification
import spock.lang.Unroll

/**
 * 工厂方法模式Spock测试类
 * 使用行为驱动开发(BDD)方式测试工厂方法模式
 */
class FactoryMethodPatternSpec extends Specification {
    
    def "应该能够通过具体创建者创建具体产品"() {
        given: "创建具体创建者实例"
        Creator creatorA = new ConcreteCreatorA()
        Creator creatorB = new ConcreteCreatorB()
        Creator creatorC = new ConcreteCreatorC()
        
        when: "调用工厂方法创建产品"
        Product productA = creatorA.factoryMethod()
        Product productB = creatorB.factoryMethod()
        Product productC = creatorC.factoryMethod()
        
        then: "验证创建的产品类型正确"
        productA instanceof ConcreteProductA
        productB instanceof ConcreteProductB
        productC instanceof ConcreteProductC
        
        and: "验证产品属性正确"
        productA.getName() == "产品A"
        productA.getPrice() == 100.0
        productB.getName() == "产品B"
        productB.getPrice() == 200.0
        productC.getName() == "产品C"
        productC.getPrice() == 300.0
    }
    
    def "应该能够通过具体创建者执行业务操作"() {
        given: "创建具体创建者实例"
        Creator creatorA = new ConcreteCreatorA()
        Creator creatorB = new ConcreteCreatorB()
        
        when: "执行业务操作"
        creatorA.anOperation()
        creatorB.anOperation()
        
        then: "验证操作执行成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
    
    def "应该能够获取产品信息"() {
        given: "创建具体创建者实例"
        Creator creatorA = new ConcreteCreatorA()
        Creator creatorB = new ConcreteCreatorB()
        Creator creatorC = new ConcreteCreatorC()
        
        when: "获取产品信息"
        String infoA = creatorA.getProductInfo()
        String infoB = creatorB.getProductInfo()
        String infoC = creatorC.getProductInfo()
        
        then: "验证产品信息正确"
        infoA.contains("产品A")
        infoA.contains("100.0")
        infoB.contains("产品B")
        infoB.contains("200.0")
        infoC.contains("产品C")
        infoC.contains("300.0")
    }
    
    @Unroll
    def "参数化工厂方法应该能够根据类型创建正确的产品: #type"() {
        given: "创建参数化工厂实例"
        ParameterizedCreator creator = new ConcreteParameterizedCreator()
        
        when: "根据类型创建产品"
        Product product = creator.factoryMethod(type)
        
        then: "验证创建的产品类型正确"
        product.getClass() == expectedClass
        product.getName() == expectedName
        product.getPrice() == expectedPrice
        
        where: "测试数据"
        type | expectedClass          | expectedName | expectedPrice
        "A"  | ConcreteProductA.class | "产品A"      | 100.0
        "B"  | ConcreteProductB.class | "产品B"      | 200.0
        "C"  | ConcreteProductC.class | "产品C"      | 300.0
    }
    
    def "参数化工厂方法应该处理未知类型并抛出异常"() {
        given: "创建参数化工厂实例"
        ParameterizedCreator creator = new ConcreteParameterizedCreator()
        String unknownType = "Unknown"
        
        when: "使用未知类型创建产品"
        creator.factoryMethod(unknownType)
        
        then: "应该抛出IllegalArgumentException异常"
        thrown(IllegalArgumentException)
    }
    
    def "应该能够批量创建产品"() {
        given: "创建参数化工厂实例"
        ParameterizedCreator creator = new ConcreteParameterizedCreator()
        int count = 5
        
        when: "批量创建产品"
        Product[] products = creator.createProducts("A", count)
        
        then: "验证批量创建的产品正确"
        products.length == count
        products.each { product ->
            assert product instanceof ConcreteProductA
            assert product.getName() == "产品A"
            assert product.getPrice() == 100.0
        }
    }
    
    def "静态工厂方法应该能够创建正确的产品"() {
        when: "使用静态工厂方法创建产品"
        Product productA = StaticFactory.createProduct("A")
        Product productB = StaticFactory.createProduct("B")
        Product productC = StaticFactory.createProduct("C")
        
        then: "验证创建的产品正确"
        productA instanceof ConcreteProductA
        productB instanceof ConcreteProductB
        productC instanceof ConcreteProductC
        
        and: "验证产品属性正确"
        productA.getName() == "产品A"
        productA.getPrice() == 100.0
        productB.getName() == "产品B"
        productB.getPrice() == 200.0
        productC.getName() == "产品C"
        productC.getPrice() == 300.0
    }
    
    def "静态工厂方法应该处理未知类型并抛出异常"() {
        given: "未知的产品类型"
        String unknownType = "Unknown"
        
        when: "使用未知类型创建产品"
        StaticFactory.createProduct(unknownType)
        
        then: "应该抛出IllegalArgumentException异常"
        thrown(IllegalArgumentException)
    }
    
    def "应该能够批量创建产品通过静态工厂"() {
        given: "指定产品类型和数量"
        String type = "B"
        int count = 3
        
        when: "批量创建产品"
        Product[] products = StaticFactory.createProducts(type, count)
        
        then: "验证批量创建的产品正确"
        products.length == count
        products.each { product ->
            assert product instanceof ConcreteProductB
            assert product.getName() == "产品B"
            assert product.getPrice() == 200.0
        }
    }
    
    def "静态工厂应该能够获取产品描述"() {
        when: "获取产品描述"
        String descriptionA = StaticFactory.getProductDescription("A")
        String descriptionB = StaticFactory.getProductDescription("B")
        String descriptionC = StaticFactory.getProductDescription("C")
        
        then: "验证产品描述正确"
        descriptionA.contains("产品A")
        descriptionA.contains("100.0")
        descriptionB.contains("产品B")
        descriptionB.contains("200.0")
        descriptionC.contains("产品C")
        descriptionC.contains("300.0")
    }
    
    def "应该能够通过敌人工厂创建正确的敌人"() {
        given: "创建具体敌人工厂实例"
        EnemyFactory goblinFactory = new GoblinFactory()
        EnemyFactory orcFactory = new OrcFactory()
        EnemyFactory dragonFactory = new DragonFactory()
        
        when: "创建敌人"
        Enemy goblin = goblinFactory.createEnemy()
        Enemy orc = orcFactory.createEnemy()
        Enemy dragon = dragonFactory.createEnemy()
        
        then: "验证创建的敌人类型正确"
        goblin instanceof Goblin
        orc instanceof Orc
        dragon instanceof Dragon
        
        and: "验证敌人属性正确"
        goblin.getType() == "哥布林"
        goblin.getHealth() == 50
        orc.getType() == "兽人"
        orc.getHealth() == 100
        dragon.getType() == "龙"
        dragon.getHealth() == 500
    }
    
    def "应该能够生成敌人并执行行为"() {
        given: "创建具体敌人工厂实例"
        EnemyFactory goblinFactory = new GoblinFactory()
        EnemyFactory orcFactory = new OrcFactory()
        
        when: "生成敌人"
        goblinFactory.spawnEnemy()
        orcFactory.spawnEnemy()
        
        then: "验证敌人生成成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
    
    def "应该能够获取敌人信息"() {
        given: "创建具体敌人工厂实例"
        EnemyFactory goblinFactory = new GoblinFactory()
        EnemyFactory orcFactory = new OrcFactory()
        EnemyFactory dragonFactory = new DragonFactory()
        
        when: "获取敌人信息"
        String infoGoblin = goblinFactory.getEnemyInfo()
        String infoOrc = orcFactory.getEnemyInfo()
        String infoDragon = dragonFactory.getEnemyInfo()
        
        then: "验证敌人信息正确"
        infoGoblin.contains("哥布林")
        infoGoblin.contains("50")
        infoOrc.contains("兽人")
        infoOrc.contains("100")
        infoDragon.contains("龙")
        infoDragon.contains("500")
    }
    
    @Unroll
    def "参数化敌人工厂应该能够根据类型创建正确的敌人: #type"() {
        when: "根据类型创建敌人"
        Enemy enemy = ParameterizedEnemyFactory.createEnemy(type)
        
        then: "验证创建的敌人类型正确"
        enemy.getClass() == expectedClass
        enemy.getType() == expectedType
        enemy.getHealth() == expectedHealth
        
        where: "测试数据"
        type     | expectedClass | expectedType | expectedHealth
        "goblin" | Goblin.class  | "哥布林"     | 50
        "orc"    | Orc.class     | "兽人"       | 100
        "dragon" | Dragon.class  | "龙"         | 500
    }
    
    def "参数化敌人工厂应该处理未知类型并抛出异常"() {
        given: "未知的敌人类型"
        String unknownType = "Unknown"
        
        when: "使用未知类型创建敌人"
        ParameterizedEnemyFactory.createEnemy(unknownType)
        
        then: "应该抛出IllegalArgumentException异常"
        thrown(IllegalArgumentException)
    }
    
    def "应该能够批量创建敌人"() {
        given: "指定敌人类型和数量"
        String type = "orc"
        int count = 4
        
        when: "批量创建敌人"
        Enemy[] enemies = ParameterizedEnemyFactory.createEnemies(type, count)
        
        then: "验证批量创建的敌人正确"
        enemies.length == count
        enemies.each { enemy ->
            assert enemy instanceof Orc
            assert enemy.getType() == "兽人"
            assert enemy.getHealth() == 100
        }
    }
    
    def "敌人应该能够执行攻击和移动行为"() {
        given: "创建各种敌人"
        Enemy goblin = new Goblin()
        Enemy orc = new Orc()
        Enemy dragon = new Dragon()
        
        when: "执行攻击和移动行为"
        goblin.attack()
        goblin.move()
        orc.attack()
        orc.move()
        dragon.attack()
        dragon.move()
        
        then: "验证行为执行成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
}