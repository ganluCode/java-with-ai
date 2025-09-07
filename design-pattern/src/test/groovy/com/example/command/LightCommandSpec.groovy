package com.example.command

import spock.lang.Specification

/**
 * 灯光命令测试类
 */
class LightCommandSpec extends Specification {
    
    def "开灯命令应该能够打开灯光"() {
        given:
        com.example.command.Light light = new com.example.command.Light("测试房间")
        com.example.command.LightOnCommand lightOnCommand = new com.example.command.LightOnCommand(light)
        
        when:
        lightOnCommand.execute()
        
        then:
        light.isOn() == true
    }
    
    def "关灯命令应该能够关闭灯光"() {
        given:
        com.example.command.Light light = new com.example.command.Light("测试房间")
        light.on() // 先打开灯
        com.example.command.LightOffCommand lightOffCommand = new com.example.command.LightOffCommand(light)
        
        when:
        lightOffCommand.execute()
        
        then:
        light.isOn() == false
    }
    
    def "开灯命令应该能够撤销操作"() {
        given:
        com.example.command.Light light = new com.example.command.Light("测试房间")
        com.example.command.LightOnCommand lightOnCommand = new com.example.command.LightOnCommand(light)
        
        when:
        lightOnCommand.execute()
        lightOnCommand.undo()
        
        then:
        light.isOn() == false
    }
    
    def "关灯命令应该能够撤销操作"() {
        given:
        com.example.command.Light light = new com.example.command.Light("测试房间")
        light.on() // 先打开灯
        com.example.command.LightOffCommand lightOffCommand = new com.example.command.LightOffCommand(light)
        
        when:
        lightOffCommand.execute()
        lightOffCommand.undo()
        
        then:
        light.isOn() == true
    }
}