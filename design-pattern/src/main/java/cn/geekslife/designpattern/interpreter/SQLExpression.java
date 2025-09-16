package cn.geekslife.designpattern.interpreter;

import java.util.List;

/**
 * SQL表达式
 * 用于解析和执行简单的SQL查询
 */
public class SQLExpression implements Expression {
    private String tableName;
    private List<String> columns;
    private Expression whereClause;
    
    public SQLExpression(String tableName, List<String> columns, Expression whereClause) {
        this.tableName = tableName;
        this.columns = columns;
        this.whereClause = whereClause;
    }
    
    @Override
    public boolean interpret(Context context) {
        System.out.println("执行SQL查询:");
        System.out.println("SELECT " + String.join(", ", columns) + " FROM " + tableName);
        
        if (whereClause != null) {
            System.out.println("WHERE " + whereClause.toString());
            boolean whereResult = whereClause.interpret(context);
            System.out.println("WHERE子句结果: " + whereResult);
        }
        
        // 模拟查询结果
        context.setOutput("查询执行完成，返回结果集");
        return true;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public List<String> getColumns() {
        return columns;
    }
    
    public Expression getWhereClause() {
        return whereClause;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(String.join(", ", columns))
          .append(" FROM ").append(tableName);
        if (whereClause != null) {
            sb.append(" WHERE ").append(whereClause.toString());
        }
        return sb.toString();
    }
}