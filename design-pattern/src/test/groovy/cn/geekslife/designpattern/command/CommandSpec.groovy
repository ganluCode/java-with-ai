package cn.geekslife.designpattern.command

import cn.geekslife.designpattern.command.Command
import spock.lang.Specification

/**
 * 命令接口测试类
 */
class CommandSpec extends Specification {
    
    def "命令接口应该定义execute和undo方法"() {
        given:
        Command command = Mock(Command)
        
        when:
        command.execute()
        command.undo()
        
        then:
        1 * command.execute()
        1 * command.undo()
    }
}