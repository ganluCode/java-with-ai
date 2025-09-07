package template;

/**
 * 文件处理抽象模板类
 */
public abstract class FileProcessor {
    
    // 模板方法，定义文件处理流程
    public final void processFile(String fileName) {
        System.out.println("开始处理文件: " + fileName);
        
        // 1. 打开文件
        openFile(fileName);
        
        // 2. 读取文件
        readFile();
        
        // 3. 调用钩子方法，判断是否需要验证数据
        if (needValidateData()) {
            validateData();
        }
        
        // 4. 处理文件数据
        processData();
        
        // 5. 调用钩子方法，判断是否需要保存结果
        if (needSaveResult()) {
            saveResult();
        }
        
        // 6. 关闭文件
        closeFile();
        
        System.out.println("文件处理完成: " + fileName);
    }
    
    // 基本方法 - 打开文件
    protected abstract void openFile(String fileName);
    
    // 基本方法 - 读取文件
    protected abstract void readFile();
    
    // 钩子方法 - 是否需要验证数据，默认需要
    protected boolean needValidateData() {
        return true;
    }
    
    // 基本方法 - 验证数据
    protected abstract void validateData();
    
    // 基本方法 - 处理数据
    protected abstract void processData();
    
    // 钩子方法 - 是否需要保存结果，默认需要
    protected boolean needSaveResult() {
        return true;
    }
    
    // 基本方法 - 保存结果
    protected abstract void saveResult();
    
    // 基本方法 - 关闭文件
    protected abstract void closeFile();
}