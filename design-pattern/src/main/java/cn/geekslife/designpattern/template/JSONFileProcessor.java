package cn.geekslife.designpattern.template;

/**
 * JSON文件处理器 - 具体模板类
 */
public class JSONFileProcessor extends FileProcessor {
    
    @Override
    protected void openFile(String fileName) {
        System.out.println("打开JSON文件: " + fileName);
        System.out.println("使用JSON解析器...");
    }
    
    @Override
    protected void readFile() {
        System.out.println("读取JSON文件内容...");
        System.out.println("解析JSON对象...");
    }
    
    // 重写钩子方法，JSON文件不需要验证数据
    @Override
    protected boolean needValidateData() {
        return false;
    }
    
    @Override
    protected void validateData() {
        System.out.println("JSON数据已通过解析器自动验证...");
    }
    
    @Override
    protected void processData() {
        System.out.println("处理JSON数据...");
        System.out.println("提取嵌套对象信息...");
    }
    
    @Override
    protected void saveResult() {
        System.out.println("保存处理结果...");
        System.out.println("生成新的JSON文件...");
    }
    
    @Override
    protected void closeFile() {
        System.out.println("关闭JSON文件...");
    }
    
    // 重写钩子方法
    @Override
    protected boolean needSaveResult() {
        return true;
    }
}