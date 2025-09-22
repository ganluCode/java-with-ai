package cn.geekslife.designpattern.iterator;

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
        if (aggregate.count() > 0) {
            current = aggregate.count() - 1;
        } else {
            current = -1; // 空集合时设置为-1，表示无效位置
        }
    }

    @Override
    public void next() {
        if (current < aggregate.count()) {
            current++;
        }
    }

    @Override
    public void previous() {
        if (current >= 0) {
            current--;
        }
    }

    @Override
    public boolean hasNext() {
        // 空集合没有前一个元素
        if (aggregate.count() == 0) {
            return false;
        }
        return current != aggregate.count();
    }

    @Override
    public boolean hasPrevious() {
        // 空集合没有前一个元素
        if (aggregate.count() == 0) {
            return false;
        }
        // current > 0时才有前一个元素
        return current > -1;
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
        // 对于空集合，既是first也是last
        if (aggregate.count() == 0) {
            return true;
        }
        return current >= 0 && current == aggregate.count() - 1;
    }

    public int getCurrentIndex() {
        return current;
    }
}