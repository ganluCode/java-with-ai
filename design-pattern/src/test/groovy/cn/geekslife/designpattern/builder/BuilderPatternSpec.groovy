package cn.geekslife.designpattern.builder

import spock.lang.Specification
import spock.lang.Unroll

/**
 * 建造者模式Spock测试类
 * 使用行为驱动开发(BDD)方式测试建造者模式
 */
class BuilderPatternSpec extends Specification {
    
    def "应该能够通过具体建造者创建计算机产品"() {
        given: "创建具体建造者实例"
        ComputerBuilder builder = new ConcreteComputerBuilder()
        
        when: "构建计算机"
        Computer computer = builder
                .buildCPU("Intel Core i7-12700K")
                .buildMemory("32GB DDR5 RAM")
                .buildStorage("1TB NVMe SSD")
                .buildGraphicsCard("NVIDIA GeForce RTX 3080")
                .build()
        
        then: "验证创建的计算机正确"
        computer.getCpu() == "Intel Core i7-12700K"
        computer.getMemory() == "32GB DDR5 RAM"
        computer.getStorage() == "1TB NVMe SSD"
        computer.getGraphicsCard() == "NVIDIA GeForce RTX 3080"
        computer.getMotherboard() == null // 未设置的属性应该为null
    }
    
    def "应该能够通过流式建造者创建计算机产品"() {
        given: "创建流式建造者实例"
        FluentComputerBuilder fluentBuilder = new FluentComputerBuilder()
        
        when: "使用快速构建方法创建基础计算机"
        Computer basicComputer = fluentBuilder
                .buildBasic("AMD Ryzen 5 5600X", "16GB DDR4 RAM", "512GB SSD")
                .build()
        
        then: "验证基础计算机正确"
        basicComputer.getCpu() == "AMD Ryzen 5 5600X"
        basicComputer.getMemory() == "16GB DDR4 RAM"
        basicComputer.getStorage() == "512GB SSD"
        
        when: "重新使用建造者创建高性能计算机"
        Computer highPerformanceComputer = fluentBuilder
                .buildHighPerformance("Intel Core i9-12900K", "64GB DDR5 RAM", "2TB NVMe SSD", "NVIDIA GeForce RTX 4090")
                .build()
        
        then: "验证高性能计算机正确"
        highPerformanceComputer.getCpu() == "Intel Core i9-12900K"
        highPerformanceComputer.getMemory() == "64GB DDR5 RAM"
        highPerformanceComputer.getStorage() == "2TB NVMe SSD"
        highPerformanceComputer.getGraphicsCard() == "NVIDIA GeForce RTX 4090"
    }
    
    def "带约束的建造者应该验证必要组件"() {
        given: "创建带约束的建造者实例"
        ConstrainedComputerBuilder constrainedBuilder = new ConstrainedComputerBuilder()
        
        when: "尝试在没有设置必要组件的情况下构建"
        constrainedBuilder.build()
        
        then: "应该抛出IllegalStateException异常"
        thrown(IllegalStateException)
        
        when: "正确设置必要组件后构建"
        Computer computer = constrainedBuilder
                .buildCPU("Intel Core i5-12600K")
                .buildMemory("16GB DDR4 RAM")
                .buildStorage("1TB SSD")
                .build()
        
        then: "验证计算机创建成功"
        computer.getCpu() == "Intel Core i5-12600K"
        computer.getMemory() == "16GB DDR4 RAM"
        computer.getStorage() == "1TB SSD"
    }
    
    def "带约束的建造者应该验证组件参数"() {
        given: "创建带约束的建造者实例"
        ConstrainedComputerBuilder constrainedBuilder = new ConstrainedComputerBuilder()
        
        when: "使用空参数设置CPU"
        constrainedBuilder.buildCPU("")
        
        then: "应该抛出IllegalArgumentException异常"
        thrown(IllegalArgumentException)
        
        when: "使用null参数设置内存"
        constrainedBuilder.buildMemory(null)
        
        then: "应该抛出IllegalArgumentException异常"
        thrown(IllegalArgumentException)
    }
    
