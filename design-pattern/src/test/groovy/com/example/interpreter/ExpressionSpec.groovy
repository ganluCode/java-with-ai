package com.example.interpreter

import spock.lang.Specification

/**
 * 表达式接口测试类
 */
class ExpressionSpec extends Specification {
    
    def "表达式接口应该定义interpret方法"() {
        given:
        com.example.interpreter.Expression expression = Mock(com.example.interpreter.Expression)
        com.example.interpreter.Context context = new com.example.interpreter.Context()
        
        when:
        boolean result = expression.interpret(context)
        
        then:
        1 * expression.interpret(context)
    }
}