package cn.geekslife.designpattern.interpreter;

/**
 * 常量表达式
 * 终结符表达式，表示布尔常量
 */
public class ConstantExpression implements Expression {
    private boolean value;
    
    public ConstantExpression(boolean value) {
        this.value = value;
    }
    
    @Override
    public boolean interpret(Context context) {
        return value;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}