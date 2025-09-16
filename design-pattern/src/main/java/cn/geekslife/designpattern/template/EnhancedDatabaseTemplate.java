package cn.geekslife.designpattern.template;

/**
 * 增强版数据库操作模板 - 使用回调机制解决子类数量膨胀问题
 */
public abstract class EnhancedDatabaseTemplate {
    
    // 模板方法，使用回调机制
    public final void executeDatabaseOperation(
            ConnectionCallback connectionCallback,
            DataPrepareCallback prepareCallback,
            OperationCallback operationCallback,
            ResultProcessCallback resultCallback,
            CloseCallback closeCallback) {
        
        System.out.println("开始执行增强版数据库操作...");
        
        // 1. 连接数据库
        Object connection = connect();
        if (connectionCallback != null) {
            connectionCallback.onConnected(connection);
        }
        
        // 2. 准备数据
        Object data = prepareData();
        if (prepareCallback != null) {
            prepareCallback.onDataPrepared(data);
        }
        
        // 3. 执行操作
        Object result = executeOperation();
        if (operationCallback != null) {
            operationCallback.onOperationExecuted(result);
        }
        
        // 4. 处理结果
        if (resultCallback != null) {
            Object processedResult = processResult(result);
            resultCallback.onResultProcessed(processedResult);
        }
        
        // 5. 关闭连接
        close();
        if (closeCallback != null) {
            closeCallback.onClosed();
        }
        
        System.out.println("增强版数据库操作执行完成");
    }
    
    // 基本方法 - 连接数据库
    protected abstract Object connect();
    
    // 基本方法 - 准备数据
    protected abstract Object prepareData();
    
    // 基本方法 - 执行操作
    protected abstract Object executeOperation();
    
    // 基本方法 - 处理结果
    protected abstract Object processResult(Object result);
    
    // 基本方法 - 关闭连接
    protected abstract void close();
}