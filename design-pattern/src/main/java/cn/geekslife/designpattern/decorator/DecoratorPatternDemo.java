package cn.geekslife.designpattern.decorator;

/**
 * 装饰模式演示类
 */
public class DecoratorPatternDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 装饰模式演示 ===\n");
        
        // 1. 咖啡系统演示
        demonstrateCoffeeSystem();
        
        // 2. 文本处理系统演示
        demonstrateTextProcessingSystem();
        
        // 3. 图形渲染系统演示
        demonstrateGraphicsSystem();
        
        // 4. 通知系统演示
        demonstrateNotificationSystem();
    }
    
    /**
     * 演示咖啡系统
     */
    private static void demonstrateCoffeeSystem() {
        System.out.println("1. 咖啡系统演示:");
        
        // 简单咖啡
        Coffee simpleCoffee = new SimpleCoffee();
        System.out.println("简单咖啡: " + simpleCoffee.getDescription() + " - $" + simpleCoffee.getCost());
        
        // 加牛奶的咖啡
        Coffee milkCoffee = new MilkDecorator(new SimpleCoffee());
        System.out.println("牛奶咖啡: " + milkCoffee.getDescription() + " - $" + milkCoffee.getCost());
        
        // 加糖和牛奶的咖啡
        Coffee sugarMilkCoffee = new SugarDecorator(new MilkDecorator(new SimpleCoffee()));
        System.out.println("糖牛奶咖啡: " + sugarMilkCoffee.getDescription() + " - $" + sugarMilkCoffee.getCost());
        
        // 加糖、牛奶和奶油的咖啡
        Coffee fullCoffee = new CreamDecorator(new SugarDecorator(new MilkDecorator(new SimpleCoffee())));
        System.out.println("全配咖啡: " + fullCoffee.getDescription() + " - $" + fullCoffee.getCost());
        
        System.out.println();
    }
    
    /**
     * 演示文本处理系统
     */
    private static void demonstrateTextProcessingSystem() {
        System.out.println("2. 文本处理系统演示:");
        
        // 基本文本处理
        TextProcessor basicProcessor = new BasicTextProcessor();
        String text = "Hello World";
        System.out.println("原始文本: " + basicProcessor.process(text));
        
        // 大写转换
        TextProcessor upperProcessor = new UpperCaseDecorator(new BasicTextProcessor());
        System.out.println("大写转换: " + upperProcessor.process(text));
        
        // 小写转换
        TextProcessor lowerProcessor = new LowerCaseDecorator(new BasicTextProcessor());
        System.out.println("小写转换: " + lowerProcessor.process(text));
        
        // 反转文本
        TextProcessor reverseProcessor = new ReverseDecorator(new BasicTextProcessor());
        System.out.println("反转文本: " + reverseProcessor.process(text));
        
        // 添加前缀和后缀
        TextProcessor prefixSuffixProcessor = new SuffixDecorator(
            new PrefixDecorator(new BasicTextProcessor(), "前缀-"), 
            "-后缀"
        );
        System.out.println("前后缀: " + prefixSuffixProcessor.process(text));
        
        // 复杂组合
        TextProcessor complexProcessor = new ReverseDecorator(
            new UpperCaseDecorator(
                new PrefixDecorator(new BasicTextProcessor(), "[处理后] ")
            )
        );
        System.out.println("复杂组合: " + complexProcessor.process(text));
        
        System.out.println();
    }
    
    /**
     * 演示图形渲染系统
     */
    private static void demonstrateGraphicsSystem() {
        System.out.println("3. 图形渲染系统演示:");
        
        // 基本圆形
        Shape circle = new Circle(5.0);
        System.out.println("基本圆形:");
        circle.draw();
        System.out.println("面积: " + circle.getArea());
        
        // 带红色边框的圆形
        Shape redCircle = new RedBorderDecorator(new Circle(5.0));
        System.out.println("\n红色边框圆形:");
        redCircle.draw();
        
        // 带蓝色填充的矩形
        Shape blueRectangle = new BlueFillDecorator(new Rectangle(10.0, 5.0));
        System.out.println("\n蓝色填充矩形:");
        blueRectangle.draw();
        
        // 带阴影效果的圆形
        Shape shadowCircle = new ShadowDecorator(new Circle(3.0));
        System.out.println("\n阴影圆形:");
        shadowCircle.draw();
        
        // 旋转的矩形
        Shape rotatedRectangle = new RotateDecorator(new Rectangle(8.0, 6.0), 45);
        System.out.println("\n旋转矩形:");
        rotatedRectangle.draw();
        
        // 复杂组合
        Shape complexShape = new RotateDecorator(
            new ShadowDecorator(
                new RedBorderDecorator(
                    new BlueFillDecorator(new Circle(4.0))
                )
            ), 
            30
        );
        System.out.println("\n复杂组合图形:");
        complexShape.draw();
        
        System.out.println();
    }
    
    /**
     * 演示通知系统
     */
    private static void demonstrateNotificationSystem() {
        System.out.println("4. 通知系统演示:");
        
        // 基本邮件通知
        System.out.println("基本邮件通知:");
        Notifier emailNotifier = new EmailNotifier("user@example.com");
        emailNotifier.send("Hello World!");
        
        // 邮件+SMS通知
        System.out.println("\n邮件+SMS通知:");
        Notifier emailSMSNotifier = new SMSNotifierDecorator(
            new EmailNotifier("user@example.com"), 
            "+1234567890"
        );
        emailSMSNotifier.send("Important message!");
        
        // 邮件+SMS+Slack通知
        System.out.println("\n邮件+SMS+Slack通知:");
        Notifier multiNotifier = new SlackNotifierDecorator(
            new SMSNotifierDecorator(
                new EmailNotifier("user@example.com"), 
                "+1234567890"
            ), 
            "#general"
        );
        multiNotifier.send("Critical alert!");
        
        // 加密的日志通知
        System.out.println("\n加密的日志通知:");
        Notifier secureNotifier = new LoggingNotifierDecorator(
            new EncryptedNotifierDecorator(
                new EmailNotifier("user@example.com")
            )
        );
        secureNotifier.send("Secret message!");
        
        // 复杂的通知组合
        System.out.println("\n复杂的通知组合:");
        Notifier complexNotifier = new LoggingNotifierDecorator(
            new EncryptedNotifierDecorator(
                new SlackNotifierDecorator(
                    new SMSNotifierDecorator(
                        new EmailNotifier("user@example.com"), 
                        "+1234567890"
                    ), 
                    "#alerts"
                )
            )
        );
        complexNotifier.send("Very important secure message!");
        
        System.out.println();
    }
}