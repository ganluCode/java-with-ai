package com.example.memento;

/**
 * 游戏角色类 - 原发器的具体实现
 * 演示备忘录模式在游戏存档中的应用
 */
public class GameCharacter implements Originator {
    private String name;
    private int level;
    private int health;
    private int mana;
    private int experience;
    private String location;
    private String[] inventory;
    
    /**
     * 构造函数
     * @param name 角色名称
     */
    public GameCharacter(String name) {
        this.name = name;
        this.level = 1;
        this.health = 100;
        this.mana = 50;
        this.experience = 0;
        this.location = "新手村";
        this.inventory = new String[10];
    }
    
    /**
     * 构造函数
     * @param name 角色名称
     * @param level 等级
     * @param health 生命值
     * @param mana 魔法值
     * @param experience 经验值
     * @param location 位置
     */
    public GameCharacter(String name, int level, int health, int mana, int experience, String location) {
        this.name = name;
        this.level = level;
        this.health = health;
        this.mana = mana;
        this.experience = experience;
        this.location = location;
        this.inventory = new String[10];
    }
    
    /**
     * 升级
     */
    public void levelUp() {
        level++;
        health += 20;
        mana += 10;
        experience = 0;
        System.out.println(name + " 升级到 " + level + " 级!");
    }
    
    /**
     * 获得经验值
     * @param exp 经验值
     */
    public void gainExperience(int exp) {
        experience += exp;
        System.out.println(name + " 获得 " + exp + " 点经验值");
        
        // 检查是否升级
        if (experience >= level * 100) {
            levelUp();
        }
    }
    
    /**
     * 受到伤害
     * @param damage 伤害值
     */
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        System.out.println(name + " 受到 " + damage + " 点伤害，剩余生命值: " + health);
    }
    
    /**
     * 恢复生命值
     * @param heal 恢复值
     */
    public void heal(int heal) {
        health += heal;
        System.out.println(name + " 恢复 " + heal + " 点生命值，当前生命值: " + health);
    }
    
    /**
     * 消耗魔法值
     * @param manaCost 魔法值消耗
     */
    public void consumeMana(int manaCost) {
        mana -= manaCost;
        if (mana < 0) {
            mana = 0;
        }
        System.out.println(name + " 消耗 " + manaCost + " 点魔法值，剩余魔法值: " + mana);
    }
    
    /**
     * 移动到新位置
     * @param newLocation 新位置
     */
    public void moveTo(String newLocation) {
        location = newLocation;
        System.out.println(name + " 移动到 " + newLocation);
    }
    
    /**
     * 添加物品到背包
     * @param item 物品
     * @param slot 背包槽位
     */
    public void addItem(String item, int slot) {
        if (slot >= 0 && slot < inventory.length) {
            inventory[slot] = item;
            System.out.println(name + " 获得物品: " + item);
        }
    }
    
    /**
     * 获取角色名称
     * @return 角色名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 获取等级
     * @return 等级
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * 获取生命值
     * @return 生命值
     */
    public int getHealth() {
        return health;
    }
    
    /**
     * 获取魔法值
     * @return 魔法值
     */
    public int getMana() {
        return mana;
    }
    
    /**
     * 获取经验值
     * @return 经验值
     */
    public int getExperience() {
        return experience;
    }
    
    /**
     * 获取位置
     * @return 位置
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * 获取背包物品
     * @return 背包物品数组
     */
    public String[] getInventory() {
        return inventory.clone();
    }
    
    @Override
    public Memento createMemento(String description) {
        // 序列化角色状态
        StringBuilder stateBuilder = new StringBuilder();
        stateBuilder.append(name).append("|")
                   .append(level).append("|")
                   .append(health).append("|")
                   .append(mana).append("|")
                   .append(experience).append("|")
                   .append(location).append("|");
        
        // 序列化背包
        for (int i = 0; i < inventory.length; i++) {
            stateBuilder.append(inventory[i] != null ? inventory[i] : "null");
            if (i < inventory.length - 1) {
                stateBuilder.append(",");
            }
        }
        
        return new Memento(stateBuilder.toString(), description);
    }
    
    @Override
    public void restoreMemento(Memento memento) {
        if (memento != null) {
            String state = memento.getState();
            String[] parts = state.split("\\|");
            
            if (parts.length >= 7) {
                name = parts[0];
                level = Integer.parseInt(parts[1]);
                health = Integer.parseInt(parts[2]);
                mana = Integer.parseInt(parts[3]);
                experience = Integer.parseInt(parts[4]);
                location = parts[5];
                
                // 恢复背包
                String[] inventoryParts = parts[6].split(",");
                for (int i = 0; i < Math.min(inventoryParts.length, inventory.length); i++) {
                    inventory[i] = "null".equals(inventoryParts[i]) ? null : inventoryParts[i];
                }
                
                System.out.println("角色状态已恢复: " + memento.getDescription());
            }
        }
    }
    
    @Override
    public String toString() {
        return "GameCharacter{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", health=" + health +
                ", mana=" + mana +
                ", experience=" + experience +
                ", location='" + location + '\'' +
                '}';
    }
}