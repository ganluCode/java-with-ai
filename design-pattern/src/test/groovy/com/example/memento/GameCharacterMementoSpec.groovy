package com.example.memento

import spock.lang.Specification

/**
 * 游戏角色备忘录模式测试类
 */
class GameCharacterMementoSpec extends Specification {
    
    def "游戏角色应该能够保存和恢复完整状态"() {
        given:
        GameCharacter character = new GameCharacter("勇士", 5, 150, 80, 250, "魔法森林")
        character.addItem("魔法剑", 0)
        character.addItem("治疗药水", 1)
        Caretaker caretaker = new Caretaker()
        
        when:
        // 保存当前状态
        caretaker.addMemento(character.createMemento("冒险前"))
        
        // 角色进行冒险
        character.gainExperience(300) // 足够升级到6级 (250 + 300 = 550 > 500)
        character.takeDamage(30)
        character.moveTo("龙之洞穴")
        character.addItem("龙之鳞片", 2)
        
        // 保存冒险后的状态
        caretaker.addMemento(character.createMemento("冒险后"))
        
        then:
        character.getLevel() == 6 // 升级了
        character.getHealth() == 140 // 150 + 20(升级) - 30(伤害) = 140
        character.getLocation() == "龙之洞穴"
        character.getInventory()[2] == "龙之鳞片"
        caretaker.getMementoCount() == 2
        
        when:
        // 恢复到冒险前的状态
        character.restoreMemento(caretaker.getMemento(0))
        
        then:
        character.getName() == "勇士"
        character.getLevel() == 5
        character.getHealth() == 150
        character.getMana() == 80
        character.getExperience() == 250
        character.getLocation() == "魔法森林"
        character.getInventory()[0] == "魔法剑"
        character.getInventory()[1] == "治疗药水"
        character.getInventory()[2] == null
    }
    
    def "游戏角色应该支持存档和读档功能"() {
        given:
        GameCharacter character = new GameCharacter("法师")
        Caretaker saveSlots = new Caretaker(5) // 5个存档槽位
        
        when:
        // 创建多个存档点
        character.gainExperience(50)
        character.moveTo("新手村")
        saveSlots.addMemento(character.createMemento("自动存档1"))
        
        character.gainExperience(100)
        character.levelUp()
        character.moveTo("城镇")
        saveSlots.addMemento(character.createMemento("自动存档2"))
        
        character.gainExperience(200)
        character.levelUp()
        character.moveTo("地下城")
        saveSlots.addMemento(character.createMemento("手动存档1"))
        
        then:
        saveSlots.getMementoCount() == 3
        saveSlots.getMemento(0).getDescription() == "自动存档1"
        saveSlots.getMemento(1).getDescription() == "自动存档2"
        saveSlots.getMemento(2).getDescription() == "手动存档1"
        
        when:
        // 读取手动存档1
        character.restoreMemento(saveSlots.getMemento(2))
        
        then:
        character.getLevel() == 4 // 1级起始 + 两次升级 = 3级，再调用levelUp() = 4级
        character.getLocation() == "地下城"
    }
    
    def "游戏角色应该处理战斗状态的备忘录"() {
        given:
        GameCharacter character = new GameCharacter("战士", 10, 300, 150, 500, "战场")
        Caretaker battleSaves = new Caretaker()
        
        when:
        // 战斗前保存
        battleSaves.addMemento(character.createMemento("战斗前"))
        
        // 模拟战斗过程
        character.takeDamage(100) // 300 - 100 = 200
        character.consumeMana(50)  // 150 - 50 = 100
        character.heal(30)        // 200 + 30 = 230
        character.takeDamage(80)  // 230 - 80 = 150
        
        // 战斗中保存
        battleSaves.addMemento(character.createMemento("战斗中"))
        
        // 战斗结束
        character.gainExperience(300)
        character.heal(200) // 150 + 200 = 350
        
        then:
        character.getHealth() == 350 // 计算: 300 - 100 + 30 - 80 + 200 = 350
        character.getMana() == 100 // 150 - 50
        character.getExperience() == 800 // 500 + 300
        
        when:
        // 如果战斗失败，可以恢复到战斗前
        character.restoreMemento(battleSaves.getMemento(0))
        
        then:
        character.getHealth() == 300
        character.getMana() == 150
        character.getExperience() == 500
        character.getLocation() == "战场"
    }
    
    def "游戏角色应该支持多个角色的状态管理"() {
        given:
        GameCharacter warrior = new GameCharacter("战士")
        GameCharacter mage = new GameCharacter("法师")
        Caretaker partySaves = new Caretaker()
        
        when:
        // 保存队伍状态
        warrior.gainExperience(100) // 升级到2级，生命值从100增加到120，经验值重置为0
        mage.gainExperience(150) // 升级到2级，魔法值从50增加到60，经验值重置为0
        
        partySaves.addMemento(warrior.createMemento("战士状态"))
        partySaves.addMemento(mage.createMemento("法师状态"))
        
        // 继续游戏
        warrior.takeDamage(30) // 120 - 30 = 90
        mage.consumeMana(30) // 60 - 30 = 30
        
        then:
        warrior.getHealth() == 90 // 120 - 30 = 90
        mage.getMana() == 30 // 60 - 30 = 30
        partySaves.getMementoCount() == 2
        
        when:
        // 恢复队伍状态
        warrior.restoreMemento(partySaves.getMemento(0))
        mage.restoreMemento(partySaves.getMemento(1))
        
        then:
        warrior.getHealth() == 120 // 恢复到保存时的状态（升级后的状态）
        warrior.getExperience() == 0 // 升级后经验值重置为0
        mage.getMana() == 60 // 恢复到保存时的状态（升级后的状态）
        mage.getExperience() == 0 // 升级后经验值重置为0
    }
    
    def "游戏角色应该正确处理背包状态"() {
        given:
        GameCharacter character = new GameCharacter("冒险者")
        Caretaker inventorySaves = new Caretaker()
        
        when:
        // 装备物品
        character.addItem("铁剑", 0)
        character.addItem("皮甲", 1)
        character.addItem("面包", 2)
        inventorySaves.addMemento(character.createMemento("装备齐全"))
        
        // 使用物品
        character.addItem(null, 2) // 使用面包
        character.addItem("魔法药水", 3)
        
        then:
        character.getInventory()[2] == null
        character.getInventory()[3] == "魔法药水"
        
        when:
        // 恢复到装备齐全的状态
        character.restoreMemento(inventorySaves.getMemento(0))
        
        then:
        character.getInventory()[0] == "铁剑"
        character.getInventory()[1] == "皮甲"
        character.getInventory()[2] == "面包"
        character.getInventory()[3] == null
    }
}