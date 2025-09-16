package cn.geekslife.designpattern.template;

/**
 * 具体配置驱动模板实现
 */
public class ConcreteConfigurableTemplate extends ConfigurableTemplate {
    
    public ConcreteConfigurableTemplate(boolean needPrepareData, boolean needProcessResult, boolean needValidation) {
        super(needPrepareData, needProcessResult, needValidation);
    }
    
    @Override
    protected void initialize() {
        System.out.println("初始化配置驱动模板...");
    }
    
    @Override
    protected Object prepareData() {
        System.out.println("准备数据...");
        return "准备好的数据";
    }
    
    @Override
    protected void validateData() {
        System.out.println("验证数据...");
    }
    
    @Override
    protected Object executeOperation() {
        System.out.println("执行核心操作...");
        return "操作结果";
    }
    
    @Override
    protected Object processResult(Object result) {
        System.out.println("处理结果...");
        return "处理后的结果";
    }
    
    @Override
    protected void cleanup() {
        System.out.println("清理资源...");
    }
}