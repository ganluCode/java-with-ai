package cn.geekslife.designpattern.command

import cn.geekslife.designpattern.command.Light
import cn.geekslife.designpattern.command.LightOffCommand
import cn.geekslife.designpattern.command.LightOnCommand
import spock.lang.Specification

/**
 * 灯光命令测试类
 */
class LightCommandSpec extends Specification {
    
    def "开灯命令应该能够打开灯光"() {
        given:
        Light light = new Light("测试房间")
        LightOnCommand lightOnCommand = new LightOnCommand(light)
        
        when:
        lightOnCommand.execute()
        
        then:
        light.isOn() == true
    }
    
    def "关灯命令应该能够关闭灯光"() {
        given:
        Light light = new Light("测试房间")
        light.on() // 先打开灯
        LightOffCommand lightOffCommand = new LightOffCommand(light)
        
        when:
        lightOffCommand.execute()
        
        then:
        light.isOn() == false
    }
    
    def "开灯命令应该能够撤销操作"() {
        given:
        Light light = new Light("测试房间")
        LightOnCommand lightOnCommand = new LightOnCommand(light)
        
        when:
        lightOnCommand.execute()
        lightOnCommand.undo()
        
        then:
        light.isOn() == false
    }
    
    def "关灯命令应该能够撤销操作"() {
        given:
        Light light = new Light("测试房间")
        light.on() // 先打开灯
        LightOffCommand lightOffCommand = new LightOffCommand(light)
        
        when:
        lightOffCommand.execute()
        lightOffCommand.undo()
        
        then:
        light.isOn() == true
    }
}