package com.example.command

import spock.lang.Specification

/**
 * 宏命令测试类
 */
class MacroCommandSpec extends Specification {
    
    def "宏命令应该能够执行多个命令"() {
        given:
        com.example.command.Light light1 = new com.example.command.Light("房间1")
        com.example.command.Light light2 = new com.example.command.Light("房间2")
        com.example.command.Television tv = new com.example.command.Television("客厅")
        
        com.example.command.LightOnCommand light1On = new com.example.command.LightOnCommand(light1)
        com.example.command.LightOnCommand light2On = new com.example.command.LightOnCommand(light2)
        com.example.command.TVOnCommand tvOn = new com.example.command.TVOnCommand(tv)
        
        com.example.command.Command[] commands = [light1On, light2On, tvOn]
        com.example.command.MacroCommand macroCommand = new com.example.command.MacroCommand(commands)
        
        when:
        macroCommand.execute()
        
        then:
        light1.isOn() == true
        light2.isOn() == true
        tv.isOn() == true
    }
    
    def "宏命令应该能够撤销多个命令"() {
        given:
        com.example.command.Light light1 = new com.example.command.Light("房间1")
        com.example.command.Light light2 = new com.example.command.Light("房间2")
        com.example.command.Television tv = new com.example.command.Television("客厅")
        
        com.example.command.LightOnCommand light1On = new com.example.command.LightOnCommand(light1)
        com.example.command.LightOnCommand light2On = new com.example.command.LightOnCommand(light2)
        com.example.command.TVOnCommand tvOn = new com.example.command.TVOnCommand(tv)
        
        com.example.command.Command[] commands = [light1On, light2On, tvOn]
        com.example.command.MacroCommand macroCommand = new com.example.command.MacroCommand(commands)
        
        when:
        macroCommand.execute()
        macroCommand.undo()
        
        then:
        light1.isOn() == false
        light2.isOn() == false
        tv.isOn() == false
    }
}