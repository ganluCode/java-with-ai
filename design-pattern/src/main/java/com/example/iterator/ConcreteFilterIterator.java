package com.example.iterator;

import java.util.function.Predicate;

/**
 * 具体过滤迭代器实现
 * 支持根据条件过滤元素进行遍历
 * @param <T> 元素类型
 */
public class ConcreteFilterIterator<T> implements FilterIterator<T> {
    private ConcreteAggregate<T> aggregate;
    private Predicate<T> filter;
    private int current = 0;
    
    public ConcreteFilterIterator(ConcreteAggregate<T> aggregate) {
        this.aggregate = aggregate;
    }
    
    public ConcreteFilterIterator(ConcreteAggregate<T> aggregate, Predicate<T> filter) {
        this(aggregate);
        this.filter = filter;
    }
    
    @Override
    public void first() {
        current = 0;
        // 跳过不满足条件的元素
        while (current < aggregate.count() && !testCurrent()) {
            current++;
        }
    }
    
    @Override
    public void next() {
        if (current < aggregate.count()) {
            current++;
            // 跳过不满足条件的元素
            while (current < aggregate.count() && !testCurrent()) {
                current++;
            }
        }
    }
    
    @Override
    public boolean hasNext() {
        return current < aggregate.count();
    }
    
    @Override
    public T currentItem() {
        if (current >= 0 && current < aggregate.count() && testCurrent()) {
            return aggregate.get(current);
        }
        return null;
    }
    
    @Override
    public boolean isFirst() {
        return current == 0 && testCurrent();
    }
    
    @Override
    public boolean isLast() {
        if (current >= aggregate.count()) return false;
        
        // 检查后面是否还有满足条件的元素
        for (int i = current + 1; i < aggregate.count(); i++) {
            T item = aggregate.get(i);
            if (filter == null || filter.test(item)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void setFilter(Predicate<T> filter) {
        this.filter = filter;
    }
    
    @Override
    public Predicate<T> getFilter() {
        return filter;
    }
    
    @Override
    public void reset() {
        current = 0;
    }
    
    private boolean testCurrent() {
        if (current >= aggregate.count()) return false;
        T item = aggregate.get(current);
        return filter == null || filter.test(item);
    }
    
    public int getCurrentIndex() {
        return current;
    }
}