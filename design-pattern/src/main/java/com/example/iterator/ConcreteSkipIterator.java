package com.example.iterator;

/**
 * 具体跳过迭代器实现
 * 支持跳过指定数量的元素进行遍历
 * @param <T> 元素类型
 */
public class ConcreteSkipIterator<T> implements SkipIterator<T> {
    private ConcreteAggregate<T> aggregate;
    private int current = 0;
    private int step = 1;
    
    public ConcreteSkipIterator(ConcreteAggregate<T> aggregate) {
        this.aggregate = aggregate;
    }
    
    public ConcreteSkipIterator(ConcreteAggregate<T> aggregate, int step) {
        this(aggregate);
        this.step = step;
    }
    
    @Override
    public void first() {
        current = 0;
    }
    
    @Override
    public void next() {
        current += step;
        if (current > aggregate.count()) {
            current = aggregate.count();
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
        return current >= aggregate.count() - step;
    }
    
    @Override
    public void setStep(int step) {
        if (step > 0) {
            this.step = step;
        }
    }
    
    @Override
    public int getStep() {
        return step;
    }
    
    @Override
    public void skip(int count) {
        current += count;
        if (current > aggregate.count()) {
            current = aggregate.count();
        }
    }
    
    public int getCurrentIndex() {
        return current;
    }
}