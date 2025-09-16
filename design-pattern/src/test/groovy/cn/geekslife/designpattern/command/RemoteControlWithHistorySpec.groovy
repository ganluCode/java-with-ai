package cn.geekslife.designpattern.command

import cn.geekslife.designpattern.command.Light
import cn.geekslife.designpattern.command.LightOffCommand
import cn.geekslife.designpattern.command.LightOnCommand
import cn.geekslife.designpattern.command.RemoteControlWithHistory
import spock.lang.Specification

/**
 * 增强版遥控器测试类
 */
class RemoteControlWithHistorySpec extends Specification {
    
    def "增强版遥控器应该支持命令历史记录"() {
        given:
        RemoteControlWithHistory remoteControl = new RemoteControlWithHistory()
        Light light = new Light("测试房间")
        LightOnCommand lightOnCommand = new LightOnCommand(light)
        LightOffCommand lightOffCommand = new LightOffCommand(light)
        
        remoteControl.setCommand(0, lightOnCommand, lightOffCommand)
        
        when:
        remoteControl.onButtonWasPushed(0)
        
        then:
        light.isOn() == true
    }
    
    def "增强版遥控器应该支持多级撤销"() {
        given:
        RemoteControlWithHistory remoteControl = new RemoteControlWithHistory()
        Light light = new Light("测试房间")
        LightOnCommand lightOnCommand = new LightOnCommand(light)
        LightOffCommand lightOffCommand = new LightOffCommand(light)
        
        remoteControl.setCommand(0, lightOnCommand, lightOffCommand)
        remoteControl.setCommand(1, lightOffCommand, lightOnCommand)
        
        when:
        remoteControl.onButtonWasPushed(0) // 开灯
        remoteControl.offButtonWasPushed(0) // 关灯
        remoteControl.undoButtonWasPushed() // 撤销关灯
        remoteControl.undoButtonWasPushed() // 撤销开灯
        
        then:
        light.isOn() == false
    }
    
    def "增强版遥控器应该支持重做功能"() {
        given:
        RemoteControlWithHistory remoteControl = new RemoteControlWithHistory()
        Light light = new Light("测试房间")
        LightOnCommand lightOnCommand = new LightOnCommand(light)
        
        remoteControl.setCommand(0, lightOnCommand, new LightOffCommand(light))
        
        when:
        remoteControl.onButtonWasPushed(0) // 开灯
        remoteControl.undoButtonWasPushed() // 撤销开灯
        remoteControl.redoButtonWasPushed() // 重做开灯
        
        then:
        light.isOn() == true
    }
}