package cn.geekslife.designpattern.iterator

import cn.geekslife.designpattern.iterator.Employee
import spock.lang.Specification

/**
 * 员工类测试类
 */
class EmployeeSpec extends Specification {
    
    def "员工类应该正确初始化和访问属性"() {
        given:
        Employee employee = new Employee("张三", "技术部", 8000.0, 25)
        
        expect:
        employee.getName() == "张三"
        employee.getDepartment() == "技术部"
        employee.getSalary() == 8000.0
        employee.getAge() == 25
    }
    
    def "员工类应该支持属性修改"() {
        given:
        Employee employee = new Employee("张三", "技术部", 8000.0, 25)
        
        when:
        employee.setName("李四")
        employee.setDepartment("销售部")
        employee.setSalary(6000.0)
        employee.setAge(28)
        
        then:
        employee.getName() == "李四"
        employee.getDepartment() == "销售部"
        employee.getSalary() == 6000.0
        employee.getAge() == 28
    }
    
    def "员工类应该正确实现equals和hashCode方法"() {
        given:
        Employee employee1 = new Employee("张三", "技术部", 8000.0, 25)
        Employee employee2 = new Employee("张三", "技术部", 8000.0, 25)
        Employee employee3 = new Employee("李四", "销售部", 6000.0, 28)
        
        expect:
        employee1.equals(employee2)
        employee1.hashCode() == employee2.hashCode()
        !employee1.equals(employee3)
        employee1.hashCode() != employee3.hashCode()
        employee1.equals(employee1)  // 自反性
        !employee1.equals(null)      // 空值比较
    }
    
    def "员工类应该正确实现toString方法"() {
        given:
        Employee employee = new Employee("张三", "技术部", 8000.0, 25)
        
        when:
        String str = employee.toString()
        
        then:
        str.contains("张三")
        str.contains("技术部")
        str.contains("8000.0")
        str.contains("25")
    }
}