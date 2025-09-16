package cn.geekslife.designpattern.observer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 增强版抽象主题类 - 解决内存泄漏问题
 * 使用WeakReference维护观察者列表，避免内存泄漏
 */
public abstract class EnhancedSubject {
    // 使用WeakReference维护观察者列表，避免内存泄漏
    protected List<WeakReference<Observer>> observers = new ArrayList<>();
    
    // 添加观察者
    public void attach(Observer observer) {
        observers.add(new WeakReference<>(observer));
    }
    
    // 删除观察者
    public void detach(Observer observer) {
        Iterator<WeakReference<Observer>> iterator = observers.iterator();
        while (iterator.hasNext()) {
            WeakReference<Observer> weakRef = iterator.next();
            Observer obs = weakRef.get();
            if (obs == null || obs == observer) {
                iterator.remove();
            }
        }
    }
    
    // 清理已被垃圾回收的观察者
    protected void cleanUpObservers() {
        observers.removeIf(weakRef -> weakRef.get() == null);
    }
    
    // 通知所有观察者 - 增强版异常处理
    public void notifyObservers() {
        cleanUpObservers(); // 清理已被垃圾回收的观察者
        
        for (WeakReference<Observer> weakRef : observers) {
            Observer observer = weakRef.get();
            if (observer != null) {
                try {
                    observer.update(this);
                } catch (Exception e) {
                    // 记录异常但继续通知其他观察者
                    System.err.println("Observer notification failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}