package cn.geekslife.designpattern.facade.computer;

/**
 * CPU子系统类
 */
public class CPU {
    public void freeze() {
        System.out.println("CPU: 冻结");
    }
    
    public void jump(long position) {
        System.out.println("CPU: 跳转到位置 " + position);
    }
    
    public void execute() {
        System.out.println("CPU: 执行指令");
    }
}