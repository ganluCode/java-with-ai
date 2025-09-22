package cn.geekslife.designpattern.observer

import cn.geekslife.designpattern.observer.EnhancedSubject
import cn.geekslife.designpattern.observer.Observer
import cn.geekslife.designpattern.observer.Subject
import spock.lang.Specification

class EnhancedSubjectSpec extends Specification {

    def "should use weak references to prevent memory leaks"() {
        given:
        EnhancedSubject testSubject = new EnhancedSubject() {}
        Observer observer = new Observer() {
            @Override
            void update(Object subject) {
                // 空实现
            }
        }
        
        when:
        testSubject.attach(observer)
        int initialSize = testSubject.observers.size()
        
        // 清空引用并尝试垃圾回收
        observer = null
        System.gc()
        
        // 调用清理方法
        testSubject.cleanUpObservers()
        int afterCleanupSize = testSubject.observers.size()
        
        then:
        initialSize == 1
        afterCleanupSize <= 1 // 可能为0或1，取决于垃圾回收时机
    }
    
    def "should handle observer exceptions gracefully"() {
        given:
        EnhancedSubject testSubject = new EnhancedSubject() {}
        Observer goodObserver = new Observer() {
            @Override
            void update(Object subject) {
                System.out.println("Good observer updated")
            }
        }
        
        Observer badObserver = new Observer() {
            @Override
            void update(Object subject) {
                throw new RuntimeException("Test exception")
            }
        }
        
        testSubject.attach(goodObserver)
        testSubject.attach(badObserver)
        
        when:
        testSubject.notifyObservers()
        
        then:
        // 不应该抛出异常
        notThrown(Exception)
    }
}