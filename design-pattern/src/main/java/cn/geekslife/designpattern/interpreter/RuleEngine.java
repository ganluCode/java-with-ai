package cn.geekslife.designpattern.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则引擎
 * 管理和执行业务规则
 */
public class RuleEngine {
    private List<RuleExpression> rules = new ArrayList<>();
    
    public void addRule(RuleExpression rule) {
        rules.add(rule);
    }
    
    public void addRule(String ruleName, Expression condition, String action) {
        rules.add(new RuleExpression(ruleName, condition, action));
    }
    
    public void executeRules(Context context) {
        System.out.println("=== 开始执行规则引擎 ===");
        for (RuleExpression rule : rules) {
            System.out.println("\n--- 执行规则 ---");
            rule.interpret(context);
        }
        System.out.println("\n=== 规则引擎执行完成 ===");
    }
    
    public void executeRule(String ruleName, Context context) {
        for (RuleExpression rule : rules) {
            if (rule.getRuleName().equals(ruleName)) {
                System.out.println("=== 执行特定规则: " + ruleName + " ===");
                rule.interpret(context);
                return;
            }
        }
        System.out.println("未找到规则: " + ruleName);
    }
    
    public List<RuleExpression> getRules() {
        return rules;
    }
    
    public void clearRules() {
        rules.clear();
    }
}