package com.example.command;

/**
 * 宏命令类
 * 将多个命令组合成一个复合命令
 */
public class MacroCommand implements Command {
    private Command[] commands;
    
    public MacroCommand(Command[] commands) {
        this.commands = commands;
    }
    
    @Override
    public void execute() {
        System.out.println("=== 执行宏命令 ===");
        for (int i = 0; i < commands.length; i++) {
            commands[i].execute();
        }
    }
    
    @Override
    public void undo() {
        System.out.println("=== 撤销宏命令 ===");
        // 按相反顺序撤销命令
        for (int i = commands.length - 1; i >= 0; i--) {
            commands[i].undo();
        }
    }
}