package cn.geekslife.designpattern.builder;

/**
 * 指挥者类
 * 构造一个使用Builder接口的对象
 */
public class ComputerDirector {
    
    /**
     * 构建游戏计算机
     * @param builder 计算机建造者
     * @return 游戏计算机
     */
    public Computer buildGamingComputer(ComputerBuilder builder) {
        System.out.println("=== 开始构建游戏计算机 ===");
        Computer computer = builder
                .buildCPU("Intel Core i9-12900K")
                .buildMemory("32GB DDR5 RAM")
                .buildStorage("2TB NVMe SSD")
                .buildGraphicsCard("NVIDIA GeForce RTX 4090")
                .buildMotherboard("ASUS ROG Maximus Z690 Hero")
                .buildPowerSupply("1200W 80+ Gold PSU")
                .buildCoolingSystem("Liquid Cooling System")
                .build();
        System.out.println("=== 游戏计算机构建完成 ===\n");
        return computer;
    }
    
    /**
     * 构建办公计算机
     * @param builder 计算机建造者
     * @return 办公计算机
     */
    public Computer buildOfficeComputer(ComputerBuilder builder) {
        System.out.println("=== 开始构建办公计算机 ===");
        Computer computer = builder
                .buildCPU("Intel Core i5-12600K")
                .buildMemory("16GB DDR4 RAM")
                .buildStorage("1TB SSD")
                .buildGraphicsCard("Integrated Graphics")
                .buildMotherboard("MSI B660 Tomahawk")
                .buildPowerSupply("650W 80+ Bronze PSU")
                .build();
        System.out.println("=== 办公计算机构建完成 ===\n");
        return computer;
    }
    
    /**
     * 构建服务器计算机
     * @param builder 计算机建造者
     * @return 服务器计算机
     */
    public Computer buildServerComputer(ComputerBuilder builder) {
        System.out.println("=== 开始构建服务器计算机 ===");
        Computer computer = builder
                .buildCPU("AMD EPYC 7763")
                .buildMemory("128GB ECC DDR4 RAM")
                .buildStorage("4TB NVMe SSD RAID")
                .buildMotherboard("Supermicro H12SSL-i")
                .buildPowerSupply("800W Redundant PSU")
                .buildCoolingSystem("Redundant Fans")
                .build();
        System.out.println("=== 服务器计算机构建完成 ===\n");
        return computer;
    }
    
    /**
     * 构建自定义计算机
     * @param builder 计算机建造者
     * @param cpu CPU型号
     * @param memory 内存规格
     * @param storage 存储规格
     * @return 自定义计算机
     */
    public Computer buildCustomComputer(ComputerBuilder builder, String cpu, String memory, String storage) {
        System.out.println("=== 开始构建自定义计算机 ===");
        Computer computer = builder
                .buildCPU(cpu)
                .buildMemory(memory)
                .buildStorage(storage)
                .build();
        System.out.println("=== 自定义计算机构建完成 ===\n");
        return computer;
    }
    
    /**
     * 分步构建计算机（演示建造者模式的逐步构建过程）
     * @param builder 计算机建造者
     * @return 构建完成的计算机
     */
    public Computer buildComputerStepByStep(ComputerBuilder builder) {
        System.out.println("=== 开始逐步构建计算机 ===");
        
        // 第一步：构建核心组件
        builder.buildCPU("AMD Ryzen 7 5800X")
               .buildMemory("32GB DDR4 RAM")
               .buildStorage("1TB NVMe SSD");
        
        System.out.println("核心组件构建完成");
        
        // 第二步：构建扩展组件
        builder.buildGraphicsCard("AMD Radeon RX 6800 XT")
               .buildMotherboard("ASUS TUF Gaming B550-PLUS");
        
        System.out.println("扩展组件构建完成");
        
        // 第三步：构建辅助组件
        builder.buildPowerSupply("750W 80+ Gold PSU")
               .buildCoolingSystem("Air Cooling System");
        
        System.out.println("辅助组件构建完成");
        
        Computer computer = builder.build();
        System.out.println("=== 计算机逐步构建完成 ===\n");
        return computer;
    }
}