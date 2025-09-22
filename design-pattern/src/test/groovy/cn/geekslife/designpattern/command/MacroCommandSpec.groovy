package cn.geekslife.designpattern.command

import cn.geekslife.designpattern.command.Command
import cn.geekslife.designpattern.command.Light
import cn.geekslife.designpattern.command.LightOnCommand
import cn.geekslife.designpattern.command.MacroCommand
import cn.geekslife.designpattern.command.TVOnCommand
import cn.geekslife.designpattern.command.Television
import spock.lang.Specification

/**
 * 宏命令测试类
 */
class MacroCommandSpec extends Specification {
    
    def "宏命令应该能够执行多个命令"() {
        given:
        Light light1 = new Light("房间1")
        Light light2 = new Light("房间2")
        Television tv = new Television("客厅")

        LightOnCommand light1On = new LightOnCommand(light1)
        LightOnCommand light2On = new LightOnCommand(light2)
        TVOnCommand tvOn = new TVOnCommand(tv)

        Command[] commands = [light1On, light2On, tvOn]
        MacroCommand macroCommand = new MacroCommand(commands)
        
        when:
        macroCommand.execute()
        
        then:
        light1.isOn() == true
        light2.isOn() == true
        tv.isOn() == true
    }
    
    def "宏命令应该能够撤销多个命令"() {
        given:
        Light light1 = new Light("房间1")
        Light light2 = new Light("房间2")
        Television tv = new Television("客厅")
        
        LightOnCommand light1On = new LightOnCommand(light1)
        LightOnCommand light2On = new LightOnCommand(light2)
        TVOnCommand tvOn = new TVOnCommand(tv)
        
        Command[] commands = [light1On, light2On, tvOn]
        MacroCommand macroCommand = new MacroCommand(commands)
        
        when:
        macroCommand.execute()
        macroCommand.undo()
        
        then:
        light1.isOn() == false
        light2.isOn() == false
        tv.isOn() == false
    }
}