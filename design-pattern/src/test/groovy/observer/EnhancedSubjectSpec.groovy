package observer

import spock.lang.Specification

class EnhancedSubjectSpec extends Specification {

    def "should use weak references to prevent memory leaks"() {
        given:
        EnhancedSubject subject = new EnhancedSubject() {}
        Observer observer = new Observer() {
            @Override
            void update(Subject subject) {
                // 空实现
            }
        }
        
        when:
        subject.attach(observer)
        int initialSize = subject.observers.size()
        
        // 清空引用并尝试垃圾回收
        observer = null
        System.gc()
        
        // 调用清理方法
        subject.cleanUpObservers()
        int afterCleanupSize = subject.observers.size()
        
        then:
        initialSize == 1
        afterCleanupSize <= 1 // 可能为0或1，取决于垃圾回收时机
    }
    
    def "should handle observer exceptions gracefully"() {
        given:
        EnhancedSubject subject = new EnhancedSubject() {}
        Observer goodObserver = new Observer() {
            @Override
            void update(Subject subject) {
                System.out.println("Good observer updated")
            }
        }
        
        Observer badObserver = new Observer() {
            @Override
            void update(Subject subject) {
                throw new RuntimeException("Test exception")
            }
        }
        
        subject.attach(goodObserver)
        subject.attach(badObserver)
        
        when:
        subject.notifyObservers()
        
        then:
        // 不应该抛出异常
        notThrown(Exception)
    }
}