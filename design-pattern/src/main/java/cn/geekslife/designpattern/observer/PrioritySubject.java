package cn.geekslife.designpattern.observer;

import java.lang.ref.WeakReference;
import java.util.PriorityQueue;

/**
 * 支持优先级通知的主题类
 * 按观察者优先级顺序通知
 */
public abstract class PrioritySubject extends EnhancedSubject {
    // 重写通知方法，按优先级顺序通知
    @Override
    public void notifyObservers() {
        cleanUpObservers(); // 清理已被垃圾回收的观察者
        
        // 使用优先队列按优先级排序观察者
        PriorityQueue<PriorityObserverWrapper> priorityQueue = new PriorityQueue<>();
        
        // 将支持优先级的观察者加入优先队列
        for (WeakReference<Observer> weakRef : observers) {
            Observer observer = weakRef.get();
            if (observer instanceof PriorityObserver) {
                priorityQueue.offer(new PriorityObserverWrapper(
                    (PriorityObserver) observer, 
                    ((PriorityObserver) observer).getPriority()
                ));
            }
        }
        
        // 按优先级顺序通知观察者
        while (!priorityQueue.isEmpty()) {
            PriorityObserverWrapper wrapper = priorityQueue.poll();
            try {
                wrapper.observer.update(this);
            } catch (Exception e) {
                System.err.println("Priority observer notification failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // 通知不支持优先级的普通观察者
        for (WeakReference<Observer> weakRef : observers) {
            Observer observer = weakRef.get();
            if (observer != null && !(observer instanceof PriorityObserver)) {
                try {
                    observer.update(this);
                } catch (Exception e) {
                    System.err.println("Observer notification failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    
    // 优先级观察者包装类
    private static class PriorityObserverWrapper implements Comparable<PriorityObserverWrapper> {
        final PriorityObserver observer;
        final int priority;
        
        PriorityObserverWrapper(PriorityObserver observer, int priority) {
            this.observer = observer;
            this.priority = priority;
        }
        
        @Override
        public int compareTo(PriorityObserverWrapper other) {
            return Integer.compare(this.priority, other.priority);
        }
    }
}