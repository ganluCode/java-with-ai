package cn.geekslife.designpattern.template

import cn.geekslife.designpattern.template.DatabaseTemplate
import cn.geekslife.designpattern.template.MongoDBDatabase
import cn.geekslife.designpattern.template.MySQLDatabase
import cn.geekslife.designpattern.template.PostgreSQLDatabase
import spock.lang.Specification

class DatabaseTemplateSpec extends Specification {

    def "should execute mysql database template method correctly"() {
        given:
        DatabaseTemplate mysql = new MySQLDatabase()
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        when:
        mysql.executeDatabaseOperation()
        String output = out.toString()
        
        then:
        output.contains("连接MySQL数据库")
        output.contains("准备MySQL数据")
        output.contains("执行MySQL数据库操作")
        output.contains("处理MySQL查询结果")
        output.contains("关闭MySQL数据库连接")
    }
    
    def "should execute postgresql database template method correctly"() {
        given:
        DatabaseTemplate postgresql = new PostgreSQLDatabase()
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        when:
        postgresql.executeDatabaseOperation()
        String output = out.toString()
        
        then:
        output.contains("连接PostgreSQL数据库")
        output.contains("准备PostgreSQL数据")
        output.contains("执行PostgreSQL数据库操作")
        output.contains("处理PostgreSQL查询结果")
        output.contains("关闭PostgreSQL数据库连接")
    }
    
    def "should execute mongodb database template method correctly"() {
        given:
        DatabaseTemplate mongodb = new MongoDBDatabase()
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        when:
        mongodb.executeDatabaseOperation()
        String output = out.toString()
        
        then:
        output.contains("连接MongoDB数据库")
        output.contains("准备MongoDB数据")
        output.contains("执行MongoDB数据库操作")
        output.contains("关闭MongoDB数据库连接")
        // MongoDB不需要处理结果
        !output.contains("处理MongoDB查询结果")
    }
}