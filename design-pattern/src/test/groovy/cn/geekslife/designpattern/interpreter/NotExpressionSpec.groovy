package cn.geekslife.designpattern.interpreter

import cn.geekslife.designpattern.interpreter.ConstantExpression
import cn.geekslife.designpattern.interpreter.Context
import cn.geekslife.designpattern.interpreter.NotExpression
import spock.lang.Specification

/**
 * 非表达式测试类
 */
class NotExpressionSpec extends Specification {
    
    def "非表达式应该正确执行逻辑非操作"() {
        given:
        ConstantExpression trueExpr = new ConstantExpression(true)
        ConstantExpression falseExpr = new ConstantExpression(false)
        Context context = new Context()
        
        when:
        NotExpression notTrue = new NotExpression(trueExpr)
        NotExpression notFalse = new NotExpression(falseExpr)
        
        boolean result1 = notTrue.interpret(context)
        boolean result2 = notFalse.interpret(context)
        
        then:
        result1 == false  // NOT true = false
        result2 == true   // NOT false = true
    }
    
    def "非表达式的toString方法应该返回正确的字符串表示"() {
        given:
        ConstantExpression expr = new ConstantExpression(true)
        NotExpression notExpr = new NotExpression(expr)
        
        expect:
        notExpr.toString() == "(NOT true)"
    }
}