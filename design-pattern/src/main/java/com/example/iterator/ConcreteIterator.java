package com.example.iterator;

/**
 * 具体迭代器实现
 * 实现迭代器接口，负责对聚合对象的遍历
 * @param <T> 元素类型
 */
public class ConcreteIterator<T> implements Iterator<T> {
    private ConcreteAggregate<T> aggregate;
    private int current = 0;
    
    public ConcreteIterator(ConcreteAggregate<T> aggregate) {
        this.aggregate = aggregate;
    }
    
    @Override
    public void first() {
        current = 0;
    }
    
    @Override
    public void next() {
        if (current < aggregate.count()) {
            current++;
        }
    }
    
    @Override
    public boolean hasNext() {
        return current < aggregate.count();
    }
    
    @Override
    public T currentItem() {
        if (current >= 0 && current < aggregate.count()) {
            return aggregate.get(current);
        }
        return null;
    }
    
    @Override
    public boolean isFirst() {
        return current == 0;
    }
    
    @Override
    public boolean isLast() {
        return current == aggregate.count() - 1;
    }
    
    public int getCurrentIndex() {
        return current;
    }
}