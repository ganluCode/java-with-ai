package com.example.command;

/**
 * 增强版遥控器类
 * 支持命令历史记录和多级撤销/重做
 */
public class RemoteControlWithHistory {
    private Command[] onCommands;
    private Command[] offCommands;
    private CommandHistory history;
    
    public RemoteControlWithHistory() {
        onCommands = new Command[7];
        offCommands = new Command[7];
        history = new CommandHistory();
        
        Command noCommand = new NoCommand();
        for (int i = 0; i < 7; i++) {
            onCommands[i] = noCommand;
            offCommands[i] = noCommand;
        }
    }
    
    public void setCommand(int slot, Command onCommand, Command offCommand) {
        onCommands[slot] = onCommand;
        offCommands[slot] = offCommand;
    }
    
    public void onButtonWasPushed(int slot) {
        onCommands[slot].execute();
        history.push(onCommands[slot]);
    }
    
    public void offButtonWasPushed(int slot) {
        offCommands[slot].execute();
        history.push(offCommands[slot]);
    }
    
    public void undoButtonWasPushed() {
        Command command = history.undo();
        command.undo();
    }
    
    public void redoButtonWasPushed() {
        Command command = history.redo();
        command.execute();
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n------ 增强版遥控器 ------\n");
        for (int i = 0; i < onCommands.length; i++) {
            buffer.append("[插槽 " + i + "] " 
                + onCommands[i].getClass().getSimpleName() 
                + "    " 
                + offCommands[i].getClass().getSimpleName() 
                + "\n");
        }
        buffer.append("[历史记录大小] " + history.size() + "\n");
        return buffer.toString();
    }
}