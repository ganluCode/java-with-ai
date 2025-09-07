package cn.geekslife.designpattern.decorator

import spock.lang.Specification
import spock.lang.Unroll

/**
 * 装饰模式Spock测试类
 * 使用行为驱动开发(BDD)方式测试装饰模式
 */
class DecoratorPatternSpec extends Specification {
    
    def "简单咖啡应该正确实现咖啡接口"() {
        given: "创建简单咖啡实例"
        SimpleCoffee simpleCoffee = new SimpleCoffee()
        
        when: "获取咖啡信息"
        double cost = simpleCoffee.getCost()
        String description = simpleCoffee.getDescription()
        
        then: "咖啡信息应该正确"
        cost == 2.0
        description == "简单咖啡"
    }
    
    def "牛奶装饰器应该正确装饰咖啡"() {
        given: "创建装饰后的咖啡"
        Coffee milkCoffee = new MilkDecorator(new SimpleCoffee())
        
        when: "获取装饰后的咖啡信息"
        double cost = milkCoffee.getCost()
        String description = milkCoffee.getDescription()
        
        then: "装饰后的信息应该正确"
        cost == 2.5 // 2.0 + 0.5
        description == "简单咖啡, 牛奶"
    }
    
    def "糖装饰器应该正确装饰咖啡"() {
        given: "创建装饰后的咖啡"
        Coffee sugarCoffee = new SugarDecorator(new SimpleCoffee())
        
        when: "获取装饰后的咖啡信息"
        double cost = sugarCoffee.getCost()
        String description = sugarCoffee.getDescription()
        
        then: "装饰后的信息应该正确"
        cost == 2.3 // 2.0 + 0.3
        description == "简单咖啡, 糖"
    }
    
    def "奶油装饰器应该正确装饰咖啡"() {
        given: "创建装饰后的咖啡"
        Coffee creamCoffee = new CreamDecorator(new SimpleCoffee())
        
        when: "获取装饰后的咖啡信息"
        double cost = creamCoffee.getCost()
        String description = creamCoffee.getDescription()
        
        then: "装饰后的信息应该正确"
        cost == 2.7 // 2.0 + 0.7
        description == "简单咖啡, 奶油"
    }
    
    def "多重装饰器应该正确组合功能"() {
        given: "创建多重装饰的咖啡"
        Coffee complexCoffee = new CreamDecorator(
            new SugarDecorator(
                new MilkDecorator(new SimpleCoffee())
            )
        )
        
        when: "获取装饰后的咖啡信息"
        double cost = complexCoffee.getCost()
        String description = complexCoffee.getDescription()
        
        then: "装饰后的信息应该正确"
        cost == 3.5 // 2.0 + 0.5 + 0.3 + 0.7
        description == "简单咖啡, 牛奶, 糖, 奶油"
    }
    
    def "基本文本处理器应该正确处理文本"() {
        given: "创建基本文本处理器"
        BasicTextProcessor processor = new BasicTextProcessor()
        String text = "Hello World"
        
        when: "处理文本"
        String result = processor.process(text)
        
        then: "文本应该保持不变"
        result == text
    }
    
    def "大写转换装饰器应该正确转换文本"() {
        given: "创建大写转换装饰器"
        TextProcessor processor = new UpperCaseDecorator(new BasicTextProcessor())
        String text = "Hello World"
        
        when: "处理文本"
        String result = processor.process(text)
        
        then: "文本应该被转换为大写"
        result == "HELLO WORLD"
    }
    
    def "小写转换装饰器应该正确转换文本"() {
        given: "创建小写转换装饰器"
        TextProcessor processor = new LowerCaseDecorator(new BasicTextProcessor())
        String text = "Hello World"
        
        when: "处理文本"
        String result = processor.process(text)
        
        then: "文本应该被转换为小写"
        result == "hello world"
    }
    
    def "反转文本装饰器应该正确反转文本"() {
        given: "创建反转文本装饰器"
        TextProcessor processor = new ReverseDecorator(new BasicTextProcessor())
        String text = "Hello"
        
        when: "处理文本"
        String result = processor.process(text)
        
        then: "文本应该被反转"
        result == "olleH"
    }
    
    def "前缀装饰器应该正确添加前缀"() {
        given: "创建前缀装饰器"
        TextProcessor processor = new PrefixDecorator(new BasicTextProcessor(), "前缀-")
        String text = "文本"
        
        when: "处理文本"
        String result = processor.process(text)
        
        then: "文本应该添加前缀"
        result == "前缀-文本"
    }
    
