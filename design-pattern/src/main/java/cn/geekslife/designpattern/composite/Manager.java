package cn.geekslife.designpattern.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理者 - 容器节点
 */
public class Manager extends Employee {
    private double salary;
    private List<Employee> subordinates = new ArrayList<>();
    
    public Manager(String name, String position, double salary) {
        super(name, position);
        this.salary = salary;
    }
    
    @Override
    public void add(Employee employee) {
        subordinates.add(employee);
    }
    
    @Override
    public void remove(Employee employee) {
        subordinates.remove(employee);
    }
    
    @Override
    public Employee getChild(int i) {
        return subordinates.get(i);
    }
    
    @Override
    public void showDetails(int depth) {
        System.out.println(getDepthString(depth) + "+ 管理者: " + name + " (" + position + ") - 薪资: $" + salary);
        for (Employee employee : subordinates) {
            employee.showDetails(depth + 1);
        }
    }
    
    @Override
    public double getCost() {
        double totalCost = salary;
        for (Employee employee : subordinates) {
            totalCost += employee.getCost();
        }
        return totalCost;
    }
    
    @Override
    public int getEmployeeCount() {
        int count = 1; // 包括管理者自己
        for (Employee employee : subordinates) {
            count += employee.getEmployeeCount();
        }
        return count;
    }
    
    // getter和setter方法
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public List<Employee> getSubordinates() { return new ArrayList<>(subordinates); }
}