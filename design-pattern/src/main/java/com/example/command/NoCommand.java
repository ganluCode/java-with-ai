package com.example.command;

/**
 * 无操作命令类
 * 空对象模式的应用，避免空指针异常
 */
public class NoCommand implements Command {
    
    @Override
    public void execute() {
        // 什么都不做
    }
    
    @Override
    public void undo() {
        // 什么都不做
    }
}