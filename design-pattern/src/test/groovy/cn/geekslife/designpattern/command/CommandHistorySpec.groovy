package cn.geekslife.designpattern.command

import cn.geekslife.designpattern.command.Command
import cn.geekslife.designpattern.command.CommandHistory
import cn.geekslife.designpattern.command.Light
import cn.geekslife.designpattern.command.LightOffCommand
import cn.geekslife.designpattern.command.LightOnCommand
import cn.geekslife.designpattern.command.NoCommand
import spock.lang.Specification

/**
 * 命令历史记录测试类
 */
class CommandHistorySpec extends Specification {
    
    def "命令历史记录应该能够添加和移除命令"() {
        given:
        CommandHistory history = new CommandHistory()
        Light light = new Light("测试房间")
        LightOnCommand command = new LightOnCommand(light)
        
        when:
        history.push(command)
        
        then:
        history.size() == 1
        history.isEmpty() == false
        
        when:
        Command poppedCommand = history.pop()
        
        then:
        poppedCommand == command
        history.isEmpty() == true
    }
    
    def "命令历史记录应该支持撤销和重做功能"() {
        given:
        CommandHistory history = new CommandHistory()
        Light light = new Light("测试房间")
        LightOnCommand command = new LightOnCommand(light)
        
        when:
        history.push(command)
        Command undoCommand = history.undo()
        
        then:
        undoCommand == command
        history.size() == 0
        
        when:
        Command redoCommand = history.redo()
        
        then:
        redoCommand == command
        history.size() == 1
    }
    
    def "命令历史记录应该在执行新命令后清空重做栈"() {
        given:
        CommandHistory history = new CommandHistory()
        Light light = new Light("测试房间")
        LightOnCommand command1 = new LightOnCommand(light)
        LightOffCommand command2 = new LightOffCommand(light)
        
        when:
        history.push(command1)
        history.undo() // command1进入重做栈
        history.push(command2) // 执行新命令
        
        then:
        // 重做栈应该被清空
        history.redo() instanceof NoCommand
    }
}