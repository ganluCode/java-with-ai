package cn.geekslife.designpattern.interpreter

import cn.geekslife.designpattern.interpreter.ConstantExpression
import cn.geekslife.designpattern.interpreter.Context
import cn.geekslife.designpattern.interpreter.RuleExpression
import spock.lang.Specification

/**
 * 规则表达式测试类
 */
class RuleExpressionSpec extends Specification {
    
    def "规则表达式应该能够执行条件判断"() {
        given:
        ConstantExpression condition = new ConstantExpression(true)
        RuleExpression rule = new RuleExpression("testRule", condition, "testAction")
        Context context = new Context()
        
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
        ConstantExpression condition = new ConstantExpression(false)
        RuleExpression rule = new RuleExpression("testRule", condition, "testAction")
        Context context = new Context()
        
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
        ConstantExpression condition = new ConstantExpression(true)
        RuleExpression rule = new RuleExpression("testRule", condition, "testAction")
        
        expect:
        rule.getRuleName() == "testRule"
        rule.getCondition() == condition
        rule.getAction() == "testAction"
    }
}