package cn.geekslife.designpattern.interpreter;

import java.util.Map;
import java.util.HashMap;

/**
 * 数学表达式解释器
 * 用于计算数学表达式
 */
public class MathExpression implements Expression {
    private String expression;
    private Map<String, Double> variables;
    
    public MathExpression(String expression) {
        this.expression = expression;
        this.variables = new HashMap<>();
    }
    
    public MathExpression(String expression, Map<String, Double> variables) {
        this.expression = expression;
        this.variables = variables;
    }
    
    public void setVariable(String name, double value) {
        variables.put(name, value);
    }
    
    @Override
    public boolean interpret(Context context) {
        try {
            double result = evaluate();
            context.setOutput(String.valueOf(result));
            return result != 0;
        } catch (Exception e) {
            context.setOutput("Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 计算数学表达式的值
     * @return 计算结果
     */
    public double evaluate() {
        return evaluateExpression(expression);
    }
    
    /**
     * 计算表达式值的核心方法
     * @param expr 表达式字符串
     * @return 计算结果
     */
    private double evaluateExpression(String expr) {
        expr = expr.replaceAll("\\s+", ""); // 移除空格
        
        // 处理括号
        int lastOpenParen = expr.lastIndexOf('(');
        if (lastOpenParen != -1) {
            int firstCloseParen = expr.indexOf(')', lastOpenParen);
            if (firstCloseParen != -1) {
                String innerExpr = expr.substring(lastOpenParen + 1, firstCloseParen);
                double innerValue = evaluateExpression(innerExpr);
                String newExpr = expr.substring(0, lastOpenParen) + innerValue + expr.substring(firstCloseParen + 1);
                return evaluateExpression(newExpr);
            }
        }
        
        // 处理加减法（从左到右）
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == '+' || c == '-') {
                double left = evaluateExpression(expr.substring(0, i));
                double right = evaluateExpression(expr.substring(i + 1));
                return c == '+' ? left + right : left - right;
            }
        }
        
        // 处理乘除法（从左到右）
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == '*' || c == '/') {
                double left = evaluateExpression(expr.substring(0, i));
                double right = evaluateExpression(expr.substring(i + 1));
                return c == '*' ? left * right : left / right;
            }
        }
        
        // 处理变量
        if (variables.containsKey(expr)) {
            return variables.get(expr);
        }
        
        // 处理数字
        try {
            return Double.parseDouble(expr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("无法解析表达式: " + expr);
        }
    }
    
    @Override
    public String toString() {
        return expression;
    }
}