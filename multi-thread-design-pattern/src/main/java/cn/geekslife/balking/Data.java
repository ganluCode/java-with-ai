package cn.geekslife.balking;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 数据类 - 演示Balking模式
 * 该类表示需要保存到文件的数据，只有在数据被修改后才执行保存操作
 */
public class Data {
    // 文件名
    private final String filename;
    // 文件内容
    private String content;
    // 是否已修改标志
    private boolean changed;
    
    /**
     * 构造函数
     * @param filename 文件名
     * @param content 初始内容
     */
    public Data(String filename, String content) {
        this.filename = filename;
        this.content = content;
        this.changed = true;
    }
    
    /**
     * 修改数据内容
     * @param newContent 新内容
     */
    public synchronized void change(String newContent) {
        this.content = newContent;
        this.changed = true;
        System.out.println(Thread.currentThread().getName() + "：数据已修改 - " + newContent);
    }
    
    /**
     * 保存数据到文件 - Balking模式的核心实现
     * 如果数据未被修改，则放弃保存操作
     */
    public synchronized void save() {
        // Balking模式：如果条件不满足（数据未被修改），则放弃执行
        if (!changed) {
            System.out.println(Thread.currentThread().getName() + "：数据未被修改，放弃保存操作");
            return;
        }
        
        // 执行保存操作
        doSave();
        // 重置修改标志
        changed = false;
        System.out.println(Thread.currentThread().getName() + "：数据已保存到文件 " + filename);
    }
    
    /**
     * 实际的保存操作
     */
    private void doSave() {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
            System.out.println(Thread.currentThread().getName() + "：正在保存数据到 " + filename);
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() + "：保存文件时发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 检查数据是否已修改
     * @return 是否已修改
     */
    public synchronized boolean isChanged() {
        return changed;
    }
    
    /**
     * 获取文件名
     * @return 文件名
     */
    public String getFilename() {
        return filename;
    }
    
    /**
     * 获取内容
     * @return 内容
     */
    public synchronized String getContent() {
        return content;
    }
}