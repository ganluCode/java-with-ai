package cn.geekslife.designpattern.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工抽象类 - 组件接口
 */
public abstract class Employee {
    protected String name;
    protected String position;
    
    public Employee(String name, String position) {
        this.name = name;
        this.position = position;
    }
    
    public abstract void showDetails(int depth);
    public abstract double getCost();
    public abstract int getEmployeeCount();
    
    // 默认实现
    public void add(Employee employee) {
        throw new UnsupportedOperationException("不支持此操作");
    }
    
    public void remove(Employee employee) {
        throw new UnsupportedOperationException("不支持此操作");
    }
    
    public Employee getChild(int i) {
        throw new UnsupportedOperationException("不支持此操作");
    }
    
    protected String getDepthString(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }
    
    // getter方法
    public String getName() { return name; }
    public String getPosition() { return position; }
}