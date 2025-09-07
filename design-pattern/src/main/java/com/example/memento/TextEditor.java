package com.example.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本编辑器 - 原发器的具体实现
 * 演示备忘录模式在文本编辑器中的应用
 */
public class TextEditor implements Originator {
    private StringBuilder content;
    private List<String> history;
    private int cursorPosition;
    
    /**
     * 构造函数
     */
    public TextEditor() {
        this.content = new StringBuilder();
        this.history = new ArrayList<>();
        this.cursorPosition = 0;
    }
    
    /**
     * 构造函数
     * @param initialContent 初始内容
     */
    public TextEditor(String initialContent) {
        this.content = new StringBuilder(initialContent);
        this.history = new ArrayList<>();
        this.cursorPosition = initialContent.length();
        this.history.add(initialContent);
    }
    
    /**
     * 输入文本
     * @param text 要输入的文本
     */
    public void type(String text) {
        content.insert(cursorPosition, text);
        cursorPosition += text.length();
        history.add("输入: " + text);
    }
    
    /**
     * 删除文本
     * @param count 删除字符数
     */
    public void delete(int count) {
        if (count > 0 && cursorPosition >= count) {
            String deletedText = content.substring(cursorPosition - count, cursorPosition);
            content.delete(cursorPosition - count, cursorPosition);
            cursorPosition -= count;
            history.add("删除: " + deletedText);
        }
    }
    
    /**
     * 移动光标
     * @param position 新的光标位置
     */
    public void moveCursor(int position) {
        if (position >= 0 && position <= content.length()) {
            cursorPosition = position;
            history.add("光标移动到位置: " + position);
        }
    }
    
    /**
     * 获取当前内容
     * @return 当前内容
     */
    public String getContent() {
        return content.toString();
    }
    
    /**
     * 获取光标位置
     * @return 光标位置
     */
    public int getCursorPosition() {
        return cursorPosition;
    }
    
    /**
     * 获取历史记录
     * @return 历史记录
     */
    public List<String> getHistory() {
        return new ArrayList<>(history);
    }
    
    /**
     * 清空内容
     */
    public void clear() {
        content.setLength(0);
        cursorPosition = 0;
        history.add("清空内容");
    }
    
    /**
     * 替换文本
     * @param oldText 要替换的文本
     * @param newText 新文本
     */
    public void replace(String oldText, String newText) {
        int index = content.indexOf(oldText);
        if (index >= 0) {
            content.replace(index, index + oldText.length(), newText);
            cursorPosition = index + newText.length();
            history.add("替换: '" + oldText + "' -> '" + newText + "'");
        }
    }
    
    @Override
    public Memento createMemento(String description) {
        // 保存当前状态：内容、光标位置
        String state = content.toString() + "|" + cursorPosition;
        return new Memento(state, description);
    }
    
    @Override
    public void restoreMemento(Memento memento) {
        if (memento != null) {
            String state = memento.getState();
            int separatorIndex = state.lastIndexOf('|');
            if (separatorIndex > 0) {
                String contentStr = state.substring(0, separatorIndex);
                String cursorPosStr = state.substring(separatorIndex + 1);
                
                try {
                    content = new StringBuilder(contentStr);
                    cursorPosition = Integer.parseInt(cursorPosStr);
                    history.add("恢复状态: " + memento.getDescription());
                } catch (NumberFormatException e) {
                    System.err.println("恢复状态失败: " + e.getMessage());
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return "TextEditor{" +
                "content='" + content + '\'' +
                ", cursorPosition=" + cursorPosition +
                '}';
    }
}