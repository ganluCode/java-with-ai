package cn.geekslife.designpattern.interpreter

import cn.geekslife.designpattern.interpreter.AndExpression
import cn.geekslife.designpattern.interpreter.ConstantExpression
import cn.geekslife.designpattern.interpreter.EqualExpression
import cn.geekslife.designpattern.interpreter.Expression
import cn.geekslife.designpattern.interpreter.ExpressionFactory
import cn.geekslife.designpattern.interpreter.MathExpression
import cn.geekslife.designpattern.interpreter.NotExpression
import cn.geekslife.designpattern.interpreter.OrExpression
import cn.geekslife.designpattern.interpreter.RuleExpression
import cn.geekslife.designpattern.interpreter.SQLExpression
import cn.geekslife.designpattern.interpreter.VariableExpression
import spock.lang.Specification

/**
 * 表达式工厂测试类
 */
class ExpressionFactorySpec extends Specification {
    
    def "表达式工厂应该能够创建各种类型的表达式"() {
        when:
        Expression constantExpr = ExpressionFactory.createConstant(true)
        Expression variableExpr = ExpressionFactory.createVariable("testVar")
        Expression andExpr = ExpressionFactory.createAnd(constantExpr, variableExpr)
        Expression orExpr = ExpressionFactory.createOr(constantExpr, variableExpr)
        Expression notExpr = ExpressionFactory.createNot(constantExpr)
        
        then:
        constantExpr instanceof ConstantExpression
        variableExpr instanceof VariableExpression
        andExpr instanceof AndExpression
        orExpr instanceof OrExpression
        notExpr instanceof NotExpression
    }
    
    def "表达式工厂应该能够创建复合表达式"() {
        given:
        Expression left = ExpressionFactory.createVariable("a")
        Expression right = ExpressionFactory.createVariable("b")
        
        when:
        Expression equalExpr = ExpressionFactory.createEqual(left, right)
        RuleExpression ruleExpr = ExpressionFactory.createRule("testRule", left, "testAction")
        
        then:
        equalExpr instanceof EqualExpression
        ruleExpr instanceof RuleExpression
        ruleExpr.getRuleName() == "testRule"
    }
    
    def "表达式工厂应该能够创建数学和SQL表达式"() {
        when:
        MathExpression mathExpr = ExpressionFactory.createMath("10 + 5")
        java.util.List<String> columns = Arrays.asList("name")
        SQLExpression sqlExpr = ExpressionFactory.createSQL("users", columns, null)
        
        then:
        mathExpr instanceof MathExpression
        sqlExpr instanceof SQLExpression
    }
}