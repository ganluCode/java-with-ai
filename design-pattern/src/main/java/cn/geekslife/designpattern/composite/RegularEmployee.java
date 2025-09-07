package cn.geekslife.designpattern.composite;

/**
 * 普通员工 - 叶子节点
 */
public class RegularEmployee extends Employee {
    private double salary;
    
    public RegularEmployee(String name, String position, double salary) {
        super(name, position);
        this.salary = salary;
    }
    
    @Override
    public void showDetails(int depth) {
        System.out.println(getDepthString(depth) + "- 员工: " + name + " (" + position + ") - 薪资: $" + salary);
    }
    
    @Override
    public double getCost() {
        return salary;
    }
    
    @Override
    public int getEmployeeCount() {
        return 1;
    }
    
    // getter和setter方法
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}