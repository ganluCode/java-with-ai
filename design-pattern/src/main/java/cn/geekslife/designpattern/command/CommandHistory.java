package cn.geekslife.designpattern.command;

import java.util.Stack;

/**
 * 命令历史记录类
 * 记录命令执行历史，支持多级撤销和重做
 */
public class CommandHistory {
    private Stack<Command> history = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();
    
    public void push(Command command) {
        history.push(command);
        // 清空重做栈，因为执行新命令后就不能重做之前的撤销操作了
        redoStack.clear();
    }
    
    public Command pop() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            redoStack.push(command);
            return command;
        }
        return new NoCommand();
    }
    
    public Command undo() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            redoStack.push(command);
            return command;
        }
        return new NoCommand();
    }
    
    public Command redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            history.push(command);
            return command;
        }
        return new NoCommand();
    }
    
    public boolean isEmpty() {
        return history.isEmpty();
    }
    
    public int size() {
        return history.size();
    }
    
    public void clear() {
        history.clear();
        redoStack.clear();
    }
}