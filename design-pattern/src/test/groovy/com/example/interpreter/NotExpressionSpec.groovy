package com.example.interpreter

import spock.lang.Specification

/**
 * 非表达式测试类
 */
class NotExpressionSpec extends Specification {
    
    def "非表达式应该正确执行逻辑非操作"() {
        given:
        com.example.interpreter.ConstantExpression trueExpr = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.ConstantExpression falseExpr = new com.example.interpreter.ConstantExpression(false)
        com.example.interpreter.Context context = new com.example.interpreter.Context()
        
        when:
        com.example.interpreter.NotExpression notTrue = new com.example.interpreter.NotExpression(trueExpr)
        com.example.interpreter.NotExpression notFalse = new com.example.interpreter.NotExpression(falseExpr)
        
        boolean result1 = notTrue.interpret(context)
        boolean result2 = notFalse.interpret(context)
        
        then:
        result1 == false  // NOT true = false
        result2 == true   // NOT false = true
    }
    
    def "非表达式的toString方法应该返回正确的字符串表示"() {
        given:
        com.example.interpreter.ConstantExpression expr = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.NotExpression notExpr = new com.example.interpreter.NotExpression(expr)
        
        expect:
        notExpr.toString() == "(NOT true)"
    }
}