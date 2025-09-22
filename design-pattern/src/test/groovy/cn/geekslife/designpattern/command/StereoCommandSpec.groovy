package cn.geekslife.designpattern.command

import cn.geekslife.designpattern.command.Stereo
import cn.geekslife.designpattern.command.StereoOffCommand
import cn.geekslife.designpattern.command.StereoOnCommand
import spock.lang.Specification

/**
 * 音响命令测试类
 */
class StereoCommandSpec extends Specification {
    
    def "开音响命令应该能够打开音响并设置默认参数"() {
        given:
        Stereo stereo = new Stereo("测试房间")
        StereoOnCommand stereoOnCommand = new StereoOnCommand(stereo)
        
        when:
        stereoOnCommand.execute()
        
        then:
        stereo.isOn() == true
        stereo.getMode() == "CD"
        stereo.getVolume() == 11
    }
    
    def "关音响命令应该能够关闭音响"() {
        given:
        Stereo stereo = new Stereo("测试房间")
        stereo.on() // 先打开音响
        StereoOffCommand stereoOffCommand = new StereoOffCommand(stereo)
        
        when:
        stereoOffCommand.execute()
        
        then:
        stereo.isOn() == false
    }
    
    def "音响命令应该能够撤销操作"() {
        given:
        Stereo stereo = new Stereo("测试房间")
        StereoOnCommand stereoOnCommand = new StereoOnCommand(stereo)
        StereoOffCommand stereoOffCommand = new StereoOffCommand(stereo)
        
        when:
        stereo.on() // 先打开音响
        stereoOnCommand.execute()
        stereoOnCommand.undo()
        
        then:
        stereo.isOn() == true // 撤销开音响命令应该恢复到之前的状态（打开）
        
        when:
        stereo.off() // 先关闭音响
        stereoOffCommand.execute()
        stereoOffCommand.undo()
        
        then:
        stereo.isOn() == false // 撤销关音响命令应该恢复到之前的状态（关闭）
    }
}