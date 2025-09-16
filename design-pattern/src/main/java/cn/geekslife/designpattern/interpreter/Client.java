package cn.geekslife.designpattern.interpreter;

import java.util.Arrays;
import java.util.List;

/**
 * 客户端类
 * 演示解释器模式的使用
 */
public class Client {
    public static void main(String[] args) {
        System.out.println("=== 解释器模式演示 ===");
        
        // 演示布尔表达式解释
        System.out.println("\n--- 布尔表达式解释 ---");
        demonstrateBooleanExpressions();
        
        // 演示数学表达式计算
        System.out.println("\n--- 数学表达式计算 ---");
        demonstrateMathExpressions();
        
        // 演示规则引擎
        System.out.println("\n--- 规则引擎 ---");
        demonstrateRuleEngine();
        
        // 演示SQL查询解释
        System.out.println("\n--- SQL查询解释 ---");
        demonstrateSQLExpressions();
        
        // 演示表达式解析器
        System.out.println("\n--- 表达式解析器 ---");
        demonstrateExpressionParser();
    }
    
    /**
     * 演示布尔表达式解释
     */
    private static void demonstrateBooleanExpressions() {
        // 创建上下文并设置变量
        Context context = new Context();
        context.setVariable("x", true);
        context.setVariable("y", false);
        context.setVariable("z", true);
        
        // 创建表达式: x AND (y OR z)
        Expression x = new VariableExpression("x");
        Expression y = new VariableExpression("y");
        Expression z = new VariableExpression("z");
        Expression yOrZ = new OrExpression(y, z);
        Expression expression = new AndExpression(x, yOrZ);
        
        // 解释表达式
        boolean result = expression.interpret(context);
        System.out.println("表达式: " + expression.toString());
        System.out.println("解释结果: " + result);
        
        // 创建表达式: NOT x OR y
        Expression notX = new NotExpression(x);
        Expression notXOrY = new OrExpression(notX, y);
        boolean result2 = notXOrY.interpret(context);
        System.out.println("表达式: " + notXOrY.toString());
        System.out.println("解释结果: " + result2);
    }
    
    /**
     * 演示数学表达式计算
     */
    private static void demonstrateMathExpressions() {
        try {
            // 创建数学表达式: 10 + 5 * 2
            MathExpression expr1 = new MathExpression("10 + 5 * 2");
            double result1 = expr1.evaluate();
            System.out.println("表达式: 10 + 5 * 2 = " + result1);
            
            // 创建数学表达式: (20 - 5) / 3
            MathExpression expr2 = new MathExpression("(20 - 5) / 3");
            double result2 = expr2.evaluate();
            System.out.println("表达式: (20 - 5) / 3 = " + result2);
            
            // 创建带变量的数学表达式
            MathExpression expr3 = new MathExpression("a + b * c");
            expr3.setVariable("a", 10);
            expr3.setVariable("b", 5);
            expr3.setVariable("c", 2);
            double result3 = expr3.evaluate();
            System.out.println("表达式: a + b * c (a=10, b=5, c=2) = " + result3);
        } catch (Exception e) {
            System.out.println("数学表达式计算错误: " + e.getMessage());
        }
    }
    
    /**
     * 演示规则引擎
     */
    private static void demonstrateRuleEngine() {
        RuleEngine ruleEngine = new RuleEngine();
        Context context = new Context();
        
        // 设置变量
        context.setVariable("age", 25);
        context.setVariable("income", 50000);
        context.setVariable("creditScore", 700);
        
        // 创建规则1: 年龄大于18岁
        Expression ageCondition = new VariableExpression("age");
        RuleExpression rule1 = ExpressionFactory.createRule(
            "年龄检查", 
            new ConstantExpression(true), // 简化条件
            "允许开户"
        );
        ruleEngine.addRule(rule1);
        
        // 创建规则2: 收入大于30000
        RuleExpression rule2 = ExpressionFactory.createRule(
            "收入检查",
            new ConstantExpression(true), // 简化条件
            "符合贷款条件"
        );
        ruleEngine.addRule(rule2);
        
        // 执行所有规则
        ruleEngine.executeRules(context);
    }
    
    /**
     * 演示SQL查询解释
     */
    private static void demonstrateSQLExpressions() {
        Context context = new Context();
        
        // 创建SELECT查询: SELECT name, age FROM users WHERE age > 18
        List<String> columns = Arrays.asList("name", "age");
        Expression whereClause = new ConstantExpression(true); // 简化WHERE子句
        SQLExpression sqlExpr = ExpressionFactory.createSQL("users", columns, whereClause);
        
        boolean result = sqlExpr.interpret(context);
        System.out.println("SQL表达式: " + sqlExpr.toString());
        System.out.println("执行结果: " + result);
        System.out.println("输出: " + context.getOutput());
    }
    
    /**
     * 演示表达式解析器
     */
    private static void demonstrateExpressionParser() {
        Context context = new Context();
        context.setVariable("a", true);
        context.setVariable("b", false);
        
        // 解析简单表达式
        try {
            Expression expr1 = ExpressionParser.parse("TRUE");
            boolean result1 = expr1.interpret(context);
            System.out.println("解析表达式 'TRUE': " + result1);
            
            Expression expr2 = ExpressionParser.parse("a");
            boolean result2 = expr2.interpret(context);
            System.out.println("解析表达式 'a': " + result2);
            
            Expression expr3 = ExpressionParser.parse("a AND TRUE");
            boolean result3 = expr3.interpret(context);
            System.out.println("解析表达式 'a AND TRUE': " + result3);
        } catch (Exception e) {
            System.out.println("表达式解析错误: " + e.getMessage());
        }
        
        // 解析数学表达式
        try {
            MathExpression mathExpr = ExpressionParser.parseMath("10 + 5 * 2");
            double mathResult = mathExpr.evaluate();
            System.out.println("解析数学表达式 '10 + 5 * 2': " + mathResult);
        } catch (Exception e) {
            System.out.println("数学表达式解析错误: " + e.getMessage());
        }
    }
}