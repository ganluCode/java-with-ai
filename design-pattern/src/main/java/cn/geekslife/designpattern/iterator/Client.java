package cn.geekslife.designpattern.iterator;

import java.util.function.Predicate;

/**
 * 客户端类
 * 演示迭代器模式的使用
 */
public class Client {
    public static void main(String[] args) {
        System.out.println("=== 迭代器模式演示 ===");
        
        // 演示基本迭代器
        System.out.println("\n--- 基本迭代器演示 ---");
        demonstrateBasicIterator();
        
        // 演示双向迭代器
        System.out.println("\n--- 双向迭代器演示 ---");
        demonstrateBidirectionalIterator();
        
        // 演示跳过迭代器
        System.out.println("\n--- 跳过迭代器演示 ---");
        demonstrateSkipIterator();
        
        // 演示过滤迭代器
        System.out.println("\n--- 过滤迭代器演示 ---");
        demonstrateFilterIterator();
        
        // 演示树形迭代器
        System.out.println("\n--- 树形迭代器演示 ---");
        demonstrateTreeIterator();
        
        // 演示多种聚合对象的统一遍历
        System.out.println("\n--- 多种聚合对象统一遍历演示 ---");
        demonstrateUnifiedTraversal();
    }
    
    /**
     * 演示基本迭代器
     */
    private static void demonstrateBasicIterator() {
        // 创建员工聚合对象
        ConcreteAggregate<Employee> employeeAggregate = new ConcreteAggregate<>();
        employeeAggregate.add(new Employee("张三", "技术部", 8000, 25));
        employeeAggregate.add(new Employee("李四", "销售部", 6000, 28));
        employeeAggregate.add(new Employee("王五", "技术部", 9000, 30));
        employeeAggregate.add(new Employee("赵六", "人事部", 5500, 26));
        
        // 创建迭代器并遍历
        Iterator<Employee> iterator = employeeAggregate.createIterator();
        
        System.out.println("员工列表:");
        iterator.first();
        while (iterator.hasNext()) {
            Employee employee = iterator.currentItem();
            System.out.println("  " + employee);
            iterator.next();
        }
        
        // 反向遍历演示
        System.out.println("\n使用相同接口遍历不同类型的聚合:");
        ConcreteAggregate<Product> productAggregate = new ConcreteAggregate<>();
        productAggregate.add(new Product("笔记本电脑", "电子产品", 5999, 10));
        productAggregate.add(new Product("手机", "电子产品", 3999, 20));
        productAggregate.add(new Product("书籍", "图书", 59, 50));
        
        Iterator<Product> productIterator = productAggregate.createIterator();
        productIterator.first();
        while (productIterator.hasNext()) {
            Product product = productIterator.currentItem();
            System.out.println("  " + product);
            productIterator.next();
        }
    }
    
    /**
     * 演示双向迭代器
     */
    private static void demonstrateBidirectionalIterator() {
        ConcreteAggregate<String> stringAggregate = new ConcreteAggregate<>();
        stringAggregate.add("第一个");
        stringAggregate.add("第二个");
        stringAggregate.add("第三个");
        stringAggregate.add("第四个");
        stringAggregate.add("第五个");
        
        ConcreteBidirectionalIterator<String> biIterator = new ConcreteBidirectionalIterator<>(stringAggregate);
        
        // 正向遍历
        System.out.println("正向遍历:");
        biIterator.first();
        while (biIterator.hasNext()) {
            String item = biIterator.currentItem();
            System.out.println("  " + item + (biIterator.isFirst() ? " (第一个)" : "") + 
                             (biIterator.isLast() ? " (最后一个)" : ""));
            biIterator.next();
        }
        
        // 反向遍历
        System.out.println("\n反向遍历:");
        biIterator.last();
        while (biIterator.hasPrevious()) {
            String item = biIterator.currentItem();
            System.out.println("  " + item);
            biIterator.previous();
        }
        
        // 再次正向遍历
        System.out.println("\n再次正向遍历:");
        biIterator.first();
        if (biIterator.hasNext()) {
            System.out.println("第一个元素: " + biIterator.currentItem());
            biIterator.next();
        }
        if (biIterator.hasNext()) {
            System.out.println("第二个元素: " + biIterator.currentItem());
        }
    }
    
    /**
     * 演示跳过迭代器
     */
    private static void demonstrateSkipIterator() {
        ConcreteAggregate<Integer> numberAggregate = new ConcreteAggregate<>();
        for (int i = 1; i <= 20; i++) {
            numberAggregate.add(i);
        }
        
        // 每隔一个元素遍历
        System.out.println("每隔一个元素遍历 (步长=2):");
        ConcreteSkipIterator<Integer> skipIterator = new ConcreteSkipIterator<>(numberAggregate, 2);
        skipIterator.first();
        while (skipIterator.hasNext()) {
            Integer number = skipIterator.currentItem();
            System.out.print(number + " ");
            skipIterator.next();
        }
        System.out.println();
        
        // 每隔两个元素遍历
        System.out.println("\n每隔两个元素遍历 (步长=3):");
        ConcreteSkipIterator<Integer> skipIterator2 = new ConcreteSkipIterator<>(numberAggregate, 3);
        skipIterator2.first();
        while (skipIterator2.hasNext()) {
            Integer number = skipIterator2.currentItem();
            System.out.print(number + " ");
            skipIterator2.next();
        }
        System.out.println();
        
        // 手动跳过元素
        System.out.println("\n手动跳过元素:");
        ConcreteSkipIterator<Integer> skipIterator3 = new ConcreteSkipIterator<>(numberAggregate);
        skipIterator3.first();
        System.out.println("第一个元素: " + skipIterator3.currentItem());
        skipIterator3.skip(5); // 跳过5个元素
        if (skipIterator3.hasNext()) {
            System.out.println("跳过5个元素后: " + skipIterator3.currentItem());
        }
    }
    
