package cn.geekslife.designpattern.facade.computer;

/**
 * 计算机启动器外观类
 */
public class ComputerFacade {
    private CPU cpu;
    private Memory memory;
    private HardDrive hardDrive;
    
    public ComputerFacade() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
    }
    
    public void start() {
        System.out.println("=== 启动计算机 ===");
        cpu.freeze();
        memory.load(0, hardDrive.read(0, 1024));
        cpu.jump(0);
        cpu.execute();
        System.out.println("=== 计算机启动完成 ===\n");
    }
    
    public void shutdown() {
        System.out.println("=== 关闭计算机 ===");
        // 简化实现
        System.out.println("计算机已关闭");
        System.out.println("=== 计算机关闭完成 ===\n");
    }
}