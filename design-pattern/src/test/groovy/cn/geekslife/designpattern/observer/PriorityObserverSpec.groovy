package cn.geekslife.designpattern.observer

import cn.geekslife.designpattern.observer.PriorityObserver
import cn.geekslife.designpattern.observer.PrioritySubject
import cn.geekslife.designpattern.observer.Subject
import spock.lang.Specification

class PriorityObserverSpec extends Specification {

    def "should notify observers in priority order"() {
        given:
        PrioritySubject subject = new PrioritySubject() {}
        
        // 创建不同优先级的观察者
        TestPriorityObserver highPriority = new TestPriorityObserver(1)
        TestPriorityObserver mediumPriority = new TestPriorityObserver(5)
        TestPriorityObserver lowPriority = new TestPriorityObserver(10)
        
        subject.attach(highPriority)
        subject.attach(mediumPriority)
        subject.attach(lowPriority)
        
        when:
        subject.notifyObservers()
        
        then:
        // 验证通知顺序
        highPriority.updateCount == 1
        mediumPriority.updateCount == 1
        lowPriority.updateCount == 1
    }
    
    // 测试用的优先级观察者实现
    static class TestPriorityObserver implements PriorityObserver {
        private int priority
        int updateCount = 0
        
        TestPriorityObserver(int priority) {
            this.priority = priority
        }
        
        @Override
        void update(Object subject) {
            updateCount++
        }
        
        @Override
        int getPriority() {
            return priority
        }
    }
}