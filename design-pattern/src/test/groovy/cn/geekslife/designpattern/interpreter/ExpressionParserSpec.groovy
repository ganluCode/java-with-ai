package cn.geekslife.designpattern.interpreter

import cn.geekslife.designpattern.interpreter.AndExpression
import cn.geekslife.designpattern.interpreter.ConstantExpression
import cn.geekslife.designpattern.interpreter.Expression
import cn.geekslife.designpattern.interpreter.ExpressionParser
import cn.geekslife.designpattern.interpreter.MathExpression
import cn.geekslife.designpattern.interpreter.NotExpression
import cn.geekslife.designpattern.interpreter.OrExpression
import cn.geekslife.designpattern.interpreter.VariableExpression
import spock.lang.Specification

/**
 * 表达式解析器测试类
 */
class ExpressionParserSpec extends Specification {
    
    def "表达式解析器应该能够解析常量表达式"() {
        when:
        Expression trueExpr = ExpressionParser.parse("TRUE")
        Expression falseExpr = ExpressionParser.parse("FALSE")
        
        then:
        trueExpr instanceof ConstantExpression
        falseExpr instanceof ConstantExpression
    }
    
    def "表达式解析器应该能够解析变量表达式"() {
        when:
        Expression varExpr = ExpressionParser.parse("testVariable")
        
        then:
        varExpr instanceof VariableExpression
        ((VariableExpression) varExpr).getName() == "testVariable"
    }
    
    def "表达式解析器应该能够解析NOT表达式"() {
        when:
        Expression notExpr = ExpressionParser.parse("NOT TRUE")
        
        then:
        notExpr instanceof NotExpression
    }
    
    def "表达式解析器应该能够解析复合表达式"() {
        when:
        Expression andExpr = ExpressionParser.parse("TRUE AND FALSE")
        Expression orExpr = ExpressionParser.parse("TRUE OR FALSE")
        
        then:
        andExpr instanceof AndExpression
        orExpr instanceof OrExpression
    }
    
    def "表达式解析器应该能够解析数学表达式"() {
        when:
        MathExpression mathExpr = ExpressionParser.parseMath("10 + 5")
        
        then:
        mathExpr instanceof MathExpression
    }
}