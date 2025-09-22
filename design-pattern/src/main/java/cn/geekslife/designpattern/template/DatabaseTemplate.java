package cn.geekslife.designpattern.template;

/**
 * 数据库操作抽象模板类
 */
public abstract class DatabaseTemplate {
    
    // 模板方法，定义数据库操作流程
    public final void executeDatabaseOperation() {
        // 1. 连接数据库
        connect();
        
        // 2. 调用钩子方法，判断是否需要准备数据
        if (needPrepareData()) {
            prepareData();
        }
        
        // 3. 执行具体数据库操作
        executeOperation();
        
        // 4. 调用钩子方法，判断是否需要处理结果
        if (needProcessResult()) {
            processResult();
        }
        
        // 5. 关闭数据库连接
        close();
    }
    
    // 基本方法 - 连接数据库
    protected abstract void connect();
    
    // 钩子方法 - 是否需要准备数据，默认需要
    protected boolean needPrepareData() {
        return true;
    }
    
    // 基本方法 - 准备数据
    protected abstract void prepareData();
    
    // 基本方法 - 执行具体操作
    protected abstract void executeOperation();
    
    // 钩子方法 - 是否需要处理结果，默认需要
    protected boolean needProcessResult() {
        return true;
    }
    
    // 基本方法 - 处理结果
    protected abstract void processResult();
    
    // 基本方法 - 关闭数据库连接
    protected abstract void close();
}