package com.example.interpreter

import spock.lang.Specification

/**
 * 规则引擎测试类
 */
class RuleEngineSpec extends Specification {
    
    def "规则引擎应该能够添加和执行规则"() {
        given:
        com.example.interpreter.RuleEngine ruleEngine = new com.example.interpreter.RuleEngine()
        com.example.interpreter.ConstantExpression condition = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.Context context = new com.example.interpreter.Context()
        
        when:
        ruleEngine.addRule("testRule", condition, "testAction")
        ruleEngine.executeRules(context)
        
        then:
        ruleEngine.getRules().size() == 1
        ruleEngine.getRules().get(0).getRuleName() == "testRule"
    }
    
    def "规则引擎应该能够执行特定规则"() {
        given:
        com.example.interpreter.RuleEngine ruleEngine = new com.example.interpreter.RuleEngine()
        com.example.interpreter.ConstantExpression condition = new com.example.interpreter.ConstantExpression(true)
        com.example.interpreter.Context context = new com.example.interpreter.Context()
        
        ruleEngine.addRule("rule1", condition, "action1")
        ruleEngine.addRule("rule2", condition, "action2")
        
        when:
        ruleEngine.executeRule("rule1", context)
        
        then:
        noExceptionThrown()
    }
    
    def "规则引擎应该能够清空规则"() {
        given:
        com.example.interpreter.RuleEngine ruleEngine = new com.example.interpreter.RuleEngine()
        com.example.interpreter.ConstantExpression condition = new com.example.interpreter.ConstantExpression(true)
        
        ruleEngine.addRule("testRule", condition, "testAction")
        
        when:
        ruleEngine.clearRules()
        
        then:
        ruleEngine.getRules().size() == 0
    }
}