    /**
     * 演示过滤迭代器
     */
    private static void demonstrateFilterIterator() {
        ConcreteAggregate<Employee> employeeAggregate = new ConcreteAggregate<>();
        employeeAggregate.add(new Employee("张三", "技术部", 8000, 25));
        employeeAggregate.add(new Employee("李四", "销售部", 6000, 28));
        employeeAggregate.add(new Employee("王五", "技术部", 9000, 30));
        employeeAggregate.add(new Employee("赵六", "人事部", 5500, 26));
        employeeAggregate.add(new Employee("钱七", "技术部", 7500, 27));
        employeeAggregate.add(new Employee("孙八", "销售部", 6500, 29));
        
        // 过滤技术部员工
        System.out.println("技术部员工:");
        Predicate<Employee> techFilter = emp -> "技术部".equals(emp.getDepartment());
        ConcreteFilterIterator<Employee> techIterator = new ConcreteFilterIterator<>(employeeAggregate, techFilter);
        techIterator.first();
        while (techIterator.hasNext()) {
            Employee employee = techIterator.currentItem();
            System.out.println("  " + employee);
            techIterator.next();
        }
        
        // 过滤高薪员工（薪资大于7000）
        System.out.println("\n高薪员工 (薪资>7000):");
        Predicate<Employee> highSalaryFilter = emp -> emp.getSalary() > 7000;
        ConcreteFilterIterator<Employee> salaryIterator = new ConcreteFilterIterator<>(employeeAggregate, highSalaryFilter);
        salaryIterator.first();
        while (salaryIterator.hasNext()) {
            Employee employee = salaryIterator.currentItem();
            System.out.println("  " + employee);
            salaryIterator.next();
        }
        
        // 过滤年轻员工（年龄小于28）
        System.out.println("\n年轻员工 (年龄<28):");
        Predicate<Employee> youngFilter = emp -> emp.getAge() < 28;
        ConcreteFilterIterator<Employee> youngIterator = new ConcreteFilterIterator<>(employeeAggregate, youngFilter);
        youngIterator.first();
        while (youngIterator.hasNext()) {
            Employee employee = youngIterator.currentItem();
            System.out.println("  " + employee);
            youngIterator.next();
        }
    }
    
    /**
     * 演示树形迭代器
     */
    private static void demonstrateTreeIterator() {
        // 创建树形结构
        TreeNode<String> root = new TreeNode<>("CEO");
        TreeNode<String> techDept = new TreeNode<>("技术部", root);
        TreeNode<String> salesDept = new TreeNode<>("销售部", root);
        TreeNode<String> hrDept = new TreeNode<>("人事部", root);
        
        root.addChild(techDept);
        root.addChild(salesDept);
        root.addChild(hrDept);
        
        TreeNode<String> devTeam = new TreeNode<>("开发组", techDept);
        TreeNode<String> qaTeam = new TreeNode<>("测试组", techDept);
        techDept.addChild(devTeam);
        techDept.addChild(qaTeam);
        
        TreeNode<String> northSales = new TreeNode<>("北方销售", salesDept);
        TreeNode<String> southSales = new TreeNode<>("南方销售", salesDept);
        salesDept.addChild(northSales);
        salesDept.addChild(southSales);
        
        // 简单的树形遍历演示
        System.out.println("组织架构树:");
        printTree(root, 0);
    }
    
    /**
     * 打印树形结构
     * @param node 节点
     * @param depth 深度
     */
    private static void printTree(TreeNode<String> node, int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }
        System.out.println("- " + node.getData());
        
        for (TreeNode<String> child : node.getChildren()) {
            printTree(child, depth + 1);
        }
    }
    
    /**
     * 演示多种聚合对象的统一遍历
     */
    private static void demonstrateUnifiedTraversal() {
        // 创建不同的聚合对象
        ConcreteAggregate<String> stringAggregate = new ConcreteAggregate<>();
        stringAggregate.add("苹果");
        stringAggregate.add("香蕉");
        stringAggregate.add("橙子");
        
        ConcreteAggregate<Integer> integerAggregate = new ConcreteAggregate<>();
        integerAggregate.add(1);
        integerAggregate.add(2);
        integerAggregate.add(3);
        integerAggregate.add(4);
        integerAggregate.add(5);
        
        // 使用统一接口遍历不同的聚合对象
        System.out.println("字符串聚合:");
        traverseAggregate(stringAggregate);
        
        System.out.println("\n整数聚合:");
        traverseAggregate(integerAggregate);
    }
    
    /**
     * 遍历聚合对象
     * @param aggregate 聚合对象
     * @param <T> 元素类型
     */
    private static <T> void traverseAggregate(Aggregate<T> aggregate) {
        Iterator<T> iterator = aggregate.createIterator();
        iterator.first();
        while (iterator.hasNext()) {
            T item = iterator.currentItem();
            System.out.println("  " + item);
            iterator.next();
        }
    }
}