package cn.geekslife.designpattern.template

import cn.geekslife.designpattern.template.CSVFileProcessor
import cn.geekslife.designpattern.template.FileProcessor
import cn.geekslife.designpattern.template.JSONFileProcessor
import cn.geekslife.designpattern.template.XMLFileProcessor
import spock.lang.Specification

class FileProcessorSpec extends Specification {

    def "should execute xml file processor template method correctly"() {
        given:
        FileProcessor xmlProcessor = new XMLFileProcessor()
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        when:
        xmlProcessor.processFile("test.xml")
        String output = out.toString()
        
        then:
        output.contains("开始处理文件: test.xml")
        output.contains("打开XML文件: test.xml")
        output.contains("读取XML文件内容")
        output.contains("验证XML数据格式")
        output.contains("处理XML数据")
        output.contains("保存处理结果")
        output.contains("关闭XML文件")
        output.contains("文件处理完成: test.xml")
    }
    
    def "should execute csv file processor template method correctly"() {
        given:
        FileProcessor csvProcessor = new CSVFileProcessor()
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        when:
        csvProcessor.processFile("test.csv")
        String output = out.toString()
        
        then:
        output.contains("开始处理文件: test.csv")
        output.contains("打开CSV文件: test.csv")
        output.contains("读取CSV文件内容")
        output.contains("验证CSV数据格式")
        output.contains("处理CSV数据")
        output.contains("关闭CSV文件")
        output.contains("文件处理完成: test.csv")
        // CSV不需要保存结果
        !output.contains("保存处理结果")
    }
    
    def "should execute json file processor template method correctly"() {
        given:
        FileProcessor jsonProcessor = new JSONFileProcessor()
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        when:
        jsonProcessor.processFile("test.json")
        String output = out.toString()
        
        then:
        output.contains("开始处理文件: test.json")
        output.contains("打开JSON文件: test.json")
        output.contains("读取JSON文件内容")
        output.contains("处理JSON数据")
        output.contains("保存处理结果")
        output.contains("关闭JSON文件")
        output.contains("文件处理完成: test.json")
        // JSON不需要验证数据
        !output.contains("验证JSON数据格式")
    }
}