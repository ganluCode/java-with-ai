package cn.geekslife.designpattern.interpreter

import cn.geekslife.designpattern.interpreter.Context
import cn.geekslife.designpattern.interpreter.VariableExpression
import spock.lang.Specification

/**
 * 变量表达式测试类
 */
class VariableExpressionSpec extends Specification {
    
    def "变量表达式应该能够从上下文中获取布尔值"() {
        given:
        VariableExpression varExpr = new VariableExpression("testVar")
        Context context = new Context()
        
        when:
        context.setVariable("testVar", true)
        boolean result1 = varExpr.interpret(context)
        
        context.setVariable("testVar", false)
        boolean result2 = varExpr.interpret(context)
        
        then:
        result1 == true
        result2 == false
    }
    
    def "变量表达式在变量不存在时应该返回false"() {
        given:
        VariableExpression varExpr = new VariableExpression("nonExistentVar")
        Context context = new Context()
        
        when:
        boolean result = varExpr.interpret(context)
        
        then:
        result == false
    }
    
    def "变量表达式的toString方法应该返回变量名"() {
        given:
        VariableExpression varExpr = new VariableExpression("testVariable")
        
        expect:
        varExpr.toString() == "testVariable"
        varExpr.getName() == "testVariable"
    }
}