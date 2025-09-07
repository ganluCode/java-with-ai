package com.example.command

import spock.lang.Specification

/**
 * 电视命令测试类
 */
class TVCommandSpec extends Specification {
    
    def "开电视命令应该能够打开电视"() {
        given:
        com.example.command.Television tv = new com.example.command.Television("测试房间")
        com.example.command.TVOnCommand tvOnCommand = new com.example.command.TVOnCommand(tv)
        
        when:
        tvOnCommand.execute()
        
        then:
        tv.isOn() == true
    }
    
    def "关电视命令应该能够关闭电视"() {
        given:
        com.example.command.Television tv = new com.example.command.Television("测试房间")
        tv.on() // 先打开电视
        com.example.command.TVOffCommand tvOffCommand = new com.example.command.TVOffCommand(tv)
        
        when:
        tvOffCommand.execute()
        
        then:
        tv.isOn() == false
    }
    
    def "电视命令应该能够撤销操作"() {
        given:
        com.example.command.Television tv = new com.example.command.Television("测试房间")
        com.example.command.TVOnCommand tvOnCommand = new com.example.command.TVOnCommand(tv)
        com.example.command.TVOffCommand tvOffCommand = new com.example.command.TVOffCommand(tv)
        
        when:
        tv.on() // 先打开电视
        tvOnCommand.execute()
        tvOnCommand.undo()
        
        then:
        tv.isOn() == true // 撤销开灯命令应该恢复到之前的状态（打开）
        
        when:
        tv.off() // 先关闭电视
        tvOffCommand.execute()
        tvOffCommand.undo()
        
        then:
        tv.isOn() == false // 撤销关灯命令应该恢复到之前的状态（关闭）
    }
}