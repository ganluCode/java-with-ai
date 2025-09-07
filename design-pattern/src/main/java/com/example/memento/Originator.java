package com.example.memento;

/**
 * 原发器接口
 * 定义创建备忘录和从备忘录恢复状态的方法
 */
public interface Originator {
    /**
     * 创建备忘录
     * @param description 状态描述
     * @return 备忘录对象
     */
    Memento createMemento(String description);
    
    /**
     * 从备忘录恢复状态
     * @param memento 备忘录对象
     */
    void restoreMemento(Memento memento);
}