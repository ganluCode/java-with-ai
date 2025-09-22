package cn.geekslife.designpattern.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * 具体聚合实现
 * 实现聚合接口，持有聚合对象的数据
 * @param <T> 元素类型
 */
public class ConcreteAggregate<T> implements Aggregate<T> {
    private List<T> items = new ArrayList<>();
    
    @Override
    public Iterator<T> createIterator() {
        return new ConcreteIterator<>(this);
    }
    
    @Override
    public int count() {
        return items.size();
    }
    
    @Override
    public T get(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }
    
    @Override
    public void set(int index, T item) {
        if (index >= 0 && index < items.size()) {
            items.set(index, item);
        }
    }
    
    @Override
    public void add(T item) {
        items.add(item);
    }
    
    @Override
    public boolean remove(T item) {
        return items.remove(item);
    }
    
    @Override
    public void clear() {
        items.clear();
    }
    
    public List<T> getItems() {
        return new ArrayList<>(items);
    }
}