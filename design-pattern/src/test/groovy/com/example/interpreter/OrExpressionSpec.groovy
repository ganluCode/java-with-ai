package com.example.interpreter

import spock.lang.Specification

/**
 * 或表达式测试类
 */
class OrExpressionSpec extends Specification {
    
    def "或表达式应该正确执行逻辑或操作"() {
        given:
        com.example.interpreter.ConstantExpression leftTrue = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.ConstantExpression leftFalse = new com.example.interpreter.ConstantExpression(false)
        com.example.interpreter.ConstantExpression rightTrue = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.ConstantExpression rightFalse = new com.example.interpreter.ConstantExpression(false)
        com.example.interpreter.Context context = new com.example.interpreter.Context()
        
        when:
        com.example.interpreter.OrExpression expr1 = new com.example.interpreter.OrExpression(leftTrue, rightTrue)
        com.example.interpreter.OrExpression expr2 = new com.example.interpreter.OrExpression(leftTrue, rightFalse)
        com.example.interpreter.OrExpression expr3 = new com.example.interpreter.OrExpression(leftFalse, rightTrue)
        com.example.interpreter.OrExpression expr4 = new com.example.interpreter.OrExpression(leftFalse, rightFalse)
        
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
        com.example.interpreter.ConstantExpression left = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.ConstantExpression right = new com.example.interpreter.ConstantExpression(false)
        com.example.interpreter.OrExpression orExpr = new com.example.interpreter.OrExpression(left, right)
        
        expect:
        orExpr.toString() == "(true OR false)"
    }
}