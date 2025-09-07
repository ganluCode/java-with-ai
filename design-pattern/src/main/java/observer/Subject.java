package observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象主题类
 * 定义了添加、删除和通知观察者的方法
 */
public abstract class Subject {
    // 维护观察者列表
    protected List<Observer> observers = new ArrayList<>();
    
    // 添加观察者
    public void attach(Observer observer) {
        observers.add(observer);
    }
    
    // 删除观察者
    public void detach(Observer observer) {
        observers.remove(observer);
    }
    
    // 通知所有观察者
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}