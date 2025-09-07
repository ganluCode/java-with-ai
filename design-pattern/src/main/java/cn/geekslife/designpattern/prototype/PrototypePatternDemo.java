package cn.geekslife.designpattern.prototype;

import java.util.Arrays;

/**
 * 原型模式演示类
 * 展示原型模式的使用方法
 */
public class PrototypePatternDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 原型模式演示 ===\n");
        
        // 测试StyleManager初始化
        testStyleManager();
        
        // 1. 浅克隆演示
        demonstrateShallowClone();
        
        // 2. 深克隆演示
        demonstrateDeepClone();
        
        // 3. 序列化克隆演示
        demonstrateSerializationClone();
        
        // 4. 文档样式演示
        demonstrateDocumentStyle();
        
        // 5. 原型管理器演示
        demonstratePrototypeManager();
    }
    
    /**
     * 测试StyleManager初始化
     */
    private static void testStyleManager() {
        System.out.println("0. StyleManager初始化测试:");
        
        DocumentStyle defaultStyle = StyleManager.getStyle("default");
        DocumentStyle headingStyle = StyleManager.getStyle("heading");
        DocumentStyle emphasisStyle = StyleManager.getStyle("emphasis");
        
        System.out.println("  Default style: " + defaultStyle);
        System.out.println("  Heading style: " + headingStyle);
        System.out.println("  Emphasis style: " + emphasisStyle);
        System.out.println("  StyleManager size: " + StyleManager.size());
        System.out.println("  Style names: " + StyleManager.getStyleNames());
        System.out.println();
    }
    
    /**
     * 演示浅克隆
     */
    private static void demonstrateShallowClone() {
        System.out.println("1. 浅克隆演示:");
        
        ShallowClonePrototype original = new ShallowClonePrototype("原始对象", 100);
        ShallowClonePrototype cloned = (ShallowClonePrototype) original.clone();
        
        System.out.println("  原始对象: " + original);
        System.out.println("  克隆对象: " + cloned);
        System.out.println("  是否为同一对象: " + (original == cloned));
        System.out.println("  内容是否相等: " + original.equals(cloned));
        System.out.println();
    }
    
    /**
     * 演示深克隆
     */
    private static void demonstrateDeepClone() {
        System.out.println("2. 深克隆演示:");
        
        Address address = new Address("北京市朝阳区", "北京");
        DeepClonePrototype original = new DeepClonePrototype(
            "原始对象", 
            Arrays.asList("标签1", "标签2"), 
            address
        );
        
        DeepClonePrototype cloned = (DeepClonePrototype) original.clone();
        
        System.out.println("  原始对象: " + original);
        System.out.println("  克隆对象: " + cloned);
        System.out.println("  是否为同一对象: " + (original == cloned));
        System.out.println("  地址是否为同一对象: " + (original.getAddress() == cloned.getAddress()));
        System.out.println();
        
        // 修改克隆对象的属性
        cloned.getTags().add("新标签");
        cloned.getAddress().setStreet("上海市浦东新区");
        
        System.out.println("  修改克隆对象后:");
        System.out.println("  原始对象: " + original);
        System.out.println("  克隆对象: " + cloned);
        System.out.println();
    }
    
    /**
     * 演示序列化克隆
     */
    private static void demonstrateSerializationClone() {
        System.out.println("3. 序列化克隆演示:");
        
        Address address = new Address("广州市天河区", "广州");
        SerializationPrototype original = new SerializationPrototype(
            "序列化对象",
            Arrays.asList("序列化标签1", "序列化标签2"),
            address
        );
        
        SerializationPrototype cloned = original.deepClone();
        
        System.out.println("  原始对象: " + original);
        System.out.println("  克隆对象: " + cloned);
        System.out.println("  是否为同一对象: " + (original == cloned));
        System.out.println("  内容是否相等: " + original.equals(cloned));
        System.out.println();
    }
    
    /**
     * 演示文档样式
     */
    private static void demonstrateDocumentStyle() {
        System.out.println("4. 文档样式演示:");
        
        // 使用默认样式
        DocumentStyle headingStyle = StyleManager.getStyle("heading");
        if (headingStyle != null) {
            headingStyle.setFontSize(24); // 个性化调整
            System.out.println("  标题样式: " + headingStyle);
        }
        
        // 使用强调样式
        DocumentStyle emphasisStyle = StyleManager.getStyle("emphasis");
        if (emphasisStyle != null) {
            emphasisStyle.addEffect("underline"); // 添加下划线效果
            System.out.println("  强调样式: " + emphasisStyle);
        }
        
        // 创建新样式并注册
        DocumentStyle codeStyle = StyleManager.getStyle("default");
        if (codeStyle != null) {
            codeStyle.setFontFamily("Courier New");
            codeStyle.setColor("green");
            codeStyle.addEffect("monospace");
            StyleManager.registerStyle("code", codeStyle);
            
            // 使用新注册的样式
            DocumentStyle newCodeStyle = StyleManager.getStyle("code");
            System.out.println("  代码样式: " + newCodeStyle);
        }
        System.out.println();
    }
    
    /**
     * 演示原型管理器
     */
    private static void demonstratePrototypeManager() {
        System.out.println("5. 原型管理器演示:");
        
        // 注册自定义原型
        PrototypeManager.registerPrototype("custom1", new ShallowClonePrototype("自定义1", 1000));
        PrototypeManager.registerPrototype("custom2", new ShallowClonePrototype("自定义2", 2000));
        
        // 获取原型
        Prototype custom1 = PrototypeManager.getPrototype("custom1");
        Prototype custom2 = PrototypeManager.getPrototype("custom2");
        
        System.out.println("  自定义原型1: " + custom1);
        System.out.println("  自定义原型2: " + custom2);
        System.out.println("  原型管理器中原型数量: " + PrototypeManager.size());
        
        // 清理
        PrototypeManager.clear();
        System.out.println("  清理后原型数量: " + PrototypeManager.size());
        System.out.println();
    }
}