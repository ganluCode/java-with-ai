package cn.geekslife.designpattern.structural;

/**
 * AdapterPattern - 适配器模式示例
 */
public class AdapterPattern {
    
    // 目标接口
    public interface Target {
        void request();
    }
    
    // 被适配者类
    public static class Adaptee {
        public void specificRequest() {
            System.out.println("Adaptee's specific request");
        }
    }
    
    // 适配器类
    public static class Adapter implements Target {
        private Adaptee adaptee;
        
        public Adapter(Adaptee adaptee) {
            this.adaptee = adaptee;
        }
        
        @Override
        public void request() {
            adaptee.specificRequest();
        }
    }
}