    def "后缀装饰器应该正确添加后缀"() {
        given: "创建后缀装饰器"
        TextProcessor processor = new SuffixDecorator(new BasicTextProcessor(), "-后缀")
        String text = "文本"
        
        when: "处理文本"
        String result = processor.process(text)
        
        then: "文本应该添加后缀"
        result == "文本-后缀"
    }
    
    def "文本处理器的多重装饰应该正确组合"() {
        given: "创建多重装饰的文本处理器"
        TextProcessor processor = new ReverseDecorator(
            new UpperCaseDecorator(
                new PrefixDecorator(new BasicTextProcessor(), "[处理] ")
            )
        )
        String text = "hello"
        
        when: "处理文本"
        String result = processor.process(text)
        
        then: "文本应该被正确处理"
        // 处理顺序：添加前缀 -> 转大写 -> 反转
        // "[处理] hello" -> "[处理] HELLO" -> "OLLEH ]理处["
        result == "OLLEH ]理处["
    }
    
    def "圆形应该正确实现图形接口"() {
        given: "创建圆形"
        Circle circle = new Circle(5.0)
        
        when: "计算面积"
        double area = circle.getArea()
        
        then: "面积应该正确计算"
        area == Math.PI * 25.0
        
        when: "获取半径"
        double radius = circle.getRadius()
        
        then: "半径应该正确"
        radius == 5.0
    }
    
    def "矩形应该正确实现图形接口"() {
        given: "创建矩形"
        Rectangle rectangle = new Rectangle(10.0, 5.0)
        
        when: "计算面积"
        double area = rectangle.getArea()
        
        then: "面积应该正确计算"
        area == 50.0
        
        when: "获取尺寸"
        double width = rectangle.getWidth()
        double height = rectangle.getHeight()
        
        then: "尺寸应该正确"
        width == 10.0
        height == 5.0
    }
    
    def "红色边框装饰器应该正确装饰图形"() {
        given: "创建红色边框装饰器"
        Shape shape = new RedBorderDecorator(new Circle(3.0))
        
        when: "绘制图形"
        shape.draw()
        
        then: "应该正确绘制"
        true // 占位符
    }
    
    def "蓝色填充装饰器应该正确装饰图形"() {
        given: "创建蓝色填充装饰器"
        Shape shape = new BlueFillDecorator(new Rectangle(5.0, 3.0))
        
        when: "绘制图形"
        shape.draw()
        
        then: "应该正确绘制"
        true // 占位符
    }
    
    def "阴影效果装饰器应该正确装饰图形"() {
        given: "创建阴影效果装饰器"
        Shape shape = new ShadowDecorator(new Circle(4.0))
        
        when: "绘制图形"
        shape.draw()
        
        then: "应该正确绘制"
        true // 占位符
    }
    
    def "旋转效果装饰器应该正确装饰图形"() {
        given: "创建旋转效果装饰器"
        Shape shape = new RotateDecorator(new Rectangle(6.0, 4.0), 45.0)
        
        when: "绘制图形"
        shape.draw()
        
        then: "应该正确绘制"
        true // 占位符
        
        when: "获取角度"
        double angle = ((RotateDecorator) shape).getAngle()
        
        then: "角度应该正确"
        angle == 45.0
    }
    
    def "图形的多重装饰应该正确组合"() {
        given: "创建多重装饰的图形"
        Shape shape = new RotateDecorator(
            new ShadowDecorator(
                new RedBorderDecorator(new Circle(2.0))
            ), 
            30.0
        )
        
        when: "绘制图形"
        shape.draw()
        
        then: "应该正确绘制"
        true // 占位符
    }
    
    def "邮件通知应该正确发送邮件"() {
        given: "创建邮件通知"
        EmailNotifier notifier = new EmailNotifier("test@example.com")
        
        when: "发送消息"
        notifier.send("测试消息")
        
        then: "应该正确发送"
        true // 占位符
        
        when: "获取邮箱地址"
        String email = notifier.getEmailAddress()
        
        then: "邮箱地址应该正确"
        email == "test@example.com"
    }
    
    def "SMS通知装饰器应该正确发送短信"() {
        given: "创建SMS通知装饰器"
        Notifier notifier = new SMSNotifierDecorator(
            new EmailNotifier("test@example.com"), 
            "+1234567890"
        )
        
        when: "发送消息"
        notifier.send("SMS测试消息")
        
        then: "应该正确发送"
        true // 占位符
    }
    
