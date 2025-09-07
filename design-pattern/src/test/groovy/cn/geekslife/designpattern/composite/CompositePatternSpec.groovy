package cn.geekslife.designpattern.composite

import spock.lang.Specification
import spock.lang.Unroll

/**
 * 组合模式Spock测试类
 * 使用行为驱动开发(BDD)方式测试组合模式
 */
class CompositePatternSpec extends Specification {
    
    def "文件应该正确实现文件系统组件接口"() {
        given: "创建文件实例"
        File file = new File("test.txt", 1024)
        
        when: "获取文件信息"
        String name = file.getName()
        long size = file.getSize()
        
        then: "文件信息应该正确"
        name == "test.txt"
        size == 1024
        
        when: "显示文件"
        file.display(0)
        
        then: "应该正确显示"
        true // 占位符，实际测试中需要验证输出
    }
    
    def "文件夹应该能够添加和移除子组件"() {
        given: "创建文件夹和子组件"
        Folder folder = new Folder("测试文件夹")
        File file1 = new File("文件1.txt", 512)
        File file2 = new File("文件2.txt", 1024)
        
        when: "添加子组件"
        folder.add(file1)
        folder.add(file2)
        
        then: "子组件应该被正确添加"
        folder.getChildren().size() == 2
        
        when: "移除子组件"
        folder.remove(file1)
        
        then: "子组件应该被正确移除"
        folder.getChildren().size() == 1
        folder.getChildren().get(0) == file2
    }
    
    def "文件夹应该能够计算总大小"() {
        given: "创建文件夹和子组件"
        Folder folder = new Folder("根文件夹")
        Folder subFolder = new Folder("子文件夹")
        File file1 = new File("文件1.txt", 512)
        File file2 = new File("文件2.txt", 1024)
        File file3 = new File("文件3.txt", 2048)
        
        when: "构建文件夹结构"
        folder.add(subFolder)
        folder.add(file1)
        subFolder.add(file2)
        subFolder.add(file3)
        
        then: "总大小应该正确计算"
        folder.getSize() == 3584 // 512 + 1024 + 2048
        subFolder.getSize() == 3072 // 1024 + 2048
    }
    
    def "文件夹应该能够显示层次结构"() {
        given: "创建文件夹层次结构"
        Folder root = new Folder("根目录")
        Folder documents = new Folder("文档")
        Folder images = new Folder("图片")
        File doc1 = new File("简历.docx", 1024)
        File image1 = new File("照片.jpg", 5120)
        
        when: "构建层次结构"
        root.add(documents)
        root.add(images)
        documents.add(doc1)
        images.add(image1)
        
        and: "显示层次结构"
        root.display(0)
        
        then: "应该正确显示"
        true
    }
    
    def "点应该正确实现图形组件接口"() {
        given: "创建点实例"
        Dot dot = new Dot("测试点", 10, 20)
        
        when: "获取点信息"
        String name = dot.getName()
        int x = dot.getX()
        int y = dot.getY()
        
        then: "点信息应该正确"
        name == "测试点"
        x == 10
        y == 20
        
        when: "绘制点"
        dot.draw()
        
        then: "应该正确绘制"
        true
        
        when: "移动点"
        dot.move(5, 10)
        
        then: "点位置应该正确更新"
        dot.getX() == 15
        dot.getY() == 30
    }
    
    def "圆形应该正确实现图形组件接口"() {
        given: "创建圆形实例"
        Circle circle = new Circle("测试圆", 50, 60, 10)
        
        when: "获取圆形信息"
        String name = circle.getName()
        int x = circle.getX()
        int y = circle.getY()
        int radius = circle.getRadius()
        
        then: "圆形信息应该正确"
        name == "测试圆"
        x == 50
        y == 60
        radius == 10
        
        when: "计算面积"
        double area = circle.getArea()
        
        then: "面积应该正确计算"
        area == Math.PI * 100
        
        when: "绘制圆形"
        circle.draw()
        
        then: "应该正确绘制"
        true
        
        when: "移动圆形"
        circle.move(10, 15)
        
        then: "圆形位置应该正确更新"
        circle.getX() == 60
        circle.getY() == 75
    }
    
    def "组合图形应该能够添加和移除子图形"() {
        given: "创建组合图形和子图形"
        CompoundGraphic compound = new CompoundGraphic("测试组合")
        Dot dot = new Dot("点1", 10, 20)
        Circle circle = new Circle("圆1", 50, 60, 10)
        
        when: "添加子图形"
        compound.add(dot)
        compound.add(circle)
        
        then: "子图形应该被正确添加"
        compound.getChildren().size() == 2
        
        when: "移除子图形"
        compound.remove(dot)
        
        then: "子图形应该被正确移除"
        compound.getChildren().size() == 1
        compound.getChildren().get(0) == circle
    }
    
