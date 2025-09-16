package cn.geekslife.designpattern.command;

import java.util.Scanner;

/**
 * 文档编辑器演示
 * 展示命令模式在文本编辑器中的实际应用
 */
public class DocumentEditorDemo {
    
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
        
        public int getDocumentLength() {
            return document.length();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== 文档编辑器演示 ===");
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
        
        // 演示交互式编辑
        System.out.println("\n--- 交互式编辑演示 ---");
        interactiveDemo(editor);
    }
    
    /**
     * 交互式演示方法
     */
    private static void interactiveDemo(TextEditor editor) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        System.out.println("文档编辑器交互演示 (输入 'help' 查看帮助)");
        
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                continue;
            }
            
            String[] parts = input.split(" ", 3);
            String command = parts[0].toLowerCase();
            
            try {
                switch (command) {
                    case "insert":
                        if (parts.length >= 3) {
                            int position = Integer.parseInt(parts[1]);
                            String text = parts[2];
                            editor.insertText(position, text);
                        } else {
                            System.out.println("用法: insert <位置> <文本>");
                        }
                        break;
                        
                    case "delete":
                        if (parts.length >= 3) {
                            int position = Integer.parseInt(parts[1]);
                            int length = Integer.parseInt(parts[2]);
                            editor.deleteText(position, length);
                        } else {
                            System.out.println("用法: delete <位置> <长度>");
                        }
                        break;
                        
                    case "replace":
                        if (parts.length >= 4) {
                            int position = Integer.parseInt(parts[1]);
                            int length = Integer.parseInt(parts[2]);
                            String newText = parts[3];
                            editor.replaceText(position, length, newText);
                        } else {
                            System.out.println("用法: replace <位置> <长度> <新文本>");
                        }
                        break;
                        
                    case "undo":
                        editor.undo();
                        break;
                        
                    case "redo":
                        editor.redo();
                        break;
                        
                    case "print":
                        editor.printDocument();
                        break;
                        
                    case "help":
                        System.out.println("可用命令:");
                        System.out.println("  insert <位置> <文本>  - 在指定位置插入文本");
                        System.out.println("  delete <位置> <长度>  - 从指定位置删除指定长度的文本");
                        System.out.println("  replace <位置> <长度> <新文本> - 替换文本");
                        System.out.println("  undo                 - 撤销上一个操作");
                        System.out.println("  redo                 - 重做上一个撤销的操作");
                        System.out.println("  print                - 显示文档内容");
                        System.out.println("  help                 - 显示帮助");
                        System.out.println("  quit                 - 退出程序");
                        break;
                        
                    case "quit":
                        running = false;
                        System.out.println("再见!");
                        break;
                        
                    default:
                        System.out.println("未知命令: " + command + " (输入 'help' 查看帮助)");
                }
            } catch (NumberFormatException e) {
                System.out.println("参数格式错误，请输入正确的数字");
            } catch (Exception e) {
                System.out.println("操作失败: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
}