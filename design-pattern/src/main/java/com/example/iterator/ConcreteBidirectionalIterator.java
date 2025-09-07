package com.example.iterator;

/**
 * 具体双向迭代器实现
 * 支持正向和反向遍历
 * @param <T> 元素类型
 */
public class ConcreteBidirectionalIterator<T> implements BidirectionalIterator<T> {
    private ConcreteAggregate<T> aggregate;
    private int current = 0;
    
    public ConcreteBidirectionalIterator(ConcreteAggregate<T> aggregate) {
        this.aggregate = aggregate;
        this.current = 0;
    }
    
    @Override
    public void first() {
        current = 0;
    }
    
    @Override
    public void last() {
        current = aggregate.count() - 1;
    }
    
    @Override
    public void next() {
        if (current < aggregate.count()) {
            current++;
        }
    }
    
    @Override
    public void previous() {
        if (current > 0) {
            current--;
        }
    }
    
    @Override
    public boolean hasNext() {
        return current < aggregate.count();
    }
    
    @Override
    public boolean hasPrevious() {
        return current > 0;
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