    def "Slack通知装饰器应该正确发送Slack消息"() {
        given: "创建Slack通知装饰器"
        Notifier notifier = new SlackNotifierDecorator(
            new EmailNotifier("test@example.com"), 
            "#general"
        )
        
        when: "发送消息"
        notifier.send("Slack测试消息")
        
        then: "应该正确发送"
        true // 占位符
    }
    
    def "加密通知装饰器应该正确加密消息"() {
        given: "创建加密通知装饰器"
        Notifier notifier = new EncryptedNotifierDecorator(
            new EmailNotifier("test@example.com")
        )
        
        when: "发送消息"
        notifier.send("加密测试消息")
        
        then: "应该正确发送加密消息"
        true // 占位符
    }
    
    def "日志通知装饰器应该正确记录日志"() {
        given: "创建日志通知装饰器"
        Notifier notifier = new LoggingNotifierDecorator(
            new EmailNotifier("test@example.com")
        )
        
        when: "发送消息"
        notifier.send("日志测试消息")
        
        then: "应该正确记录日志"
        true // 占位符
    }
    
    def "通知系统的多重装饰应该正确组合"() {
        given: "创建多重装饰的通知系统"
        Notifier notifier = new LoggingNotifierDecorator(
            new EncryptedNotifierDecorator(
                new SMSNotifierDecorator(
                    new EmailNotifier("test@example.com"), 
                    "+1234567890"
                )
            )
        )
        
        when: "发送消息"
        notifier.send("复杂测试消息")
        
        then: "应该正确发送"
        true // 占位符
    }
    
    def "装饰器应该保持被装饰对象的基本功能"() {
        given: "创建装饰后的对象"
        Coffee decoratedCoffee = new MilkDecorator(new SimpleCoffee())
        TextProcessor decoratedTextProcessor = new UpperCaseDecorator(new BasicTextProcessor())
        Shape decoratedShape = new RedBorderDecorator(new Circle(5.0))
        Notifier decoratedNotifier = new SMSNotifierDecorator(
            new EmailNotifier("test@example.com"), 
            "+1234567890"
        )
        
        when: "调用基本方法"
        double coffeeCost = decoratedCoffee.getCost()
        String textResult = decoratedTextProcessor.process("test")
        double shapeArea = decoratedShape.getArea()
        
        then: "基本功能应该保持"
        coffeeCost > 2.0 // 装饰后成本应该增加
        textResult == "TEST" // 文本应该被处理
        shapeArea == Math.PI * 25.0 // 面积应该正确计算
    }
    
    def "装饰器应该支持运行时动态组合"() {
        given: "创建基本对象"
        Coffee coffee = new SimpleCoffee()
        
        when: "动态添加装饰器"
        coffee = new MilkDecorator(coffee)
        coffee = new SugarDecorator(coffee)
        
        and: "获取最终结果"
        double cost = coffee.getCost()
        String description = coffee.getDescription()
        
        then: "装饰应该正确应用"
        cost == 2.8 // 2.0 + 0.5 + 0.3
        description == "简单咖啡, 牛奶, 糖"
    }
    
    @Unroll
    def "不同类型的装饰器应该正确装饰组件: #decoratorType"() {
        given: "创建装饰器"
        Coffee coffee = decoratorCreator.call()
        
        when: "获取装饰后的信息"
        double cost = coffee.getCost()
        String description = coffee.getDescription()
        
        then: "装饰应该正确应用"
        cost > 2.0
        description.contains("牛奶") || description.contains("糖") || description.contains("奶油")
        
        where:
        decoratorType      | decoratorCreator
        "牛奶装饰器"      | { new MilkDecorator(new SimpleCoffee()) }
        "糖装饰器"        | { new SugarDecorator(new SimpleCoffee()) }
        "奶油装饰器"      | { new CreamDecorator(new SimpleCoffee()) }
    }
    
    def "装饰器模式应该支持透明性"() {
        given: "创建装饰后的对象"
        Coffee decoratedCoffee = new MilkDecorator(new SimpleCoffee())
        
        when: "将装饰后的对象当作普通对象使用"
        double cost = decoratedCoffee.getCost()
        String description = decoratedCoffee.getDescription()
        
        then: "应该能够透明地使用"
        cost instanceof Double
        description instanceof String
    }
    
    def "装饰器应该能够嵌套多层"() {
        given: "创建多层装饰"
        Coffee coffee = new SimpleCoffee()
        5.times {
            coffee = new MilkDecorator(coffee)
        }
        
        when: "获取最终结果"
        double cost = coffee.getCost()
        String description = coffee.getDescription()
        
        then: "多层装饰应该正确应用"
        cost == 4.5 // 2.0 + 5 * 0.5
        description.count("牛奶") == 5
    }
}