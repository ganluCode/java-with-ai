package cn.geekslife.designpattern.interpreter;

/**
 * 或表达式
 * 非终结符表达式，表示逻辑或操作
 */
public class OrExpression implements Expression {
    private Expression leftExpression;
    private Expression rightExpression;
    
    public OrExpression(Expression leftExpression, Expression rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }
    
    @Override
    public boolean interpret(Context context) {
        return leftExpression.interpret(context) || rightExpression.interpret(context);
    }
    
    @Override
    public String toString() {
        return "(" + leftExpression.toString() + " OR " + rightExpression.toString() + ")";
    }
}