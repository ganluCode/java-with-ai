package cn.geekslife.designpattern.command

import cn.geekslife.designpattern.command.Command
import cn.geekslife.designpattern.command.Light
import cn.geekslife.designpattern.command.LightOffCommand
import cn.geekslife.designpattern.command.LightOnCommand
import cn.geekslife.designpattern.command.MacroCommand
import cn.geekslife.designpattern.command.NoCommand
import cn.geekslife.designpattern.command.Stereo
import cn.geekslife.designpattern.command.StereoOffCommand
import cn.geekslife.designpattern.command.StereoOnCommand
import cn.geekslife.designpattern.command.TVOffCommand
import cn.geekslife.designpattern.command.TVOnCommand
import cn.geekslife.designpattern.command.Television
import spock.lang.Specification

/**
 * 命令模式综合测试类
 */
class CommandPatternSpec extends Specification {
    
    def "所有命令都应该实现Command接口"() {
        given:
        Command lightOnCommand = new LightOnCommand(new Light("测试"))
        Command lightOffCommand = new LightOffCommand(new Light("测试"))
        Command tvOnCommand = new TVOnCommand(new Television("测试"))
        Command tvOffCommand = new TVOffCommand(new Television("测试"))
        Command stereoOnCommand = new StereoOnCommand(new Stereo("测试"))
        Command stereoOffCommand = new StereoOffCommand(new Stereo("测试"))
        Command macroCommand = new MacroCommand(new Command[0])
        Command noCommand = new NoCommand()
        
        expect:
        lightOnCommand instanceof Command
        lightOffCommand instanceof Command
        tvOnCommand instanceof Command
        tvOffCommand instanceof Command
        stereoOnCommand instanceof Command
        stereoOffCommand instanceof Command
        macroCommand instanceof Command
        noCommand instanceof Command
    }
    
    def "所有接收者都应该正确实现各自的功能"() {
        given:
        Light light = new Light("测试房间")
        Television tv = new Television("测试房间")
        Stereo stereo = new Stereo("测试房间")
        
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
        Light light = new Light("测试房间")
        LightOnCommand lightOnCommand = new LightOnCommand(light)
        LightOffCommand lightOffCommand = new LightOffCommand(light)
        
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