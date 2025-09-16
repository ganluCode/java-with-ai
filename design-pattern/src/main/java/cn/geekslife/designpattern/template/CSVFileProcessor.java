package cn.geekslife.designpattern.template;

/**
 * CSV文件处理器 - 具体模板类
 */
public class CSVFileProcessor extends FileProcessor {
    
    @Override
    protected void openFile(String fileName) {
        System.out.println("打开CSV文件: " + fileName);
        System.out.println("使用CSV读取器...");
    }
    
    @Override
    protected void readFile() {
        System.out.println("读取CSV文件内容...");
        System.out.println("解析CSV行数据...");
    }
    
    @Override
    protected void validateData() {
        System.out.println("验证CSV数据格式...");
        System.out.println("检查列数和数据类型...");
    }
    
    @Override
    protected void processData() {
        System.out.println("处理CSV数据...");
        System.out.println("转换数据格式...");
    }
    
    // 重写钩子方法，CSV文件不需要保存结果
    @Override
    protected boolean needSaveResult() {
        return false;
    }
    
    @Override
    protected void saveResult() {
        System.out.println("CSV文件直接输出到控制台...");
    }
    
    @Override
    protected void closeFile() {
        System.out.println("关闭CSV文件...");
    }
    
    // 重写钩子方法
    @Override
    protected boolean needValidateData() {
        return true;
    }
}