package com.example.command

import spock.lang.Specification

/**
 * 命令模式综合测试类
 */
class CommandPatternSpec extends Specification {
    
    def "所有命令都应该实现Command接口"() {
        given:
        com.example.command.Command lightOnCommand = new com.example.command.LightOnCommand(new com.example.command.Light("测试"))
        com.example.command.Command lightOffCommand = new com.example.command.LightOffCommand(new com.example.command.Light("测试"))
        com.example.command.Command tvOnCommand = new com.example.command.TVOnCommand(new com.example.command.Television("测试"))
        com.example.command.Command tvOffCommand = new com.example.command.TVOffCommand(new com.example.command.Television("测试"))
        com.example.command.Command stereoOnCommand = new com.example.command.StereoOnCommand(new com.example.command.Stereo("测试"))
        com.example.command.Command stereoOffCommand = new com.example.command.StereoOffCommand(new com.example.command.Stereo("测试"))
        com.example.command.Command macroCommand = new com.example.command.MacroCommand(new com.example.command.Command[0])
        com.example.command.Command noCommand = new com.example.command.NoCommand()
        
        expect:
        lightOnCommand instanceof com.example.command.Command
        lightOffCommand instanceof com.example.command.Command
        tvOnCommand instanceof com.example.command.Command
        tvOffCommand instanceof com.example.command.Command
        stereoOnCommand instanceof com.example.command.Command
        stereoOffCommand instanceof com.example.command.Command
        macroCommand instanceof com.example.command.Command
        noCommand instanceof com.example.command.Command
    }
    
    def "所有接收者都应该正确实现各自的功能"() {
        given:
        com.example.command.Light light = new com.example.command.Light("测试房间")
        com.example.command.Television tv = new com.example.command.Television("测试房间")
        com.example.command.Stereo stereo = new com.example.command.Stereo("测试房间")
        
        when:
        light.on()
        tv.on()
        stereo.on()
        
        then:
        light.isOn() == true
        tv.isOn() == true
        stereo.isOn() == true
    }
    
    def "命令模式应该支持完整的撤销操作"() {
        given:
        com.example.command.Light light = new com.example.command.Light("测试房间")
        com.example.command.LightOnCommand lightOnCommand = new com.example.command.LightOnCommand(light)
        com.example.command.LightOffCommand lightOffCommand = new com.example.command.LightOffCommand(light)
        
        when:
        light.on() // 先打开灯
        lightOnCommand.execute()
        lightOnCommand.undo()
        
        then:
        light.isOn() == true // 撤销开灯命令应该恢复到之前的状态（打开）
        
        when:
        light.off() // 先关闭灯
        lightOffCommand.execute()
        lightOffCommand.undo()
        
        then:
        light.isOn() == false // 撤销关灯命令应该恢复到之前的状态（关闭）
    }
}