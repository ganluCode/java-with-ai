package cn.geekslife.designpattern.interpreter

import cn.geekslife.designpattern.interpreter.AndExpression
import cn.geekslife.designpattern.interpreter.ConstantExpression
import cn.geekslife.designpattern.interpreter.Context
import spock.lang.Specification

/**
 * 与表达式测试类
 */
class AndExpressionSpec extends Specification {
    
    def "与表达式应该正确执行逻辑与操作"() {
        given:
        ConstantExpression leftTrue = new ConstantExpression(true)
        ConstantExpression leftFalse = new ConstantExpression(false)
        ConstantExpression rightTrue = new ConstantExpression(true)
        ConstantExpression rightFalse = new ConstantExpression(false)
        Context context = new Context()
        
        when:
        AndExpression expr1 = new AndExpression(leftTrue, rightTrue)
        AndExpression expr2 = new AndExpression(leftTrue, rightFalse)
        AndExpression expr3 = new AndExpression(leftFalse, rightTrue)
        AndExpression expr4 = new AndExpression(leftFalse, rightFalse)
        
        boolean result1 = expr1.interpret(context)
        boolean result2 = expr2.interpret(context)
        boolean result3 = expr3.interpret(context)
        boolean result4 = expr4.interpret(context)
        
        then:
        result1 == true   // true AND true = true
        result2 == false  // true AND false = false
        result3 == false  // false AND true = false
        result4 == false  // false AND false = false
    }
    
    def "与表达式的toString方法应该返回正确的字符串表示"() {
        given:
        ConstantExpression left = new ConstantExpression(true)
        ConstantExpression right = new ConstantExpression(false)
        AndExpression andExpr = new AndExpression(left, right)
        
        expect:
        andExpr.toString() == "(true AND false)"
    }
}