    def "应该能够通过指挥者创建不同类型的计算机"() {
        given: "创建指挥者和建造者实例"
        ComputerDirector director = new ComputerDirector()
        ComputerBuilder builder = new ConcreteComputerBuilder()
        
        when: "构建游戏计算机"
        Computer gamingComputer = director.buildGamingComputer(builder)
        
        then: "验证游戏计算机配置正确"
        gamingComputer.getCpu() == "Intel Core i9-12900K"
        gamingComputer.getMemory() == "32GB DDR5 RAM"
        gamingComputer.getStorage() == "2TB NVMe SSD"
        gamingComputer.getGraphicsCard() == "NVIDIA GeForce RTX 4090"
        
        when: "构建办公计算机"
        Computer officeComputer = director.buildOfficeComputer(builder)
        
        then: "验证办公计算机配置正确"
        officeComputer.getCpu() == "Intel Core i5-12600K"
        officeComputer.getMemory() == "16GB DDR4 RAM"
        officeComputer.getStorage() == "1TB SSD"
        officeComputer.getGraphicsCard() == "Integrated Graphics"
        
        when: "构建服务器计算机"
        Computer serverComputer = director.buildServerComputer(builder)
        
        then: "验证服务器计算机配置正确"
        serverComputer.getCpu() == "AMD EPYC 7763"
        serverComputer.getMemory() == "128GB ECC DDR4 RAM"
        serverComputer.getStorage() == "4TB NVMe SSD RAID"
    }
    
    def "应该能够构建自定义计算机"() {
        given: "创建指挥者和建造者实例"
        ComputerDirector director = new ComputerDirector()
        ComputerBuilder builder = new ConcreteComputerBuilder()
        
        when: "构建自定义计算机"
        Computer customComputer = director.buildCustomComputer(builder, 
                "AMD Ryzen 7 5800X", "32GB DDR4 RAM", "1TB NVMe SSD")
        
        then: "验证自定义计算机配置正确"
        customComputer.getCpu() == "AMD Ryzen 7 5800X"
        customComputer.getMemory() == "32GB DDR4 RAM"
        customComputer.getStorage() == "1TB NVMe SSD"
    }
    
    def "应该能够逐步构建计算机"() {
        given: "创建指挥者和建造者实例"
        ComputerDirector director = new ComputerDirector()
        ComputerBuilder builder = new ConcreteComputerBuilder()
        
        when: "逐步构建计算机"
        Computer stepByStepComputer = director.buildComputerStepByStep(builder)
        
        then: "验证逐步构建的计算机配置正确"
        stepByStepComputer.getCpu() == "AMD Ryzen 7 5800X"
        stepByStepComputer.getMemory() == "32GB DDR4 RAM"
        stepByStepComputer.getStorage() == "1TB NVMe SSD"
        stepByStepComputer.getGraphicsCard() == "AMD Radeon RX 6800 XT"
        stepByStepComputer.getMotherboard() == "ASUS TUF Gaming B550-PLUS"
    }
    
    @Unroll
    def "建造者应该能够重置状态并构建新的产品: #testDescription"() {
        given: "创建建造者实例"
        ComputerBuilder builder = builderType.newInstance()
        
        when: "第一次构建计算机"
        Computer firstComputer = builder
                .buildCPU("First CPU")
                .buildMemory("First Memory")
                .build()
        
        and: "重置建造者并构建第二个计算机"
        Computer secondComputer = builder
                .buildCPU("Second CPU")
                .buildMemory("Second Memory")
                .buildStorage("Second Storage")
                .build()
        
        then: "验证两个计算机是不同的实例且配置正确"
        firstComputer.getCpu() == "First CPU"
        firstComputer.getMemory() == "First Memory"
        firstComputer.getStorage() == null // 第一个计算机没有设置存储
        
        secondComputer.getCpu() == "Second CPU"
        secondComputer.getMemory() == "Second Memory"
        secondComputer.getStorage() == "Second Storage"
        
        where:
        builderType                  | testDescription
        ConcreteComputerBuilder.class | "具体建造者"
        FluentComputerBuilder.class   | "流式建造者"
        // ConstrainedComputerBuilder.class | "带约束的建造者" // 暂时排除，因为测试逻辑需要调整
    }
    
    def "带约束的建造者应该能够检查构建进度"() {
        given: "创建带约束的建造者实例"
        ConstrainedComputerBuilder constrainedBuilder = new ConstrainedComputerBuilder()
        
        when: "检查初始构建进度"
        String initialProgress = constrainedBuilder.getBuildProgress()
        
        then: "验证初始进度正确"
        initialProgress.contains("CPU: ✗")
        initialProgress.contains("内存: ✗")
        initialProgress.contains("存储: ✗")
        !constrainedBuilder.isComplete()
        
        when: "设置CPU并检查进度"
        constrainedBuilder.buildCPU("Intel Core i5-12600K")
        String cpuProgress = constrainedBuilder.getBuildProgress()
        
        then: "验证CPU进度正确"
        cpuProgress.contains("CPU: ✓")
        cpuProgress.contains("内存: ✗")
        cpuProgress.contains("存储: ✗")
        !constrainedBuilder.isComplete()
        
        when: "设置内存和存储并检查进度"
        constrainedBuilder.buildMemory("16GB DDR4 RAM")
                .buildStorage("1TB SSD")
        String completeProgress = constrainedBuilder.getBuildProgress()
        
        then: "验证完整进度正确"
        completeProgress.contains("CPU: ✓")
        completeProgress.contains("内存: ✓")
        completeProgress.contains("存储: ✓")
        constrainedBuilder.isComplete()
    }
    
