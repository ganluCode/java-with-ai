package com.example.command

import spock.lang.Specification

/**
 * 命令历史记录测试类
 */
class CommandHistorySpec extends Specification {
    
    def "命令历史记录应该能够添加和移除命令"() {
        given:
        com.example.command.CommandHistory history = new com.example.command.CommandHistory()
        com.example.command.Light light = new com.example.command.Light("测试房间")
        com.example.command.LightOnCommand command = new com.example.command.LightOnCommand(light)
        
        when:
        history.push(command)
        
        then:
        history.size() == 1
        history.isEmpty() == false
        
        when:
        com.example.command.Command poppedCommand = history.pop()
        
        then:
        poppedCommand == command
        history.isEmpty() == true
    }
    
    def "命令历史记录应该支持撤销和重做功能"() {
        given:
        com.example.command.CommandHistory history = new com.example.command.CommandHistory()
        com.example.command.Light light = new com.example.command.Light("测试房间")
        com.example.command.LightOnCommand command = new com.example.command.LightOnCommand(light)
        
        when:
        history.push(command)
        com.example.command.Command undoCommand = history.undo()
        
        then:
        undoCommand == command
        history.size() == 0
        
        when:
        com.example.command.Command redoCommand = history.redo()
        
        then:
        redoCommand == command
        history.size() == 1
    }
    
    def "命令历史记录应该在执行新命令后清空重做栈"() {
        given:
        com.example.command.CommandHistory history = new com.example.command.CommandHistory()
        com.example.command.Light light = new com.example.command.Light("测试房间")
        com.example.command.LightOnCommand command1 = new com.example.command.LightOnCommand(light)
        com.example.command.LightOffCommand command2 = new com.example.command.LightOffCommand(light)
        
        when:
        history.push(command1)
        history.undo() // command1进入重做栈
        history.push(command2) // 执行新命令
        
        then:
        // 重做栈应该被清空
        history.redo() instanceof com.example.command.NoCommand
    }
}