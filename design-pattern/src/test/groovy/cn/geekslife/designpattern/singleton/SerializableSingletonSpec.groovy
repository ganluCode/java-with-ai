package cn.geekslife.designpattern.singleton

import spock.lang.Specification

/**
 * 可序列化单例模式Spock测试类
 * 专门测试序列化对单例模式的影响
 */
class SerializableSingletonSpec extends Specification {
    
    def "可序列化单例应该在序列化和反序列化后保持唯一性"() {
        given: "获取可序列化单例实例"
        SerializableSingleton originalInstance = SerializableSingleton.getInstance()
        
        when: "序列化实例到字节数组"
        ByteArrayOutputStream bos = new ByteArrayOutputStream()
        ObjectOutputStream oos = new ObjectOutputStream(bos)
        oos.writeObject(originalInstance)
        oos.close()
        
        and: "从字节数组反序列化实例"
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray())
        ObjectInputStream ois = new ObjectInputStream(bis)
        SerializableSingleton deserializedInstance = (SerializableSingleton) ois.readObject()
        ois.close()
        
        then: "验证反序列化的实例与原实例相同"
        deserializedInstance.is(originalInstance)
        
        and: "验证实例不为null"
        deserializedInstance != null
    }
    
    def "可序列化单例应该能够执行业务逻辑"() {
        given: "获取可序列化单例实例"
        SerializableSingleton singleton = SerializableSingleton.getInstance()
        
        when: "执行业务逻辑"
        singleton.doSomething()
        
        then: "验证操作执行成功（无异常抛出）"
        true // 如果没有异常抛出，测试通过
    }
}