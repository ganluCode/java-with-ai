package cn.geekslife.designpattern.template

import cn.geekslife.designpattern.template.EnhancedDatabaseTemplate
import cn.geekslife.designpattern.template.EnhancedMySQLDatabase
import spock.lang.Specification

class EnhancedDatabaseTemplateSpec extends Specification {

    def "should execute enhanced database template with callbacks"() {
        given:
        EnhancedDatabaseTemplate mysql = new EnhancedMySQLDatabase()
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        List<String> callbacks = []
        
        when:
        mysql.executeDatabaseOperation(
            connection -> callbacks.add("connected"),
            data -> callbacks.add("prepared"),
            result -> callbacks.add("executed"),
            processedResult -> callbacks.add("processed"),
            () -> callbacks.add("closed")
        )
        String output = out.toString()
        
        then:
        output.contains("开始执行增强版数据库操作")
        output.contains("连接MySQL数据库")
        output.contains("准备MySQL数据")
        output.contains("执行MySQL数据库操作")
        output.contains("处理MySQL查询结果")
        output.contains("关闭MySQL数据库连接")
        output.contains("增强版数据库操作执行完成")
        callbacks == ["connected", "prepared", "executed", "processed", "closed"]
    }
    
    def "should execute enhanced database template without callbacks"() {
        given:
        EnhancedDatabaseTemplate mysql = new EnhancedMySQLDatabase()
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        when:
        mysql.executeDatabaseOperation(null, null, null, null, null)
        String output = out.toString()
        
        then:
        output.contains("开始执行增强版数据库操作")
        output.contains("连接MySQL数据库")
        output.contains("准备MySQL数据")
        output.contains("执行MySQL数据库操作")
        output.contains("处理MySQL查询结果")
        output.contains("关闭MySQL数据库连接")
        output.contains("增强版数据库操作执行完成")
    }
}