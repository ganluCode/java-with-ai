package com.example.interpreter

import spock.lang.Specification

/**
 * 表达式解析器测试类
 */
class ExpressionParserSpec extends Specification {
    
    def "表达式解析器应该能够解析常量表达式"() {
        when:
        com.example.interpreter.Expression trueExpr = com.example.interpreter.ExpressionParser.parse("TRUE")
        com.example.interpreter.Expression falseExpr = com.example.interpreter.ExpressionParser.parse("FALSE")
        
        then:
        trueExpr instanceof com.example.interpreter.ConstantExpression
        falseExpr instanceof com.example.interpreter.ConstantExpression
    }
    
    def "表达式解析器应该能够解析变量表达式"() {
        when:
        com.example.interpreter.Expression varExpr = com.example.interpreter.ExpressionParser.parse("testVariable")
        
        then:
        varExpr instanceof com.example.interpreter.VariableExpression
        ((com.example.interpreter.VariableExpression) varExpr).getName() == "testVariable"
    }
    
    def "表达式解析器应该能够解析NOT表达式"() {
        when:
        com.example.interpreter.Expression notExpr = com.example.interpreter.ExpressionParser.parse("NOT TRUE")
        
        then:
        notExpr instanceof com.example.interpreter.NotExpression
    }
    
    def "表达式解析器应该能够解析复合表达式"() {
        when:
        com.example.interpreter.Expression andExpr = com.example.interpreter.ExpressionParser.parse("TRUE AND FALSE")
        com.example.interpreter.Expression orExpr = com.example.interpreter.ExpressionParser.parse("TRUE OR FALSE")
        
        then:
        andExpr instanceof com.example.interpreter.AndExpression
        orExpr instanceof com.example.interpreter.OrExpression
    }
    
    def "表达式解析器应该能够解析数学表达式"() {
        when:
        com.example.interpreter.MathExpression mathExpr = com.example.interpreter.ExpressionParser.parseMath("10 + 5")
        
        then:
        mathExpr instanceof com.example.interpreter.MathExpression
    }
}