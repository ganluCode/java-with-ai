package cn.geekslife.designpattern.interpreter

import cn.geekslife.designpattern.interpreter.ConstantExpression
import cn.geekslife.designpattern.interpreter.Context
import cn.geekslife.designpattern.interpreter.SQLExpression
import spock.lang.Specification

/**
 * SQL表达式测试类
 */
class SQLExpressionSpec extends Specification {
    
    def "SQL表达式应该能够解释简单的查询"() {
        given:
        ConstantExpression whereClause = new ConstantExpression(true)
        java.util.List<String> columns = Arrays.asList("name", "age")
        SQLExpression sqlExpr = new SQLExpression("users", columns, whereClause)
        Context context = new Context()
        
        when:
        boolean result = sqlExpr.interpret(context)
        String output = context.getOutput()
        
        then:
        result == true
        output != null
        output.contains("查询执行完成")
    }
    
    def "SQL表达式应该提供正确的访问方法"() {
        given:
        ConstantExpression whereClause = new ConstantExpression(true)
        java.util.List<String> columns = Arrays.asList("name", "age")
        SQLExpression sqlExpr = new SQLExpression("users", columns, whereClause)
        
        expect:
        sqlExpr.getTableName() == "users"
        sqlExpr.getColumns() == columns
        sqlExpr.getWhereClause() == whereClause
    }
    
    def "SQL表达式的toString方法应该返回正确的字符串表示"() {
        given:
        ConstantExpression whereClause = new ConstantExpression(true)
        java.util.List<String> columns = Arrays.asList("name", "age")
        SQLExpression sqlExpr = new SQLExpression("users", columns, whereClause)
        
        when:
        String str = sqlExpr.toString()
        
        then:
        str.contains("SELECT")
        str.contains("name, age")
        str.contains("FROM users")
    }
}