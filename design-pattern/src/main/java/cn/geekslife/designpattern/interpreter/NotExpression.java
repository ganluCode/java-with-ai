package cn.geekslife.designpattern.interpreter;

/**
 * 非表达式
 * 非终结符表达式，表示逻辑非操作
 */
public class NotExpression implements Expression {
    private Expression expression;
    
    public NotExpression(Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public boolean interpret(Context context) {
        return !expression.interpret(context);
    }
    
    @Override
    public String toString() {
        return "(NOT " + expression.toString() + ")";
    }
}