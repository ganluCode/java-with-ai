package com.example.command;

/**
 * 遥控器调用者类
 * 持有命令对象的引用，负责调用命令
 */
public class RemoteControl {
    private Command[] onCommands;
    private Command[] offCommands;
    private Command undoCommand;
    
    public RemoteControl() {
        onCommands = new Command[7];
        offCommands = new Command[7];
        
        Command noCommand = new NoCommand();
        for (int i = 0; i < 7; i++) {
            onCommands[i] = noCommand;
            offCommands[i] = noCommand;
        }
        undoCommand = noCommand;
    }
    
    public void setCommand(int slot, Command onCommand, Command offCommand) {
        onCommands[slot] = onCommand;
        offCommands[slot] = offCommand;
    }
    
    public void onButtonWasPushed(int slot) {
        onCommands[slot].execute();
        undoCommand = onCommands[slot];
    }
    
    public void offButtonWasPushed(int slot) {
        offCommands[slot].execute();
        undoCommand = offCommands[slot];
    }
    
    public void undoButtonWasPushed() {
        undoCommand.undo();
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n------ 遥控器 ------\n");
        for (int i = 0; i < onCommands.length; i++) {
            buffer.append("[插槽 " + i + "] " 
                + onCommands[i].getClass().getSimpleName() 
                + "    " 
                + offCommands[i].getClass().getSimpleName() 
                + "\n");
        }
        buffer.append("[撤销] " + undoCommand.getClass().getSimpleName() + "\n");
        return buffer.toString();
    }
}