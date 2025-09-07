package com.example.iterator

import spock.lang.Specification

/**
 * 树节点测试类
 */
class TreeNodeSpec extends Specification {
    
    def "树节点应该正确初始化"() {
        given:
        com.example.iterator.TreeNode<String> node = new com.example.iterator.TreeNode<>("根节点")
        
        expect:
        node.getData() == "根节点"
        node.getParent() == null
        node.getChildren().size() == 0
        node.isLeaf() == true
        node.isRoot() == true
        node.getDepth() == 0
    }
    
    def "树节点应该支持添加子节点"() {
        given:
        com.example.iterator.TreeNode<String> parent = new com.example.iterator.TreeNode<>("父节点")
        com.example.iterator.TreeNode<String> child = new com.example.iterator.TreeNode<>("子节点")
        
        when:
        parent.addChild(child)
        
        then:
        parent.getChildren().size() == 1
        parent.getChildren().get(0) == child
        child.getParent() == parent
        parent.isLeaf() == false
        child.isLeaf() == true
        child.isRoot() == false
    }
    
    def "树节点应该支持移除子节点"() {
        given:
        com.example.iterator.TreeNode<String> parent = new com.example.iterator.TreeNode<>("父节点")
        com.example.iterator.TreeNode<String> child = new com.example.iterator.TreeNode<>("子节点")
        parent.addChild(child)
        
        when:
        parent.removeChild(child)
        
        then:
        parent.getChildren().size() == 0
        child.getParent() == null
        parent.isLeaf() == true
        child.isLeaf() == true
        child.isRoot() == true
    }
    
    def "树节点应该正确计算深度"() {
        given:
        com.example.iterator.TreeNode<String> root = new com.example.iterator.TreeNode<>("根")
        com.example.iterator.TreeNode<String> level1 = new com.example.iterator.TreeNode<>("一级")
        com.example.iterator.TreeNode<String> level2 = new com.example.iterator.TreeNode<>("二级")
        com.example.iterator.TreeNode<String> level3 = new com.example.iterator.TreeNode<>("三级")
        
        root.addChild(level1)
        level1.addChild(level2)
        level2.addChild(level3)
        
        expect:
        root.getDepth() == 0
        level1.getDepth() == 1
        level2.getDepth() == 2
        level3.getDepth() == 3
    }
    
    def "树节点应该正确实现toString方法"() {
        given:
        com.example.iterator.TreeNode<String> node = new com.example.iterator.TreeNode<>("测试节点")
        com.example.iterator.TreeNode<String> child = new com.example.iterator.TreeNode<>("子节点")
        node.addChild(child)
        
        when:
        String str = node.toString()
        
        then:
        str.contains("测试节点")
        str.contains("children=1")
    }
}