package com.example.interpreter

import spock.lang.Specification

/**
 * SQL表达式测试类
 */
class SQLExpressionSpec extends Specification {
    
    def "SQL表达式应该能够解释简单的查询"() {
        given:
        com.example.interpreter.ConstantExpression whereClause = new com.example.interpreter.ConstantExpression(true)
        java.util.List<String> columns = Arrays.asList("name", "age")
        com.example.interpreter.SQLExpression sqlExpr = new com.example.interpreter.SQLExpression("users", columns, whereClause)
        com.example.interpreter.Context context = new com.example.interpreter.Context()
        
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
        com.example.interpreter.ConstantExpression whereClause = new com.example.interpreter.ConstantExpression(true)
        java.util.List<String> columns = Arrays.asList("name", "age")
        com.example.interpreter.SQLExpression sqlExpr = new com.example.interpreter.SQLExpression("users", columns, whereClause)
        
        expect:
        sqlExpr.getTableName() == "users"
        sqlExpr.getColumns() == columns
        sqlExpr.getWhereClause() == whereClause
    }
    
    def "SQL表达式的toString方法应该返回正确的字符串表示"() {
        given:
        com.example.interpreter.ConstantExpression whereClause = new com.example.interpreter.ConstantExpression(true)
        java.util.List<String> columns = Arrays.asList("name", "age")
        com.example.interpreter.SQLExpression sqlExpr = new com.example.interpreter.SQLExpression("users", columns, whereClause)
        
        when:
        String str = sqlExpr.toString()
        
        then:
        str.contains("SELECT")
        str.contains("name, age")
        str.contains("FROM users")
    }
}