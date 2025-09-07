package cn.geekslife.designpattern.prototype;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 样式管理器
 * 演示原型模式在实际应用中的使用
 */
public class StyleManager {
    private static Map<String, DocumentStyle> styles = new HashMap<>();
    
    static {
        // 初始化默认样式
        styles.put("default", new DocumentStyle("Arial", 12, "black"));
        styles.put("heading", new DocumentStyle("Times New Roman", 18, "blue"));
        styles.put("emphasis", new DocumentStyle("Arial", 12, "red"));
        
        // 为标题样式添加粗体效果
        styles.get("heading").addEffect("bold");
        // 为强调样式添加斜体效果
        styles.get("emphasis").addEffect("italic");
    }
    
    // 获取样式克隆
    public static DocumentStyle getStyle(String name) {
        DocumentStyle style = styles.get(name);
        return style != null ? (DocumentStyle) style.clone() : null;
    }
    
    // 注册新样式
    public static void registerStyle(String name, DocumentStyle style) {
        styles.put(name, style);
    }
    
    // 移除样式
    public static void removeStyle(String name) {
        styles.remove(name);
    }
    
    // 获取所有样式名称
    public static Set<String> getStyleNames() {
        return styles.keySet();
    }
    
    // 清空所有样式
    public static void clear() {
        styles.clear();
    }
    
    // 获取样式数量
    public static int size() {
        return styles.size();
    }
}