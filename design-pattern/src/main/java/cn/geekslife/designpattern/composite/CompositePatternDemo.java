package cn.geekslife.designpattern.composite;

/**
 * 组合模式演示类
 */
public class CompositePatternDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 组合模式演示 ===\n");
        
        // 1. 文件系统演示
        demonstrateFileSystem();
        
        // 2. 图形绘制系统演示
        demonstrateGraphicsSystem();
        
        // 3. 菜单系统演示
        demonstrateMenuSystem();
        
        // 4. 公司组织架构演示
        demonstrateOrganization();
    }
    
    /**
     * 演示文件系统
     */
    private static void demonstrateFileSystem() {
        System.out.println("1. 文件系统演示:");
        
        // 创建文件夹和文件
        Folder root = new Folder("根目录");
        Folder documents = new Folder("文档");
        Folder images = new Folder("图片");
        Folder music = new Folder("音乐");
        
        File doc1 = new File("简历.docx", 1024);
        File doc2 = new File("报告.pdf", 2048);
        File image1 = new File("照片1.jpg", 5120);
        File image2 = new File("照片2.png", 3072);
        File song1 = new File("歌曲1.mp3", 8192);
        File song2 = new File("歌曲2.wav", 16384);
        
        // 构建文件系统结构
        root.add(documents);
        root.add(images);
        root.add(music);
        
        documents.add(doc1);
        documents.add(doc2);
        
        images.add(image1);
        images.add(image2);
        
        music.add(song1);
        music.add(song2);
        
        // 显示文件系统结构
        root.display(0);
        
        System.out.println("根目录总大小: " + root.getSize() + " 字节");
        System.out.println();
    }
    
    /**
     * 演示图形绘制系统
     */
    private static void demonstrateGraphicsSystem() {
        System.out.println("2. 图形绘制系统演示:");
        
        // 创建基本图形
        Dot dot1 = new Dot("点1", 10, 20);
        Dot dot2 = new Dot("点2", 30, 40);
        Circle circle1 = new Circle("圆1", 50, 60, 10);
        Circle circle2 = new Circle("圆2", 70, 80, 15);
        
        // 创建组合图形
        CompoundGraphic compound1 = new CompoundGraphic("组合图形1");
        compound1.add(dot1);
        compound1.add(circle1);
        
        CompoundGraphic compound2 = new CompoundGraphic("组合图形2");
        compound2.add(dot2);
        compound2.add(circle2);
        
        // 创建更大的组合图形
        CompoundGraphic mainCompound = new CompoundGraphic("主组合图形");
        mainCompound.add(compound1);
        mainCompound.add(compound2);
        
        // 绘制图形
        mainCompound.draw();
        
        System.out.println("总面积: " + mainCompound.getArea());
        System.out.println();
    }
    
    /**
     * 演示菜单系统
     */
    private static void demonstrateMenuSystem() {
        System.out.println("3. 菜单系统演示:");
        
        // 创建菜单项
        MenuItem pancakeHouseMenu = new MenuItem("煎饼屋菜单", "煎饼屋的菜单项", true, 2.99);
        MenuItem dinerMenu = new MenuItem("餐厅菜单", "餐厅的菜单项", false, 2.99);
        MenuItem cafeMenu = new MenuItem("咖啡厅菜单", "咖啡厅的菜单项", true, 3.99);
        MenuItem dessertMenu = new MenuItem("甜品菜单", "甜品的菜单项", true, 1.99);
        
        // 创建菜单
        Menu allMenus = new Menu("所有菜单", "所有餐厅的菜单");
        Menu mainMenu = new Menu("主菜单", "主菜单项");
        
        // 构建菜单结构
        allMenus.add(mainMenu);
        mainMenu.add(pancakeHouseMenu);
        mainMenu.add(dinerMenu);
        mainMenu.add(cafeMenu);
        mainMenu.add(dessertMenu);
        
        // 显示菜单
        allMenus.print();
        System.out.println();
    }
    
    /**
     * 演示公司组织架构
     */
    private static void demonstrateOrganization() {
        System.out.println("4. 公司组织架构演示:");
        
        // 创建CEO
        Manager ceo = new Manager("张三", "CEO", 100000);
        
        // 创建部门经理
        Manager techManager = new Manager("李四", "技术部经理", 80000);
        Manager salesManager = new Manager("王五", "销售部经理", 75000);
        Manager hrManager = new Manager("赵六", "人事部经理", 70000);
        
        // 创建普通员工
        RegularEmployee dev1 = new RegularEmployee("小明", "Java开发工程师", 60000);
        RegularEmployee dev2 = new RegularEmployee("小红", "前端开发工程师", 55000);
        RegularEmployee tester = new RegularEmployee("小刚", "测试工程师", 50000);
        
        RegularEmployee sales1 = new RegularEmployee("小丽", "销售代表", 45000);
        RegularEmployee sales2 = new RegularEmployee("小强", "销售代表", 45000);
        
        RegularEmployee hr = new RegularEmployee("小美", "人事专员", 40000);
        
        // 构建组织架构
        // CEO管理三个部门经理
        ceo.add(techManager);
        ceo.add(salesManager);
        ceo.add(hrManager);
        
        // 技术部经理管理开发人员和测试人员
        techManager.add(dev1);
        techManager.add(dev2);
        techManager.add(tester);
        
        // 销售部经理管理销售代表
        salesManager.add(sales1);
        salesManager.add(sales2);
        
        // 人事部经理管理人事专员
        hrManager.add(hr);
        
        // 显示组织架构
        System.out.println("公司组织架构:");
        ceo.showDetails(0);
        
        System.out.println("\n公司总成本: $" + ceo.getCost());
        System.out.println("公司总员工数: " + ceo.getEmployeeCount() + " 人");
        
        // 查看技术部信息
        System.out.println("\n技术部信息:");
        techManager.showDetails(1);
        System.out.println("技术部成本: $" + techManager.getCost());
        System.out.println("技术部员工数: " + techManager.getEmployeeCount() + " 人");
        System.out.println();
    }
}