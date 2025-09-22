package cn.geekslife.designpattern.interpreter;

/**
 * 规则表达式
 * 用于业务规则引擎
 */
public class RuleExpression implements Expression {
    private String ruleName;
    private Expression condition;
    private String action;
    
    public RuleExpression(String ruleName, Expression condition, String action) {
        this.ruleName = ruleName;
        this.condition = condition;
        this.action = action;
    }
    
    @Override
    public boolean interpret(Context context) {
        System.out.println("执行规则: " + ruleName);
        
        boolean conditionResult = condition.interpret(context);
        System.out.println("条件评估结果: " + conditionResult);
        
        if (conditionResult) {
            System.out.println("执行动作: " + action);
            // 这里可以执行实际的业务逻辑
            context.setOutput("规则 " + ruleName + " 已执行，动作为: " + action);
        } else {
            System.out.println("条件不满足，跳过动作执行");
            context.setOutput("规则 " + ruleName + " 条件不满足");
        }
        
        return conditionResult;
    }
    
    public String getRuleName() {
        return ruleName;
    }
    
    public Expression getCondition() {
        return condition;
    }
    
    public String getAction() {
        return action;
    }
    
    @Override
    public String toString() {
        return "Rule[" + ruleName + ": " + condition.toString() + " -> " + action + "]";
    }
}