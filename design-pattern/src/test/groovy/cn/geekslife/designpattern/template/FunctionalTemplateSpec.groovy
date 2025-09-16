package cn.geekslife.designpattern.template

import cn.geekslife.designpattern.template.FunctionalTemplate
import spock.lang.Specification

class FunctionalTemplateSpec extends Specification {

    def "should execute functional template with all callbacks"() {
        given:
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        List<String> executionOrder = []
        
        when:
        FunctionalTemplate.executeTemplate(
            () -> executionOrder.add("init"),
            () -> {
                executionOrder.add("prepare")
                return "testData"
            },
            () -> executionOrder.add("validate"),
            data -> {
                executionOrder.add("execute")
                return "testResult"
            },
            result -> {
                executionOrder.add("process")
                return "finalResult"
            },
            () -> executionOrder.add("cleanup")
        )
        String output = out.toString()
        
        then:
        output.contains("开始执行函数式模板方法")
        output.contains("初始化完成")
        output.contains("准备数据...")
        output.contains("数据验证完成")
        output.contains("处理数据: testData")
        output.contains("处理结果: testResult")
        output.contains("资源清理完成")
        output.contains("函数式模板方法执行完成")
        executionOrder == ["init", "prepare", "validate", "execute", "process", "cleanup"]
    }
    
    def "should execute functional template with partial callbacks"() {
        given:
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        when:
        FunctionalTemplate.executeTemplate(
            null,
            () -> "minimalData",
            null,
            data -> "minimalResult",
            null,
            null
        )
        String output = out.toString()
        
        then:
        output.contains("开始执行函数式模板方法")
        output.contains("数据准备完成: minimalData")
        output.contains("操作执行完成: minimalResult")
        output.contains("函数式模板方法执行完成")
        !output.contains("初始化完成")
        !output.contains("数据验证完成")
        !output.contains("结果处理完成")
        !output.contains("资源清理完成")
    }
}