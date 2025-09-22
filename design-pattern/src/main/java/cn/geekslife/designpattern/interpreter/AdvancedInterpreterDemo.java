package cn.geekslife.designpattern.interpreter;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 高级解释器演示
 * 展示解释器模式在实际应用中的使用
 */
public class AdvancedInterpreterDemo {
    
    /**
     * 正则表达式表达式
     */
    static class RegexExpression implements Expression {
        private Pattern pattern;
        private String regex;
        
        public RegexExpression(String regex) {
            this.regex = regex;
            this.pattern = Pattern.compile(regex);
        }
        
        @Override
        public boolean interpret(Context context) {
            String input = context.getInput();
            if (input == null) return false;
            return pattern.matcher(input).matches();
        }
        
        @Override
        public String toString() {
            return "Regex[" + regex + "]";
        }
    }
    
    /**
     * 数值比较表达式
     */
    static class ComparisonExpression implements Expression {
        private String variable;
        private String operator;
        private double value;
        
        public ComparisonExpression(String variable, String operator, double value) {
            this.variable = variable;
            this.operator = operator;
            this.value = value;
        }
        
        @Override
        public boolean interpret(Context context) {
            Object varValue = context.getVariable(variable);
            if (varValue == null) return false;
            
            double varDouble;
            if (varValue instanceof Number) {
                varDouble = ((Number) varValue).doubleValue();
            } else {
                try {
                    varDouble = Double.parseDouble(varValue.toString());
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            
            switch (operator) {
                case ">": return varDouble > value;
                case "<": return varDouble < value;
                case ">=": return varDouble >= value;
                case "<=": return varDouble <= value;
                case "==": return varDouble == value;
                case "!=": return varDouble != value;
                default: return false;
            }
        }
        
        @Override
        public String toString() {
            return variable + " " + operator + " " + value;
        }
    }
    
    /**
     * 字符串包含表达式
     */
    static class ContainsExpression implements Expression {
        private String variable;
        private String substring;
        
        public ContainsExpression(String variable, String substring) {
            this.variable = variable;
            this.substring = substring;
        }
        
        @Override
        public boolean interpret(Context context) {
            Object varValue = context.getVariable(variable);
            if (varValue == null) return false;
            return varValue.toString().contains(substring);
        }
        
        @Override
        public String toString() {
            return variable + " contains '" + substring + "'";
        }
    }
    
    /**
     * 权限检查表达式
     */
    static class PermissionExpression implements Expression {
        private String permission;
        
        public PermissionExpression(String permission) {
            this.permission = permission;
        }
        
        @Override
        public boolean interpret(Context context) {
            @SuppressWarnings("unchecked")
            Set<String> permissions = (Set<String>) context.getVariable("permissions");
            if (permissions == null) return false;
            return permissions.contains(permission);
        }
        
        @Override
        public String toString() {
            return "hasPermission('" + permission + "')";
        }
    }
    
    /**
     * 配置检查表达式
     */
    static class ConfigExpression implements Expression {
        private String key;
        private String expectedValue;
        
        public ConfigExpression(String key, String expectedValue) {
            this.key = key;
            this.expectedValue = expectedValue;
        }
        
        @Override
        public boolean interpret(Context context) {
            Object actualValue = context.getVariable(key);
            if (actualValue == null) return false;
            return expectedValue.equals(actualValue.toString());
        }
        
        @Override
        public String toString() {
            return "config['" + key + "'] == '" + expectedValue + "'";
        }
    }
    
    /**
     * 复杂业务规则引擎
     */
    static class BusinessRuleEngine {
        private List<RuleExpression> rules = new ArrayList<>();
        
        public void addRule(String name, Expression condition, String action) {
            rules.add(new RuleExpression(name, condition, action));
        }
        
        public void executeRules(Context context) {
            System.out.println("=== 业务规则引擎执行 ===");
            int executedCount = 0;
            
            for (RuleExpression rule : rules) {
                if (rule.interpret(context)) {
                    executedCount++;
                }
            }
            
            System.out.println("总共执行了 " + executedCount + " 条规则");
            System.out.println("====================\n");
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== 高级解释器模式演示 ===");
        
        // 演示正则表达式解释器
        System.out.println("\n--- 正则表达式解释器 ---");
        demonstrateRegexInterpreter();
        
        // 演示数值比较解释器
        System.out.println("\n--- 数值比较解释器 ---");
        demonstrateComparisonInterpreter();
        
        // 演示字符串包含解释器
        System.out.println("\n--- 字符串包含解释器 ---");
        demonstrateContainsInterpreter();
        
        // 演示权限检查解释器
        System.out.println("\n--- 权限检查解释器 ---");
        demonstratePermissionInterpreter();
        
        // 演示配置检查解释器
        System.out.println("\n--- 配置检查解释器 ---");
        demonstrateConfigInterpreter();
        
        // 演示复杂业务规则引擎
        System.out.println("\n--- 复杂业务规则引擎 ---");
        demonstrateBusinessRuleEngine();
    }
    
    /**
     * 演示正则表达式解释器
     */
    private static void demonstrateRegexInterpreter() {
        // 创建正则表达式表达式
        Expression emailRegex = new RegexExpression("^[\\w.-]+@[\\w.-]+\\.[\\w]+$");
        Expression phoneRegex = new RegexExpression("^1[3-9]\\d{9}$");
        
        // 测试邮箱正则
        Context emailContext = new Context("user@example.com");
        boolean isEmailValid = emailRegex.interpret(emailContext);
        System.out.println("邮箱验证 '" + emailContext.getInput() + "': " + isEmailValid);
        
        // 测试手机号正则
        Context phoneContext = new Context("13812345678");
        boolean isPhoneValid = phoneRegex.interpret(phoneContext);
        System.out.println("手机号验证 '" + phoneContext.getInput() + "': " + isPhoneValid);
        
        // 测试无效格式
        Context invalidContext = new Context("invalid-format");
        boolean isInvalid = emailRegex.interpret(invalidContext);
        System.out.println("无效邮箱验证 '" + invalidContext.getInput() + "': " + isInvalid);
    }
    
    /**
     * 演示数值比较解释器
     */
    private static void demonstrateComparisonInterpreter() {
        Context context = new Context();
        context.setVariable("age", 25);
        context.setVariable("income", 50000);
        context.setVariable("score", 85.5);
        
        // 创建比较表达式
        Expression ageCheck = new ComparisonExpression("age", ">=", 18);
        Expression incomeCheck = new ComparisonExpression("income", ">", 30000);
        Expression scoreCheck = new ComparisonExpression("score", "==", 85.5);
        
        // 执行比较
        System.out.println("年龄 >= 18: " + ageCheck.interpret(context));
        System.out.println("收入 > 30000: " + incomeCheck.interpret(context));
        System.out.println("分数 == 85.5: " + scoreCheck.interpret(context));
        
        // 复合表达式
        Expression compoundExpr = new AndExpression(ageCheck, incomeCheck);
        System.out.println("年龄 >= 18 且 收入 > 30000: " + compoundExpr.interpret(context));
    }
    
    /**
     * 演示字符串包含解释器
     */
    private static void demonstrateContainsInterpreter() {
        Context context = new Context();
        context.setVariable("username", "admin_user");
        context.setVariable("email", "user@company.com");
        context.setVariable("description", "This is a test user account for demonstration purposes");
        
        // 创建包含表达式
        Expression adminCheck = new ContainsExpression("username", "admin");
        Expression companyCheck = new ContainsExpression("email", "company.com");
        Expression testCheck = new ContainsExpression("description", "test");
        
        // 执行检查
        System.out.println("用户名包含 'admin': " + adminCheck.interpret(context));
        System.out.println("邮箱包含 'company.com': " + companyCheck.interpret(context));
        System.out.println("描述包含 'test': " + testCheck.interpret(context));
        
        // 复合表达式
        Expression compoundExpr = new OrExpression(adminCheck, testCheck);
        System.out.println("用户名包含 'admin' 或 描述包含 'test': " + compoundExpr.interpret(context));
    }
    
    /**
     * 演示权限检查解释器
     */
    private static void demonstratePermissionInterpreter() {
        Context context = new Context();
        
        // 设置用户权限
        Set<String> permissions = new HashSet<>();
        permissions.add("read");
        permissions.add("write");
        permissions.add("delete");
        context.setVariable("permissions", permissions);
        
        // 创建权限表达式
        Expression readPermission = new PermissionExpression("read");
        Expression writePermission = new PermissionExpression("write");
        Expression adminPermission = new PermissionExpression("admin");
        
        // 执行权限检查
        System.out.println("具有读权限: " + readPermission.interpret(context));
        System.out.println("具有写权限: " + writePermission.interpret(context));
        System.out.println("具有管理员权限: " + adminPermission.interpret(context));
        
        // 复合权限检查
        Expression requiredPermissions = new AndExpression(readPermission, writePermission);
        System.out.println("具有读写权限: " + requiredPermissions.interpret(context));
    }
    
    /**
     * 演示配置检查解释器
     */
    private static void demonstrateConfigInterpreter() {
        Context context = new Context();
        
        // 设置配置项
        context.setVariable("environment", "production");
        context.setVariable("debug", "false");
        context.setVariable("version", "2.1.0");
        context.setVariable("feature_flag", "enabled");
        
        // 创建配置表达式
        Expression prodEnv = new ConfigExpression("environment", "production");
        Expression debugDisabled = new ConfigExpression("debug", "false");
        Expression versionCheck = new ConfigExpression("version", "2.1.0");
        
        // 执行配置检查
        System.out.println("生产环境: " + prodEnv.interpret(context));
        System.out.println("调试已禁用: " + debugDisabled.interpret(context));
        System.out.println("版本正确: " + versionCheck.interpret(context));
        
        // 复合配置检查
        Expression validConfig = new AndExpression(
            new AndExpression(prodEnv, debugDisabled),
            versionCheck
        );
        System.out.println("有效配置: " + validConfig.interpret(context));
    }
    
    /**
     * 演示复杂业务规则引擎
     */
    private static void demonstrateBusinessRuleEngine() {
        BusinessRuleEngine ruleEngine = new BusinessRuleEngine();
        Context context = new Context();
        
        // 设置用户信息
        context.setVariable("age", 25);
        context.setVariable("income", 50000);
        context.setVariable("creditScore", 750);
        context.setVariable("employmentYears", 3);
        
        // 设置权限
        Set<String> permissions = new HashSet<>();
        permissions.add("user");
        permissions.add("verified");
        context.setVariable("permissions", permissions);
        
        // 设置配置
        context.setVariable("minAge", 18);
        context.setVariable("minIncome", 30000);
        context.setVariable("minCreditScore", 600);
        
        // 添加业务规则
        ruleEngine.addRule(
            "年龄检查",
            new ComparisonExpression("age", ">=", 18),
            "允许注册账户"
        );
        
        ruleEngine.addRule(
            "收入检查",
            new ComparisonExpression("income", ">=", 30000),
            "符合贷款条件"
        );
        
        ruleEngine.addRule(
            "信用评分检查",
            new ComparisonExpression("creditScore", ">=", 600),
            "信用评级良好"
        );
        
        ruleEngine.addRule(
            "就业年限检查",
            new ComparisonExpression("employmentYears", ">=", 1),
            "稳定就业"
        );
        
        ruleEngine.addRule(
            "权限检查",
            new PermissionExpression("verified"),
            "已验证用户"
        );
        
        // 执行所有规则
        ruleEngine.executeRules(context);
        
        // 演示复合条件规则
        System.out.println("--- 复合条件规则演示 ---");
        Context premiumContext = new Context();
        premiumContext.setVariable("age", 30);
        premiumContext.setVariable("income", 80000);
        premiumContext.setVariable("creditScore", 800);
        
        Set<String> premiumPermissions = new HashSet<>();
        premiumPermissions.add("user");
        premiumPermissions.add("verified");
        premiumPermissions.add("premium");
        premiumContext.setVariable("permissions", premiumPermissions);
        
        Expression premiumCondition = new AndExpression(
            new AndExpression(
                new ComparisonExpression("age", ">=", 25),
                new ComparisonExpression("income", ">=", 50000)
            ),
            new AndExpression(
                new ComparisonExpression("creditScore", ">=", 700),
                new PermissionExpression("premium")
            )
        );
        
        RuleExpression premiumRule = new RuleExpression(
            "高级用户资格检查",
            premiumCondition,
            "授予高级用户权限"
        );
        
        System.out.println("高级用户条件: " + premiumCondition.toString());
        premiumRule.interpret(premiumContext);
    }
}