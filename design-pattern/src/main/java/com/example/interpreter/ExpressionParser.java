package com.example.interpreter;

import java.util.Stack;

/**
 * 表达式解析器
 * 将字符串表达式解析为抽象语法树
 */
public class ExpressionParser {
    
    /**
     * 解析布尔表达式
     * 支持的语法: TRUE, FALSE, 变量名, AND, OR, NOT, 括号
     * @param expression 表达式字符串
     * @return 抽象表达式
     */
    public static Expression parse(String expression) {
        // 简化实现，这里只处理一些基本情况
        expression = expression.trim();
        
        // 处理常量
        if ("TRUE".equalsIgnoreCase(expression)) {
            return new ConstantExpression(true);
        }
        if ("FALSE".equalsIgnoreCase(expression)) {
            return new ConstantExpression(false);
        }
        
        // 处理变量
        if (!expression.contains(" ") && !expression.contains("(") && !expression.contains(")")) {
            return new VariableExpression(expression);
        }
        
        // 处理NOT表达式
        if (expression.startsWith("NOT ")) {
            String subExpression = expression.substring(4).trim();
            return new NotExpression(parse(subExpression));
        }
        
        // 处理括号表达式
        if (expression.startsWith("(") && expression.endsWith(")")) {
            String innerExpression = expression.substring(1, expression.length() - 1).trim();
            return parse(innerExpression);
        }
        
        // 处理AND和OR表达式（简单实现）
        int andIndex = expression.indexOf(" AND ");
        if (andIndex > 0) {
            String left = expression.substring(0, andIndex).trim();
            String right = expression.substring(andIndex + 5).trim();
            return new AndExpression(parse(left), parse(right));
        }
        
        int orIndex = expression.indexOf(" OR ");
        if (orIndex > 0) {
            String left = expression.substring(0, orIndex).trim();
            String right = expression.substring(orIndex + 4).trim();
            return new OrExpression(parse(left), parse(right));
        }
        
        // 默认返回变量表达式
        return new VariableExpression(expression);
    }
    
    /**
     * 解析数学表达式
     * @param expression 数学表达式字符串
     * @return 数学表达式解释器
     */
    public static MathExpression parseMath(String expression) {
        return new MathExpression(expression);
    }
}