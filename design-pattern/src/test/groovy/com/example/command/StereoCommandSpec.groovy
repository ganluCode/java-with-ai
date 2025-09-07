package com.example.command

import spock.lang.Specification

/**
 * 音响命令测试类
 */
class StereoCommandSpec extends Specification {
    
    def "开音响命令应该能够打开音响并设置默认参数"() {
        given:
        com.example.command.Stereo stereo = new com.example.command.Stereo("测试房间")
        com.example.command.StereoOnCommand stereoOnCommand = new com.example.command.StereoOnCommand(stereo)
        
        when:
        stereoOnCommand.execute()
        
        then:
        stereo.isOn() == true
        stereo.getMode() == "CD"
        stereo.getVolume() == 11
    }
    
    def "关音响命令应该能够关闭音响"() {
        given:
        com.example.command.Stereo stereo = new com.example.command.Stereo("测试房间")
        stereo.on() // 先打开音响
        com.example.command.StereoOffCommand stereoOffCommand = new com.example.command.StereoOffCommand(stereo)
        
        when:
        stereoOffCommand.execute()
        
        then:
        stereo.isOn() == false
    }
    
    def "音响命令应该能够撤销操作"() {
        given:
        com.example.command.Stereo stereo = new com.example.command.Stereo("测试房间")
        com.example.command.StereoOnCommand stereoOnCommand = new com.example.command.StereoOnCommand(stereo)
        com.example.command.StereoOffCommand stereoOffCommand = new com.example.command.StereoOffCommand(stereo)
        
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