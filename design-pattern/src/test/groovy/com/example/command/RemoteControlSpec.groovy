package com.example.command

import spock.lang.Specification

/**
 * 遥控器测试类
 */
class RemoteControlSpec extends Specification {
    
    def "遥控器应该能够创建并设置命令"() {
        given:
        com.example.command.RemoteControl remoteControl = new com.example.command.RemoteControl()
        com.example.command.Light light = new com.example.command.Light("测试房间")
        com.example.command.LightOnCommand lightOnCommand = new com.example.command.LightOnCommand(light)
        com.example.command.LightOffCommand lightOffCommand = new com.example.command.LightOffCommand(light)
        
        when:
        remoteControl.setCommand(0, lightOnCommand, lightOffCommand)
        
        then:
        // 验证不抛出异常
        noExceptionThrown()
    }
    
    def "遥控器应该能够执行命令"() {
        given:
        com.example.command.RemoteControl remoteControl = new com.example.command.RemoteControl()
        com.example.command.Light light = new com.example.command.Light("测试房间")
        com.example.command.LightOnCommand lightOnCommand = new com.example.command.LightOnCommand(light)
        com.example.command.LightOffCommand lightOffCommand = new com.example.command.LightOffCommand(light)
        
        remoteControl.setCommand(0, lightOnCommand, lightOffCommand)
        
        when:
        remoteControl.onButtonWasPushed(0)
        
        then:
        light.isOn() == true
        
        when:
        remoteControl.offButtonWasPushed(0)
        
        then:
        light.isOn() == false
    }
    
    def "遥控器应该支持撤销功能"() {
        given:
        com.example.command.RemoteControl remoteControl = new com.example.command.RemoteControl()
        com.example.command.Light light = new com.example.command.Light("测试房间")
        com.example.command.LightOnCommand lightOnCommand = new com.example.command.LightOnCommand(light)
        com.example.command.LightOffCommand lightOffCommand = new com.example.command.LightOffCommand(light)
        
        remoteControl.setCommand(0, lightOnCommand, lightOffCommand)
        
        when:
        remoteControl.onButtonWasPushed(0)
        remoteControl.undoButtonWasPushed()
        
        then:
        light.isOn() == false
    }
    
    def "遥控器应该正确显示状态信息"() {
        given:
        com.example.command.RemoteControl remoteControl = new com.example.command.RemoteControl()
        
        when:
        String info = remoteControl.toString()
        
        then:
        info != null
        info.contains("遥控器")
    }
}