package cn.geekslife.designpattern.interpreter;

/**
 * 等于表达式
 * 非终结符表达式，表示相等比较操作
 */
public class EqualExpression implements Expression {
    private Expression leftExpression;
    private Expression rightExpression;
    
    public EqualExpression(Expression leftExpression, Expression rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }
    
    @Override
    public boolean interpret(Context context) {
        // 简化实现，假设表达式返回字符串进行比较
        return leftExpression.toString().equals(rightExpression.toString());
    }
    
    @Override
    public String toString() {
        return "(" + leftExpression.toString() + " = " + rightExpression.toString() + ")";
    }
}