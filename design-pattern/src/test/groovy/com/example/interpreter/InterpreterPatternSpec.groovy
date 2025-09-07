package com.example.interpreter

import spock.lang.Specification

/**
 * 解释器模式综合测试类
 */
class InterpreterPatternSpec extends Specification {
    
    def "所有表达式都应该实现Expression接口"() {
        given:
        com.example.interpreter.Expression constantExpr = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.Expression variableExpr = new com.example.interpreter.VariableExpression("test")
        com.example.interpreter.Expression andExpr = new com.example.interpreter.AndExpression(constantExpr, variableExpr)
        com.example.interpreter.Expression orExpr = new com.example.interpreter.OrExpression(constantExpr, variableExpr)
        com.example.interpreter.Expression notExpr = new com.example.interpreter.NotExpression(constantExpr)
        com.example.interpreter.MathExpression mathExpr = new com.example.interpreter.MathExpression("10 + 5")
        com.example.interpreter.RuleExpression ruleExpr = new com.example.interpreter.RuleExpression("testRule", constantExpr, "testAction")
        com.example.interpreter.SQLExpression sqlExpr = new com.example.interpreter.SQLExpression("users", Arrays.asList("name"), constantExpr)
        
        expect:
        constantExpr instanceof com.example.interpreter.Expression
        variableExpr instanceof com.example.interpreter.Expression
        andExpr instanceof com.example.interpreter.Expression
        orExpr instanceof com.example.interpreter.Expression
        notExpr instanceof com.example.interpreter.Expression
        mathExpr instanceof com.example.interpreter.Expression
        ruleExpr instanceof com.example.interpreter.Expression
        sqlExpr instanceof com.example.interpreter.Expression
    }
    
    def "解释器模式应该支持完整的表达式组合"() {
        given:
        com.example.interpreter.Context context = new com.example.interpreter.Context()
        context.setVariable("x", true)
        context.setVariable("y", false)
        
        // 创建复杂表达式: (x AND TRUE) OR (y AND FALSE)
        com.example.interpreter.VariableExpression x = new com.example.interpreter.VariableExpression("x")
        com.example.interpreter.VariableExpression y = new com.example.interpreter.VariableExpression("y")
        com.example.interpreter.ConstantExpression trueConst = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.ConstantExpression falseConst = new com.example.interpreter.ConstantExpression(false)
        
        com.example.interpreter.AndExpression leftAnd = new com.example.interpreter.AndExpression(x, trueConst)
        com.example.interpreter.AndExpression rightAnd = new com.example.interpreter.AndExpression(y, falseConst)
        com.example.interpreter.OrExpression complexExpr = new com.example.interpreter.OrExpression(leftAnd, rightAnd)
        
        when:
        boolean result = complexExpr.interpret(context)
        
        then:
        result == true  // (true AND true) OR (false AND false) = true OR false = true
    }
    
    def "解释器模式应该支持数学表达式计算"() {
        given:
        com.example.interpreter.MathExpression expr = new com.example.interpreter.MathExpression("(10 + 5) * 2 - 5")
        com.example.interpreter.Context context = new com.example.interpreter.Context()
        
        when:
        boolean boolResult = expr.interpret(context)
        String output = context.getOutput()
        
        then:
        boolResult == true  // 25 != 0
        output == "25.0"
    }
}