package cn.geekslife.designpattern.behavioral;

/**
 * ObserverPattern - 观察者模式示例
 */
public class ObserverPattern {
    
    // 主题接口
    public interface Subject {
        void addObserver(Observer observer);
        void removeObserver(Observer observer);
        void notifyObservers();
    }
    
    // 观察者接口
    public interface Observer {
        void update();
    }
    
    // 具体主题
    public static class ConcreteSubject implements Subject {
        private java.util.List<Observer> observers = new java.util.ArrayList<>();
        
        @Override
        public void addObserver(Observer observer) {
            observers.add(observer);
        }
        
        @Override
        public void removeObserver(Observer observer) {
            observers.remove(observer);
        }
        
        @Override
        public void notifyObservers() {
            for (Observer observer : observers) {
                observer.update();
            }
        }
    }
}