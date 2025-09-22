package cn.geekslife.designpattern.interpreter

import cn.geekslife.designpattern.interpreter.ConstantExpression
import cn.geekslife.designpattern.interpreter.Context
import spock.lang.Specification

/**
 * 常量表达式测试类
 */
class ConstantExpressionSpec extends Specification {
    
    def "常量表达式应该返回固定的值"() {
        given:
        ConstantExpression trueExpr = new ConstantExpression(true)
        ConstantExpression falseExpr = new ConstantExpression(false)
        Context context = new Context()
        
        when:
        boolean trueResult = trueExpr.interpret(context)
        boolean falseResult = falseExpr.interpret(context)
        
        then:
        trueResult == true
        falseResult == false
    }
    
    def "常量表达式的toString方法应该返回正确的字符串表示"() {
        given:
        ConstantExpression trueExpr = new ConstantExpression(true)
        ConstantExpression falseExpr = new ConstantExpression(false)
        
        expect:
        trueExpr.toString() == "true"
        falseExpr.toString() == "false"
    }
}