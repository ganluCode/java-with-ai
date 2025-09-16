package cn.geekslife.designpattern.template;

/**
 * XML文件处理器 - 具体模板类
 */
public class XMLFileProcessor extends FileProcessor {
    
    @Override
    protected void openFile(String fileName) {
        System.out.println("打开XML文件: " + fileName);
        System.out.println("使用DOM解析器...");
    }
    
    @Override
    protected void readFile() {
        System.out.println("读取XML文件内容...");
        System.out.println("解析XML节点...");
    }
    
    @Override
    protected void validateData() {
        System.out.println("验证XML数据格式...");
        System.out.println("检查XML Schema...");
    }
    
    @Override
    protected void processData() {
        System.out.println("处理XML数据...");
        System.out.println("提取用户信息...");
    }
    
    @Override
    protected void saveResult() {
        System.out.println("保存处理结果...");
        System.out.println("生成新的XML文件...");
    }
    
    @Override
    protected void closeFile() {
        System.out.println("关闭XML文件...");
    }
    
    // 重写钩子方法
    @Override
    protected boolean needValidateData() {
        return true;
    }
    
    @Override
    protected boolean needSaveResult() {
        return true;
    }
}