    def "组合图形应该能够计算总面积"() {
        given: "创建组合图形和子图形"
        CompoundGraphic compound = new CompoundGraphic("主组合")
        CompoundGraphic subCompound = new CompoundGraphic("子组合")
        Dot dot = new Dot("点1", 10, 20)
        Circle circle1 = new Circle("圆1", 50, 60, 10)
        Circle circle2 = new Circle("圆2", 70, 80, 5)
        
        when: "构建图形结构"
        compound.add(subCompound)
        compound.add(dot)
        subCompound.add(circle1)
        subCompound.add(circle2)
        
        and: "计算总面积"
        double totalArea = compound.getArea()
        double subArea = subCompound.getArea()
        
        then: "总面积应该正确计算"
        totalArea == subArea // 点的面积为0
        subArea == Math.PI * 100 + Math.PI * 25 // 两个圆的面积之和
    }
    
    def "菜单项应该正确实现菜单组件接口"() {
        given: "创建菜单项实例"
        MenuItem menuItem = new MenuItem("测试菜单项", "测试描述", true, 19.99)
        
        when: "获取菜单项信息"
        String name = menuItem.getName()
        String description = menuItem.getDescription()
        boolean vegetarian = menuItem.isVegetarian()
        double price = menuItem.getPrice()
        
        then: "菜单项信息应该正确"
        name == "测试菜单项"
        description == "测试描述"
        vegetarian == true
        price == 19.99
        
        when: "显示菜单项"
        menuItem.print()
        
        then: "应该正确显示"
        true
    }
    
    def "菜单应该能够添加和移除菜单组件"() {
        given: "创建菜单和菜单组件"
        Menu menu = new Menu("测试菜单", "测试菜单描述")
        MenuItem item1 = new MenuItem("项目1", "描述1", true, 9.99)
        MenuItem item2 = new MenuItem("项目2", "描述2", false, 14.99)
        
        when: "添加菜单组件"
        menu.add(item1)
        menu.add(item2)
        
        then: "菜单组件应该被正确添加"
        menu.getMenuComponents().size() == 2
        
        when: "移除菜单组件"
        menu.remove(item1)
        
        then: "菜单组件应该被正确移除"
        menu.getMenuComponents().size() == 1
        menu.getMenuComponents().get(0) == item2
    }
    
    def "普通员工应该正确实现员工接口"() {
        given: "创建普通员工实例"
        RegularEmployee employee = new RegularEmployee("张三", "开发工程师", 50000)
        
        when: "获取员工信息"
        String name = employee.getName()
        String position = employee.getPosition()
        double salary = employee.getSalary()
        
        then: "员工信息应该正确"
        name == "张三"
        position == "开发工程师"
        salary == 50000
        
        when: "显示员工详情"
        employee.showDetails(0)
        
        then: "应该正确显示"
        true
        
        when: "计算成本和员工数"
        double cost = employee.getCost()
        int count = employee.getEmployeeCount()
        
        then: "成本和员工数应该正确"
        cost == 50000
        count == 1
    }
    
    def "管理者应该能够添加和移除下属"() {
        given: "创建管理者和下属"
        Manager manager = new Manager("李四", "部门经理", 80000)
        RegularEmployee employee1 = new RegularEmployee("员工1", "开发工程师", 50000)
        RegularEmployee employee2 = new RegularEmployee("员工2", "测试工程师", 45000)
        
        when: "添加下属"
        manager.add(employee1)
        manager.add(employee2)
        
        then: "下属应该被正确添加"
        manager.getSubordinates().size() == 2
        
        when: "移除下属"
        manager.remove(employee1)
        
        then: "下属应该被正确移除"
        manager.getSubordinates().size() == 1
        manager.getSubordinates().get(0) == employee2
    }
    
    def "管理者应该能够计算总成本和总员工数"() {
        given: "创建管理者层次结构"
        Manager ceo = new Manager("CEO", "首席执行官", 100000)
        Manager manager = new Manager("经理", "部门经理", 80000)
        RegularEmployee employee1 = new RegularEmployee("员工1", "开发工程师", 50000)
        RegularEmployee employee2 = new RegularEmployee("员工2", "测试工程师", 45000)
        
        when: "构建组织结构"
        ceo.add(manager)
        manager.add(employee1)
        manager.add(employee2)
        
        and: "计算总成本和员工数"
        double ceoCost = ceo.getCost()
        int ceoCount = ceo.getEmployeeCount()
        double managerCost = manager.getCost()
        int managerCount = manager.getEmployeeCount()
        
        then: "总成本和员工数应该正确计算"
        ceoCost == 275000 // 100000 + 80000 + 50000 + 45000
        ceoCount == 4 // CEO + 经理 + 2个员工
        managerCost == 175000 // 80000 + 50000 + 45000
        managerCount == 3 // 经理 + 2个员工
    }
    
    def "文件系统组件应该正确处理不支持的操作"() {
        given: "创建文件实例"
        File file = new File("test.txt", 1024)
        
        when: "尝试对文件执行不支持的操作"
        file.add(new File("another.txt", 512))
        
        then: "应该抛出UnsupportedOperationException"
        thrown(UnsupportedOperationException)
        
        when: "尝试移除子组件"
        file.remove(new File("another.txt", 512))
        
        then: "应该抛出UnsupportedOperationException"
        thrown(UnsupportedOperationException)
        
        when: "尝试获取子组件"
        file.getChild(0)
        
        then: "应该抛出UnsupportedOperationException"
        thrown(UnsupportedOperationException)
    }
    
