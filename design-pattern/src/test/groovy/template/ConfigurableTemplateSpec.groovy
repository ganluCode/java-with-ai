package template

import spock.lang.Specification

class ConfigurableTemplateSpec extends Specification {

    def "should execute configurable template with all steps"() {
        given:
        ConfigurableTemplate template = new ConcreteConfigurableTemplate(true, true, true)
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        when:
        template.execute()
        String output = out.toString()
        
        then:
        output.contains("开始执行配置驱动的模板方法")
        output.contains("初始化配置驱动模板")
        output.contains("准备数据")
        output.contains("数据准备完成")
        output.contains("验证数据")
        output.contains("执行核心操作")
        output.contains("操作执行完成")
        output.contains("处理结果")
        output.contains("结果处理完成")
        output.contains("清理资源")
        output.contains("配置驱动的模板方法执行完成")
    }
    
    def "should execute configurable template with partial steps"() {
        given:
        ConfigurableTemplate template = new ConcreteConfigurableTemplate(false, true, false)
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        
        when:
        template.execute()
        String output = out.toString()
        
        then:
        output.contains("开始执行配置驱动的模板方法")
        output.contains("初始化配置驱动模板")
        !output.contains("准备数据")
        !output.contains("数据准备完成")
        !output.contains("验证数据")
        output.contains("执行核心操作")
        output.contains("操作执行完成")
        output.contains("处理结果")
        output.contains("结果处理完成")
        output.contains("清理资源")
        output.contains("配置驱动的模板方法执行完成")
    }
    
    def "should return correct configuration values"() {
        given:
        ConfigurableTemplate template1 = new ConcreteConfigurableTemplate(true, false, true)
        ConfigurableTemplate template2 = new ConcreteConfigurableTemplate(false, true, false)
        
        when:
        boolean needPrepare1 = template1.isNeedPrepareData()
        boolean needProcess1 = template1.isNeedProcessResult()
        boolean needValidate1 = template1.isNeedValidation()
        
        boolean needPrepare2 = template2.isNeedPrepareData()
        boolean needProcess2 = template2.isNeedProcessResult()
        boolean needValidate2 = template2.isNeedValidation()
        
        then:
        needPrepare1 == true
        needProcess1 == false
        needValidate1 == true
        
        needPrepare2 == false
        needProcess2 == true
        needValidate2 == false
    }
}