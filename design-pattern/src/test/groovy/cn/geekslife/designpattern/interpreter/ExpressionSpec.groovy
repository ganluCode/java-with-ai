package cn.geekslife.designpattern.interpreter

import cn.geekslife.designpattern.interpreter.Context
import cn.geekslife.designpattern.interpreter.Expression
import spock.lang.Specification

/**
 * 表达式接口测试类
 */
class ExpressionSpec extends Specification {
    
    def "表达式接口应该定义interpret方法"() {
        given:
        Expression expression = Mock(Expression)
        Context context = new Context()
        
        when:
        boolean result = expression.interpret(context)
        
        then:
        1 * expression.interpret(context)
    }
}