    def "计算机产品应该能够正确显示配置摘要"() {
        given: "创建计算机实例"
        Computer computer = new Computer()
        computer.setCpu("Intel Core i7-12700K")
        computer.setMemory("32GB DDR5 RAM")
        computer.setStorage("1TB NVMe SSD")
        computer.setGraphicsCard("NVIDIA GeForce RTX 3080")
        
        when: "获取配置摘要"
        String summary = computer.getConfigurationSummary()
        
        then: "验证配置摘要正确"
        summary.contains("计算机配置:")
        summary.contains("CPU: Intel Core i7-12700K")
        summary.contains("内存: 32GB DDR5 RAM")
        summary.contains("存储: 1TB NVMe SSD")
        summary.contains("显卡: NVIDIA GeForce RTX 3080")
    }
    
    def "建造者应该支持方法链式调用"() {
        given: "创建具体建造者实例"
        ConcreteComputerBuilder builder = new ConcreteComputerBuilder()
        
        when: "使用方法链式调用构建计算机"
        Computer computer = builder
                .buildCPU("Intel Core i9-12900K")
                .buildMemory("64GB DDR5 RAM")
                .buildStorage("2TB NVMe SSD")
                .buildGraphicsCard("NVIDIA GeForce RTX 4090")
                .buildMotherboard("ASUS ROG Maximus Z690 Hero")
                .buildPowerSupply("1200W 80+ Gold PSU")
                .buildCoolingSystem("Liquid Cooling System")
                .build()
        
        then: "验证计算机配置正确"
        computer.getCpu() == "Intel Core i9-12900K"
        computer.getMemory() == "64GB DDR5 RAM"
        computer.getStorage() == "2TB NVMe SSD"
        computer.getGraphicsCard() == "NVIDIA GeForce RTX 4090"
        computer.getMotherboard() == "ASUS ROG Maximus Z690 Hero"
        computer.getPowerSupply() == "1200W 80+ Gold PSU"
        computer.getCoolingSystem() == "Liquid Cooling System"
    }
    
    def "不同的建造者实例应该能够独立工作"() {
        given: "创建两个不同的建造者实例"
        ConcreteComputerBuilder builder1 = new ConcreteComputerBuilder()
        ConcreteComputerBuilder builder2 = new ConcreteComputerBuilder()
        
        when: "分别使用两个建造者构建不同的计算机"
        Computer computer1 = builder1
                .buildCPU("Builder1 CPU")
                .buildMemory("Builder1 Memory")
                .build()
        
        Computer computer2 = builder2
                .buildCPU("Builder2 CPU")
                .buildMemory("Builder2 Memory")
                .buildStorage("Builder2 Storage")
                .build()
        
        then: "验证两个计算机是独立的且配置正确"
        computer1.getCpu() == "Builder1 CPU"
        computer1.getMemory() == "Builder1 Memory"
        computer1.getStorage() == null
        
        computer2.getCpu() == "Builder2 CPU"
        computer2.getMemory() == "Builder2 Memory"
        computer2.getStorage() == "Builder2 Storage"
    }
    
    def "带约束的建造者应该能够重置状态并构建新的产品"() {
        given: "创建带约束的建造者实例"
        ConstrainedComputerBuilder builder = new ConstrainedComputerBuilder()
        
        when: "第一次构建计算机"
        Computer firstComputer = builder
                .buildCPU("First CPU")
                .buildMemory("First Memory")
                .buildStorage("First Storage")
                .build()
        
        and: "重置建造者并构建第二个计算机"
        Computer secondComputer = builder
                .buildCPU("Second CPU")
                .buildMemory("Second Memory")
                .buildStorage("Second Storage")
                .build()
        
        then: "验证两个计算机是不同的实例且配置正确"
        firstComputer.getCpu() == "First CPU"
        firstComputer.getMemory() == "First Memory"
        firstComputer.getStorage() == "First Storage"
        
        secondComputer.getCpu() == "Second CPU"
        secondComputer.getMemory() == "Second Memory"
        secondComputer.getStorage() == "Second Storage"
    }
}