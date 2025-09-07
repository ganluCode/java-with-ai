package com.example.interpreter

import spock.lang.Specification

/**
 * 常量表达式测试类
 */
class ConstantExpressionSpec extends Specification {
    
    def "常量表达式应该返回固定的值"() {
        given:
        com.example.interpreter.ConstantExpression trueExpr = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.ConstantExpression falseExpr = new com.example.interpreter.ConstantExpression(false)
        com.example.interpreter.Context context = new com.example.interpreter.Context()
        
        when:
        boolean trueResult = trueExpr.interpret(context)
        boolean falseResult = falseExpr.interpret(context)
        
        then:
        trueResult == true
        falseResult == false
    }
    
    def "常量表达式的toString方法应该返回正确的字符串表示"() {
        given:
        com.example.interpreter.ConstantExpression trueExpr = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.ConstantExpression falseExpr = new com.example.interpreter.ConstantExpression(false)
        
        expect:
        trueExpr.toString() == "true"
        falseExpr.toString() == "false"
    }
}