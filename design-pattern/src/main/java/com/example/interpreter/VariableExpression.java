package com.example.interpreter;

/**
 * 变量表达式
 * 终结符表达式，表示变量
 */
public class VariableExpression implements Expression {
    private String name;
    
    public VariableExpression(String name) {
        this.name = name;
    }
    
    @Override
    public boolean interpret(Context context) {
        Object value = context.getVariable(name);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}