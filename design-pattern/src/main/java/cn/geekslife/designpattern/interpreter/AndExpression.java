package cn.geekslife.designpattern.interpreter;

/**
 * 与表达式
 * 非终结符表达式，表示逻辑与操作
 */
public class AndExpression implements Expression {
    private Expression leftExpression;
    private Expression rightExpression;
    
    public AndExpression(Expression leftExpression, Expression rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }
    
    @Override
    public boolean interpret(Context context) {
        return leftExpression.interpret(context) && rightExpression.interpret(context);
    }
    
    @Override
    public String toString() {
        return "(" + leftExpression.toString() + " AND " + rightExpression.toString() + ")";
    }
}