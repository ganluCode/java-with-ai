package cn.geekslife.future;

/**
 * 真实数据类 - Future模式中实际的数据实现
 */
public class RealData implements Data {
    private final String content;
    
    /**
     * 构造函数 - 模拟耗时的数据构造过程
     * @param data 原始数据
     */
    public RealData(String data) {
        System.out.println("RealData：开始构造真实数据 - " + data);
        
        try {
            // 模拟耗时操作
            Thread.sleep(1000);
            
            // 构造真实数据
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                sb.append(data).append(i).append("-");
            }
            this.content = sb.toString();
            
            System.out.println("RealData：真实数据构造完成 - " + this.content);
        } catch (InterruptedException e) {
            System.out.println("RealData：数据构造被中断");
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 获取数据内容
     * @return 数据内容
     */
    @Override
    public String getContent() {
        return content;
    }
    
    /**
     * 检查数据是否准备就绪
     * @return 是否准备就绪
     */
    @Override
    public boolean isReady() {
        return true;
    }
}