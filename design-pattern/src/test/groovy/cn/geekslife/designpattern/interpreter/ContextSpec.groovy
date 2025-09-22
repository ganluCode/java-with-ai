package cn.geekslife.designpattern.interpreter

import cn.geekslife.designpattern.interpreter.Context
import spock.lang.Specification

/**
 * 上下文测试类
 */
class ContextSpec extends Specification {
    
    def "上下文应该能够创建和设置变量"() {
        given:
        Context context = new Context()
        
        when:
        context.setVariable("testVar", "testValue")
        
        then:
        context.hasVariable("testVar")
        context.getVariable("testVar") == "testValue"
    }
    
    def "上下文应该能够处理输入输出"() {
        given:
        Context context = new Context("test input")
        
        when:
        context.setOutput("test output")
        
        then:
        context.getInput() == "test input"
        context.getOutput() == "test output"
    }
    
    def "上下文应该能够处理不同类型的变量"() {
        given:
        Context context = new Context()
        
        when:
        context.setVariable("stringVar", "stringValue")
        context.setVariable("intVar", 42)
        context.setVariable("boolVar", true)
        
        then:
        context.getVariable("stringVar") == "stringValue"
        context.getVariable("intVar") == 42
        context.getVariable("boolVar") == true
    }
}