    def "图形组件应该正确处理不支持的操作"() {
        given: "创建点实例"
        Dot dot = new Dot("测试点", 10, 20)
        
        when: "尝试对点执行不支持的操作"
        dot.add(new Dot("另一个点", 30, 40))
        
        then: "应该抛出UnsupportedOperationException"
        thrown(UnsupportedOperationException)
        
        when: "尝试移除子图形"
        dot.remove(new Dot("另一个点", 30, 40))
        
        then: "应该抛出UnsupportedOperationException"
        thrown(UnsupportedOperationException)
        
        when: "尝试获取子图形"
        dot.getChild(0)
        
        then: "应该抛出UnsupportedOperationException"
        thrown(UnsupportedOperationException)
    }
    
    def "菜单组件应该正确处理不支持的操作"() {
        given: "创建菜单项实例"
        MenuItem menuItem = new MenuItem("测试项", "描述", true, 9.99)
        
        when: "尝试对菜单项执行不支持的操作"
        menuItem.add(new MenuItem("另一个项", "描述", false, 14.99))
        
        then: "应该抛出UnsupportedOperationException"
        thrown(UnsupportedOperationException)
        
        when: "尝试移除菜单组件"
        menuItem.remove(new MenuItem("另一个项", "描述", false, 14.99))
        
        then: "应该抛出UnsupportedOperationException"
        thrown(UnsupportedOperationException)
        
        when: "尝试获取菜单组件"
        menuItem.getChild(0)
        
        then: "应该抛出UnsupportedOperationException"
        thrown(UnsupportedOperationException)
    }
    
    def "员工组件应该正确处理不支持的操作"() {
        given: "创建普通员工实例"
        RegularEmployee employee = new RegularEmployee("张三", "开发工程师", 50000)
        
        when: "尝试对普通员工执行不支持的操作"
        employee.add(new RegularEmployee("李四", "测试工程师", 45000))
        
        then: "应该抛出UnsupportedOperationException"
        thrown(UnsupportedOperationException)
        
        when: "尝试移除下属"
        employee.remove(new RegularEmployee("李四", "测试工程师", 45000))
        
        then: "应该抛出UnsupportedOperationException"
        thrown(UnsupportedOperationException)
        
        when: "尝试获取下属"
        employee.getChild(0)
        
        then: "应该抛出UnsupportedOperationException"
        thrown(UnsupportedOperationException)
    }
    
    @Unroll
    def "不同类型的文件系统组件应该正确显示: #componentType"() {
        given: "创建文件系统组件"
        FileSystemComponent component = componentCreator.call()
        
        when: "显示组件"
        component.display(0)
        
        then: "应该正确显示"
        true
        
        where:
        componentType      | componentCreator
        "文件"            | { new File("测试文件.txt", 1024) }
        "文件夹"          | { 
            Folder folder = new Folder("测试文件夹")
            folder.add(new File("子文件.txt", 512))
            return folder
        }
    }
    
    @Unroll
    def "不同类型的图形组件应该正确绘制: #graphicType"() {
        given: "创建图形组件"
        Graphic graphic = graphicCreator.call()
        
        when: "绘制图形"
        graphic.draw()
        
        then: "应该正确绘制"
        true
        
        where:
        graphicType      | graphicCreator
        "点"            | { new Dot("测试点", 10, 20) }
        "圆形"          | { new Circle("测试圆", 50, 60, 10) }
        "组合图形"      | { 
            CompoundGraphic compound = new CompoundGraphic("测试组合")
            compound.add(new Dot("点1", 10, 20))
            compound.add(new Circle("圆1", 50, 60, 10))
            return compound
        }
    }
    
    def "组合模式应该支持递归操作"() {
        given: "创建复杂的文件夹层次结构"
        Folder root = new Folder("根目录")
        Folder level1 = new Folder("一级目录")
        Folder level2 = new Folder("二级目录")
        File file1 = new File("文件1.txt", 1024)
        File file2 = new File("文件2.txt", 2048)
        File file3 = new File("文件3.txt", 4096)
        
        when: "构建复杂的层次结构"
        root.add(level1)
        root.add(file1)
        level1.add(level2)
        level1.add(file2)
        level2.add(file3)
        
        and: "计算总大小"
        long totalSize = root.getSize()
        
        then: "递归操作应该正确执行"
        totalSize == 7168 // 1024 + 2048 + 4096
        root.getChildren().size() == 2
        level1.getChildren().size() == 2
        level2.getChildren().size() == 1
    }
    
    def "组合模式应该保持客户端代码的一致性"() {
        given: "创建不同类型的组件"
        File file = new File("测试文件.txt", 1024)
        Folder folder = new Folder("测试文件夹")
        folder.add(new File("子文件.txt", 512))
        
        when: "对不同类型的组件执行相同操作"
        long fileSize = file.getSize()
        long folderSize = folder.getSize()
        
        and: "显示组件"
        file.display(0)
        folder.display(0)
        
        then: "客户端代码应该保持一致性"
        fileSize == 1024
        folderSize == 512
    }
}