package cn.geekslife.designpattern.interpreter

import cn.geekslife.designpattern.interpreter.ConstantExpression
import cn.geekslife.designpattern.interpreter.Context
import cn.geekslife.designpattern.interpreter.OrExpression
import spock.lang.Specification

/**
 * 或表达式测试类
 */
class OrExpressionSpec extends Specification {
    
    def "或表达式应该正确执行逻辑或操作"() {
        given:
        ConstantExpression leftTrue = new ConstantExpression(true)
        ConstantExpression leftFalse = new ConstantExpression(false)
        ConstantExpression rightTrue = new ConstantExpression(true)
        ConstantExpression rightFalse = new ConstantExpression(false)
        Context context = new Context()
        
        when:
        OrExpression expr1 = new OrExpression(leftTrue, rightTrue)
        OrExpression expr2 = new OrExpression(leftTrue, rightFalse)
        OrExpression expr3 = new OrExpression(leftFalse, rightTrue)
        OrExpression expr4 = new OrExpression(leftFalse, rightFalse)
        
        boolean result1 = expr1.interpret(context)
        boolean result2 = expr2.interpret(context)
        boolean result3 = expr3.interpret(context)
        boolean result4 = expr4.interpret(context)
        
        then:
        result1 == true   // true OR true = true
        result2 == true   // true OR false = true
        result3 == true   // false OR true = true
        result4 == false  // false OR false = false
    }
    
    def "或表达式的toString方法应该返回正确的字符串表示"() {
        given:
        ConstantExpression left = new ConstantExpression(true)
        ConstantExpression right = new ConstantExpression(false)
        OrExpression orExpr = new OrExpression(left, right)
        
        expect:
        orExpr.toString() == "(true OR false)"
    }
}