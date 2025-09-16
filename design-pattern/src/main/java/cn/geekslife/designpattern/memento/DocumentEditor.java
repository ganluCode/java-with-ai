package cn.geekslife.designpattern.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档编辑器 - 更复杂的原发器实现
 * 演示备忘录模式在复杂文档编辑中的应用
 */
public class DocumentEditor implements Originator {
    private String title;
    private String content;
    private List<String> chapters;
    private String author;
    private String lastModified;
    private int fontSize;
    private String fontFamily;
    private boolean isBold;
    private boolean isItalic;
    private List<DocumentVersion> versionHistory;
    
    /**
     * 文档版本类
     */
    public static class DocumentVersion {
        private String title;
        private String author;
        private String timestamp;
        
        public DocumentVersion(String title, String author, String timestamp) {
            this.title = title;
            this.author = author;
            this.timestamp = timestamp;
        }
        
        // Getters
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getTimestamp() { return timestamp; }
    }
    
    /**
     * 构造函数
     */
    public DocumentEditor() {
        this.title = "未命名文档";
        this.content = "";
        this.chapters = new ArrayList<>();
        this.author = System.getProperty("user.name");
        this.lastModified = java.time.LocalDateTime.now().toString();
        this.fontSize = 12;
        this.fontFamily = "Arial";
        this.isBold = false;
        this.isItalic = false;
        this.versionHistory = new ArrayList<>();
    }
    
    /**
     * 构造函数
     * @param title 文档标题
     * @param author 作者
     */
    public DocumentEditor(String title, String author) {
        this.title = title;
        this.content = "";
        this.chapters = new ArrayList<>();
        this.author = author;
        this.lastModified = java.time.LocalDateTime.now().toString();
        this.fontSize = 12;
        this.fontFamily = "Arial";
        this.isBold = false;
        this.isItalic = false;
        this.versionHistory = new ArrayList<>();
    }
    
    /**
     * 设置标题
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
        updateLastModified();
    }
    
    /**
     * 添加章节
     * @param chapter 章节标题
     */
    public void addChapter(String chapter) {
        chapters.add(chapter);
        updateLastModified();
    }
    
    /**
     * 编辑内容
     * @param content 内容
     */
    public void editContent(String content) {
        this.content = content;
        updateLastModified();
    }
    
    /**
     * 设置字体大小
     * @param fontSize 字体大小
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        updateLastModified();
    }
    
    /**
     * 设置字体
     * @param fontFamily 字体
     */
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
        updateLastModified();
    }
    
    /**
     * 设置粗体
     * @param bold 是否粗体
     */
    public void setBold(boolean bold) {
        isBold = bold;
        updateLastModified();
    }
    
    /**
     * 设置斜体
     * @param italic 是否斜体
     */
    public void setItalic(boolean italic) {
        isItalic = italic;
        updateLastModified();
    }
    
    /**
     * 更新最后修改时间
     */
    private void updateLastModified() {
        lastModified = java.time.LocalDateTime.now().toString();
    }
    
    /**
     * 获取标题
     * @return 标题
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * 获取内容
     * @return 内容
     */
    public String getContent() {
        return content;
    }
    
    /**
     * 获取章节列表
     * @return 章节列表
     */
    public List<String> getChapters() {
        return new ArrayList<>(chapters);
    }
    
    /**
     * 获取作者
     * @return 作者
     */
    public String getAuthor() {
        return author;
    }
    
    /**
     * 获取最后修改时间
     * @return 最后修改时间
     */
    public String getLastModified() {
        return lastModified;
    }
    
    /**
     * 获取字体大小
     * @return 字体大小
     */
    public int getFontSize() {
        return fontSize;
    }
    
    /**
     * 获取字体
     * @return 字体
     */
    public String getFontFamily() {
        return fontFamily;
    }
    
    /**
     * 是否粗体
     * @return 是否粗体
     */
    public boolean isBold() {
        return isBold;
    }
    
    /**
     * 是否斜体
     * @return 是否斜体
     */
    public boolean isItalic() {
        return isItalic;
    }
    
    /**
     * 获取版本历史
     * @return 版本历史
     */
    public List<DocumentVersion> getVersionHistory() {
        return new ArrayList<>(versionHistory);
    }
    
    /**
     * 保存版本
     * @param versionName 版本名称
     */
    public void saveVersion(String versionName) {
        versionHistory.add(new DocumentVersion(title, author, lastModified));
        System.out.println("文档版本已保存: " + versionName);
    }
    
    /**
     * 应用格式
     * @param format 格式类型
     */
    public void applyFormat(String format) {
        switch (format.toLowerCase()) {
            case "bold":
                isBold = true;
                break;
            case "italic":
                isItalic = true;
                break;
            case "underline":
                // 简化处理
                break;
            default:
                System.out.println("未知格式: " + format);
        }
        updateLastModified();
    }
    
    @Override
    public Memento createMemento(String description) {
        // 序列化文档状态
        StringBuilder stateBuilder = new StringBuilder();
        stateBuilder.append(title).append("|")
                   .append(content).append("|")
                   .append(author).append("|")
                   .append(lastModified).append("|")
                   .append(fontSize).append("|")
                   .append(fontFamily).append("|")
                   .append(isBold).append("|")
                   .append(isItalic).append("|");
        
        // 序列化章节
        for (int i = 0; i < chapters.size(); i++) {
            stateBuilder.append(chapters.get(i));
            if (i < chapters.size() - 1) {
                stateBuilder.append(";");
            }
        }
        stateBuilder.append("|");
        
        // 序列化版本历史大小
        stateBuilder.append(versionHistory.size());
        
        return new Memento(stateBuilder.toString(), description);
    }
    
    @Override
    public void restoreMemento(Memento memento) {
        if (memento != null) {
            String state = memento.getState();
            String[] parts = state.split("\\|");
            
            if (parts.length >= 9) {
                title = parts[0];
                content = parts[1];
                author = parts[2];
                lastModified = parts[3];
                fontSize = Integer.parseInt(parts[4]);
                fontFamily = parts[5];
                isBold = Boolean.parseBoolean(parts[6]);
                isItalic = Boolean.parseBoolean(parts[7]);
                
                // 恢复章节
                chapters.clear();
                if (!parts[8].isEmpty()) {
                    String[] chapterArray = parts[8].split(";");
                    for (String chapter : chapterArray) {
                        chapters.add(chapter);
                    }
                }
                
                System.out.println("文档状态已恢复: " + memento.getDescription());
            }
        }
    }
    
    @Override
    public String toString() {
        return "DocumentEditor{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", fontSize=" + fontSize +
                ", fontFamily='" + fontFamily + '\'' +
                ", isBold=" + isBold +
                ", isItalic=" + isItalic +
                ", chapterCount=" + chapters.size() +
                '}';
    }
}