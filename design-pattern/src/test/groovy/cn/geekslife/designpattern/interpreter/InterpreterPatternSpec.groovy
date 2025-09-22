package cn.geekslife.designpattern.interpreter

import cn.geekslife.designpattern.interpreter.AndExpression
import cn.geekslife.designpattern.interpreter.ConstantExpression
import cn.geekslife.designpattern.interpreter.Context
import cn.geekslife.designpattern.interpreter.Expression
import cn.geekslife.designpattern.interpreter.MathExpression
import cn.geekslife.designpattern.interpreter.NotExpression
import cn.geekslife.designpattern.interpreter.OrExpression
import cn.geekslife.designpattern.interpreter.RuleExpression
import cn.geekslife.designpattern.interpreter.SQLExpression
import cn.geekslife.designpattern.interpreter.VariableExpression
import spock.lang.Specification

/**
 * 解释器模式综合测试类
 */
class InterpreterPatternSpec extends Specification {
    
    def "所有表达式都应该实现Expression接口"() {
        given:
        Expression constantExpr = new ConstantExpression(true)
        Expression variableExpr = new VariableExpression("test")
        Expression andExpr = new AndExpression(constantExpr, variableExpr)
        Expression orExpr = new OrExpression(constantExpr, variableExpr)
        Expression notExpr = new NotExpression(constantExpr)
        MathExpression mathExpr = new MathExpression("10 + 5")
        RuleExpression ruleExpr = new RuleExpression("testRule", constantExpr, "testAction")
        SQLExpression sqlExpr = new SQLExpression("users", Arrays.asList("name"), constantExpr)
        
        expect:
        constantExpr instanceof Expression
        variableExpr instanceof Expression
        andExpr instanceof Expression
        orExpr instanceof Expression
        notExpr instanceof Expression
        mathExpr instanceof Expression
        ruleExpr instanceof Expression
        sqlExpr instanceof Expression
    }
    
    def "解释器模式应该支持完整的表达式组合"() {
        given:
        Context context = new Context()
        context.setVariable("x", true)
        context.setVariable("y", false)
        
        // 创建复杂表达式: (x AND TRUE) OR (y AND FALSE)
        VariableExpression x = new VariableExpression("x")
        VariableExpression y = new VariableExpression("y")
        ConstantExpression trueConst = new ConstantExpression(true)
        ConstantExpression falseConst = new ConstantExpression(false)
        
        AndExpression leftAnd = new AndExpression(x, trueConst)
        AndExpression rightAnd = new AndExpression(y, falseConst)
        OrExpression complexExpr = new OrExpression(leftAnd, rightAnd)
        
        when:
        boolean result = complexExpr.interpret(context)
        
        then:
        result == true  // (true AND true) OR (false AND false) = true OR false = true
    }
    
    def "解释器模式应该支持数学表达式计算"() {
        given:
        MathExpression expr = new MathExpression("(10 + 5) * 2 - 5")
        Context context = new Context()
        
        when:
        boolean boolResult = expr.interpret(context)
        String output = context.getOutput()
        
        then:
        boolResult == true  // 25 != 0
        output == "25.0"
    }
}