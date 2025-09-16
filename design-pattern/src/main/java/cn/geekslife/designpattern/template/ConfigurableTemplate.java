package cn.geekslife.designpattern.template;

/**
 * 配置驱动的模板方法 - 使用配置解决算法步骤僵化问题
 */
public abstract class ConfigurableTemplate {
    private boolean needPrepareData = true;
    private boolean needProcessResult = true;
    private boolean needValidation = true;
    
    // 通过构造函数或setter方法配置流程
    public ConfigurableTemplate(boolean needPrepareData, boolean needProcessResult, boolean needValidation) {
        this.needPrepareData = needPrepareData;
        this.needProcessResult = needProcessResult;
        this.needValidation = needValidation;
    }
    
    // 模板方法，根据配置执行不同步骤
    public final void execute() {
        System.out.println("开始执行配置驱动的模板方法...");
        
        // 1. 初始化
        initialize();
        
        // 2. 根据配置决定是否准备数据
        if (needPrepareData) {
            Object data = prepareData();
            onDataPrepared(data);
        }
        
        // 3. 根据配置决定是否验证数据
        if (needValidation) {
            validateData();
        }
        
        // 4. 执行核心操作
        Object result = executeOperation();
        onOperationExecuted(result);
        
        // 5. 根据配置决定是否处理结果
        if (needProcessResult) {
            Object processedResult = processResult(result);
            onResultProcessed(processedResult);
        }
        
        // 6. 清理资源
        cleanup();
        
        System.out.println("配置驱动的模板方法执行完成");
    }
    
    // 基本方法 - 初始化
    protected abstract void initialize();
    
    // 基本方法 - 准备数据
    protected abstract Object prepareData();
    
    // 钩子方法 - 数据准备完成
    protected void onDataPrepared(Object data) {
        System.out.println("数据准备完成");
    }
    
    // 基本方法 - 验证数据
    protected abstract void validateData();
    
    // 基本方法 - 执行操作
    protected abstract Object executeOperation();
    
    // 钩子方法 - 操作执行完成
    protected void onOperationExecuted(Object result) {
        System.out.println("操作执行完成");
    }
    
    // 基本方法 - 处理结果
    protected abstract Object processResult(Object result);
    
    // 钩子方法 - 结果处理完成
    protected void onResultProcessed(Object result) {
        System.out.println("结果处理完成");
    }
    
    // 基本方法 - 清理资源
    protected abstract void cleanup();
    
    // Getter方法
    public boolean isNeedPrepareData() {
        return needPrepareData;
    }
    
    public boolean isNeedProcessResult() {
        return needProcessResult;
    }
    
    public boolean isNeedValidation() {
        return needValidation;
    }
}