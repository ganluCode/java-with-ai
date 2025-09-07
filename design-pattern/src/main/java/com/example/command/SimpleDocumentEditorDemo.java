package com.example.command;

/**
 * 简单文档编辑器演示
 * 展示命令模式在文本编辑器中的实际应用
 */
public class SimpleDocumentEditorDemo {
    
    /**
     * 文档类 - 模拟简单的文本编辑器
     */
    static class Document {
        private StringBuilder content = new StringBuilder();
        
        public void insertText(int position, String text) {
            content.insert(position, text);
            System.out.println("在位置 " + position + " 插入文本: \"" + text + "\"");
        }
        
        public void deleteText(int position, int length) {
            String deletedText = content.substring(position, Math.min(position + length, content.length()));
            content.delete(position, position + length);
            System.out.println("从位置 " + position + " 删除文本: \"" + deletedText + "\"");
        }
        
        public void replaceText(int position, int length, String newText) {
            String oldText = content.substring(position, Math.min(position + length, content.length()));
            content.replace(position, position + length, newText);
            System.out.println("替换文本 \"" + oldText + "\" 为 \"" + newText + "\"");
        }
        
        public String getContent() {
            return content.toString();
        }
        
        public int length() {
            return content.length();
        }
    }
    
    /**
     * 插入文本命令
     */
    static class InsertTextCommand implements Command {
        private Document document;
        private int position;
        private String text;
        private boolean executed = false;
        
        public InsertTextCommand(Document document, int position, String text) {
            this.document = document;
            this.position = position;
            this.text = text;
        }
        
        @Override
        public void execute() {
            document.insertText(position, text);
            executed = true;
        }
        
        @Override
        public void undo() {
            if (executed) {
                document.deleteText(position, text.length());
            }
        }
    }
    
    /**
     * 删除文本命令
     */
    static class DeleteTextCommand implements Command {
        private Document document;
        private int position;
        private int length;
        private String deletedText;
        private boolean executed = false;
        
        public DeleteTextCommand(Document document, int position, int length) {
            this.document = document;
            this.position = position;
            this.length = length;
        }
        
        @Override
        public void execute() {
            deletedText = document.getContent().substring(position, Math.min(position + length, document.length()));
            document.deleteText(position, length);
            executed = true;
        }
        
        @Override
        public void undo() {
            if (executed) {
                document.insertText(position, deletedText);
            }
        }
    }
    
    /**
     * 替换文本命令
     */
    static class ReplaceTextCommand implements Command {
        private Document document;
        private int position;
        private int length;
        private String newText;
        private String oldText;
        private boolean executed = false;
        
        public ReplaceTextCommand(Document document, int position, int length, String newText) {
            this.document = document;
            this.position = position;
            this.length = length;
            this.newText = newText;
        }
        
        @Override
        public void execute() {
            oldText = document.getContent().substring(position, Math.min(position + length, document.length()));
            document.replaceText(position, length, newText);
            executed = true;
        }
        
        @Override
        public void undo() {
            if (executed) {
                document.replaceText(position, newText.length(), oldText);
            }
        }
    }
    
    /**
     * 编辑器类 - 模拟文本编辑器的命令管理
     */
    static class TextEditor {
        private Document document = new Document();
        private CommandHistory history = new CommandHistory();
        
        public void insertText(int position, String text) {
            Command command = new InsertTextCommand(document, position, text);
            command.execute();
            history.push(command);
        }
        
        public void deleteText(int position, int length) {
            Command command = new DeleteTextCommand(document, position, length);
            command.execute();
            history.push(command);
        }
        
        public void replaceText(int position, int length, String newText) {
            Command command = new ReplaceTextCommand(document, position, length, newText);
            command.execute();
            history.push(command);
        }
        
        public void undo() {
            if (!history.isEmpty()) {
                System.out.println("撤销操作:");
                Command command = history.undo();
                command.undo();
            } else {
                System.out.println("没有可撤销的操作");
            }
        }
        
        public void redo() {
            System.out.println("重做操作:");
            Command command = history.redo();
            command.execute();
        }
        
        public void printDocument() {
            System.out.println("\n=== 文档内容 ===");
            System.out.println(document.getContent());
            System.out.println("文档长度: " + document.length());
            System.out.println("================\n");
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== 简单文档编辑器演示 ===");
        TextEditor editor = new TextEditor();
        
        // 演示基本编辑操作
        System.out.println("\n--- 基本编辑操作 ---");
        editor.insertText(0, "Hello");
        editor.insertText(5, " World");
        editor.insertText(11, "!");
        editor.printDocument();
        
        // 演示替换操作
        System.out.println("--- 替换操作 ---");
        editor.replaceText(6, 5, "Java");
        editor.printDocument();
        
        // 演示删除操作
        System.out.println("--- 删除操作 ---");
        editor.deleteText(11, 1); // 删除感叹号
        editor.printDocument();
        
        // 演示撤销操作
        System.out.println("--- 撤销操作 ---");
        editor.undo(); // 撤销删除
        editor.printDocument();
        
        editor.undo(); // 撤销替换
        editor.printDocument();
        
        editor.undo(); // 撤销插入感叹号
        editor.printDocument();
        
        // 演示重做操作
        System.out.println("--- 重做操作 ---");
        editor.redo(); // 重做插入感叹号
        editor.printDocument();
        
        editor.redo(); // 重做替换
        editor.printDocument();
        
        editor.redo(); // 重做删除
        editor.printDocument();
        
        System.out.println("=== 演示完成 ===");
    }
}