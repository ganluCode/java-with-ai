package com.example.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责人类
 * 负责保存备忘录对象
 */
public class Caretaker {
    private List<Memento> mementos;
    private int maxSize;
    
    /**
     * 构造函数
     */
    public Caretaker() {
        this.mementos = new ArrayList<>();
        this.maxSize = 10; // 默认最大保存10个备忘录
    }
    
    /**
     * 构造函数
     * @param maxSize 最大保存备忘录数量
     */
    public Caretaker(int maxSize) {
        this.mementos = new ArrayList<>();
        this.maxSize = maxSize;
    }
    
    /**
     * 添加备忘录
     * @param memento 备忘录对象
     */
    public void addMemento(Memento memento) {
        if (mementos.size() >= maxSize) {
            // 如果超过最大数量，移除最旧的备忘录
            mementos.remove(0);
        }
        mementos.add(memento);
    }
    
    /**
     * 获取指定索引的备忘录
     * @param index 索引
     * @return 备忘录对象
     */
    public Memento getMemento(int index) {
        if (index >= 0 && index < mementos.size()) {
            return mementos.get(index);
        }
        return null;
    }
    
    /**
     * 获取最新的备忘录
     * @return 备忘录对象
     */
    public Memento getLatestMemento() {
        if (!mementos.isEmpty()) {
            return mementos.get(mementos.size() - 1);
        }
        return null;
    }
    
    /**
     * 获取备忘录数量
     * @return 备忘录数量
     */
    public int getMementoCount() {
        return mementos.size();
    }
    
    /**
     * 清空所有备忘录
     */
    public void clear() {
        mementos.clear();
    }
    
    /**
     * 获取所有备忘录
     * @return 备忘录列表
     */
    public List<Memento> getAllMementos() {
        return new ArrayList<>(mementos);
    }
    
    /**
     * 移除指定索引的备忘录
     * @param index 索引
     * @return 被移除的备忘录
     */
    public Memento removeMemento(int index) {
        if (index >= 0 && index < mementos.size()) {
            return mementos.remove(index);
        }
        return null;
    }
}