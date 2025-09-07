package com.example.interpreter

import spock.lang.Specification

/**
 * 与表达式测试类
 */
class AndExpressionSpec extends Specification {
    
    def "与表达式应该正确执行逻辑与操作"() {
        given:
        com.example.interpreter.ConstantExpression leftTrue = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.ConstantExpression leftFalse = new com.example.interpreter.ConstantExpression(false)
        com.example.interpreter.ConstantExpression rightTrue = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.ConstantExpression rightFalse = new com.example.interpreter.ConstantExpression(false)
        com.example.interpreter.Context context = new com.example.interpreter.Context()
        
        when:
        com.example.interpreter.AndExpression expr1 = new com.example.interpreter.AndExpression(leftTrue, rightTrue)
        com.example.interpreter.AndExpression expr2 = new com.example.interpreter.AndExpression(leftTrue, rightFalse)
        com.example.interpreter.AndExpression expr3 = new com.example.interpreter.AndExpression(leftFalse, rightTrue)
        com.example.interpreter.AndExpression expr4 = new com.example.interpreter.AndExpression(leftFalse, rightFalse)
        
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
        com.example.interpreter.ConstantExpression left = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.ConstantExpression right = new com.example.interpreter.ConstantExpression(false)
        com.example.interpreter.AndExpression andExpr = new com.example.interpreter.AndExpression(left, right)
        
        expect:
        andExpr.toString() == "(true AND false)"
    }
}