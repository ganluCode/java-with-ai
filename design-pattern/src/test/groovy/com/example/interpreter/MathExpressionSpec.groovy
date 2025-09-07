package com.example.interpreter

import spock.lang.Specification

/**
 * 数学表达式测试类
 */
class MathExpressionSpec extends Specification {
    
    def "数学表达式应该能够计算简单算术表达式"() {
        given:
        com.example.interpreter.MathExpression expr1 = new com.example.interpreter.MathExpression("10 + 5")
        com.example.interpreter.MathExpression expr2 = new com.example.interpreter.MathExpression("10 - 5")
        com.example.interpreter.MathExpression expr3 = new com.example.interpreter.MathExpression("10 * 5")
        com.example.interpreter.MathExpression expr4 = new com.example.interpreter.MathExpression("10 / 5")
        
        when:
        double result1 = expr1.evaluate()
        double result2 = expr2.evaluate()
        double result3 = expr3.evaluate()
        double result4 = expr4.evaluate()
        
        then:
        result1 == 15.0
        result2 == 5.0
        result3 == 50.0
        result4 == 2.0
    }
    
    def "数学表达式应该能够处理带括号的表达式"() {
        given:
        com.example.interpreter.MathExpression expr = new com.example.interpreter.MathExpression("(10 + 5) * 2")
        
        when:
        double result = expr.evaluate()
        
        then:
        result == 30.0
    }
    
    def "数学表达式应该能够处理变量"() {
        given:
        com.example.interpreter.MathExpression expr = new com.example.interpreter.MathExpression("a + b * c")
        expr.setVariable("a", 10)
        expr.setVariable("b", 5)
        expr.setVariable("c", 2)
        
        when:
        double result = expr.evaluate()
        
        then:
        result == 20.0  // 10 + 5 * 2 = 10 + 10 = 20
    }
    
    def "数学表达式应该能够与上下文交互"() {
        given:
        com.example.interpreter.MathExpression expr = new com.example.interpreter.MathExpression("10 + 5")
        com.example.interpreter.Context context = new com.example.interpreter.Context()
        
        when:
        boolean boolResult = expr.interpret(context)
        String output = context.getOutput()
        
        then:
        boolResult == true  // 非零结果为true
        output == "15.0"
    }
}