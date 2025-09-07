package com.example.interpreter

import spock.lang.Specification

/**
 * 表达式工厂测试类
 */
class ExpressionFactorySpec extends Specification {
    
    def "表达式工厂应该能够创建各种类型的表达式"() {
        when:
        com.example.interpreter.Expression constantExpr = com.example.interpreter.ExpressionFactory.createConstant(true)
        com.example.interpreter.Expression variableExpr = com.example.interpreter.ExpressionFactory.createVariable("testVar")
        com.example.interpreter.Expression andExpr = com.example.interpreter.ExpressionFactory.createAnd(constantExpr, variableExpr)
        com.example.interpreter.Expression orExpr = com.example.interpreter.ExpressionFactory.createOr(constantExpr, variableExpr)
        com.example.interpreter.Expression notExpr = com.example.interpreter.ExpressionFactory.createNot(constantExpr)
        
        then:
        constantExpr instanceof com.example.interpreter.ConstantExpression
        variableExpr instanceof com.example.interpreter.VariableExpression
        andExpr instanceof com.example.interpreter.AndExpression
        orExpr instanceof com.example.interpreter.OrExpression
        notExpr instanceof com.example.interpreter.NotExpression
    }
    
    def "表达式工厂应该能够创建复合表达式"() {
        given:
        com.example.interpreter.Expression left = com.example.interpreter.ExpressionFactory.createVariable("a")
        com.example.interpreter.Expression right = com.example.interpreter.ExpressionFactory.createVariable("b")
        
        when:
        com.example.interpreter.Expression equalExpr = com.example.interpreter.ExpressionFactory.createEqual(left, right)
        com.example.interpreter.RuleExpression ruleExpr = com.example.interpreter.ExpressionFactory.createRule("testRule", left, "testAction")
        
        then:
        equalExpr instanceof com.example.interpreter.EqualExpression
        ruleExpr instanceof com.example.interpreter.RuleExpression
        ruleExpr.getRuleName() == "testRule"
    }
    
    def "表达式工厂应该能够创建数学和SQL表达式"() {
        when:
        com.example.interpreter.MathExpression mathExpr = com.example.interpreter.ExpressionFactory.createMath("10 + 5")
        java.util.List<String> columns = Arrays.asList("name")
        com.example.interpreter.SQLExpression sqlExpr = com.example.interpreter.ExpressionFactory.createSQL("users", columns, null)
        
        then:
        mathExpr instanceof com.example.interpreter.MathExpression
        sqlExpr instanceof com.example.interpreter.SQLExpression
    }
}