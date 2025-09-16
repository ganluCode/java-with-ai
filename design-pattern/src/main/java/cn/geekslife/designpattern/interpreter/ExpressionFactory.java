package cn.geekslife.designpattern.interpreter;

import java.util.List;

/**
 * 表达式工厂
 * 创建各种类型的表达式
 */
public class ExpressionFactory {
    
    /**
     * 创建常量表达式
     * @param value 常量值
     * @return 常量表达式
     */
    public static Expression createConstant(boolean value) {
        return new ConstantExpression(value);
    }
    
    /**
     * 创建变量表达式
     * @param name 变量名
     * @return 变量表达式
     */
    public static Expression createVariable(String name) {
        return new VariableExpression(name);
    }
    
    /**
     * 创建与表达式
     * @param left 左表达式
     * @param right 右表达式
     * @return 与表达式
     */
    public static Expression createAnd(Expression left, Expression right) {
        return new AndExpression(left, right);
    }
    
    /**
     * 创建或表达式
     * @param left 左表达式
     * @param right 右表达式
     * @return 或表达式
     */
    public static Expression createOr(Expression left, Expression right) {
        return new OrExpression(left, right);
    }
    
    /**
     * 创建非表达式
     * @param expression 表达式
     * @return 非表达式
     */
    public static Expression createNot(Expression expression) {
        return new NotExpression(expression);
    }
    
    /**
     * 创建等于表达式
     * @param left 左表达式
     * @param right 右表达式
     * @return 等于表达式
     */
    public static Expression createEqual(Expression left, Expression right) {
        return new EqualExpression(left, right);
    }
    
    /**
     * 创建规则表达式
     * @param ruleName 规则名
     * @param condition 条件表达式
     * @param action 动作
     * @return 规则表达式
     */
    public static RuleExpression createRule(String ruleName, Expression condition, String action) {
        return new RuleExpression(ruleName, condition, action);
    }
    
    /**
     * 创建SQL表达式
     * @param tableName 表名
     * @param columns 列名列表
     * @param whereClause WHERE子句
     * @return SQL表达式
     */
    public static SQLExpression createSQL(String tableName, List<String> columns, Expression whereClause) {
        return new SQLExpression(tableName, columns, whereClause);
    }
    
    /**
     * 创建数学表达式
     * @param expression 数学表达式字符串
     * @return 数学表达式
     */
    public static MathExpression createMath(String expression) {
        return new MathExpression(expression);
    }
}