package com.example.interpreter

import spock.lang.Specification

/**
 * 规则表达式测试类
 */
class RuleExpressionSpec extends Specification {
    
    def "规则表达式应该能够执行条件判断"() {
        given:
        com.example.interpreter.ConstantExpression condition = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.RuleExpression rule = new com.example.interpreter.RuleExpression("testRule", condition, "testAction")
        com.example.interpreter.Context context = new com.example.interpreter.Context()
        
        when:
        boolean result = rule.interpret(context)
        String output = context.getOutput()
        
        then:
        result == true
        output != null
        output.contains("规则 testRule 已执行")
    }
    
    def "规则表达式在条件为假时应该不执行动作"() {
        given:
        com.example.interpreter.ConstantExpression condition = new com.example.interpreter.ConstantExpression(false)
        com.example.interpreter.RuleExpression rule = new com.example.interpreter.RuleExpression("testRule", condition, "testAction")
        com.example.interpreter.Context context = new com.example.interpreter.Context()
        
        when:
        boolean result = rule.interpret(context)
        String output = context.getOutput()
        
        then:
        result == false
        output != null
        output.contains("规则 testRule 条件不满足")
    }
    
    def "规则表达式应该提供正确的访问方法"() {
        given:
        com.example.interpreter.ConstantExpression condition = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.RuleExpression rule = new com.example.interpreter.RuleExpression("testRule", condition, "testAction")
        
        expect:
        rule.getRuleName() == "testRule"
        rule.getCondition() == condition
        rule.getAction() == "testAction"
    }
}