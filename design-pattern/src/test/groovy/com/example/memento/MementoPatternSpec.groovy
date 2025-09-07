package com.example.memento

import spock.lang.Specification

/**
 * 备忘录模式基础功能测试类
 */
class MementoPatternSpec extends Specification {
    
    def "备忘录应该能够保存和恢复状态"() {
        given:
        TextEditor editor = new TextEditor("初始内容")
        Caretaker caretaker = new Caretaker()
        
        when:
        // 保存初始状态
        Memento initialMemento = editor.createMemento("初始状态")
        caretaker.addMemento(initialMemento)
        
        // 编辑内容
        editor.type(" 添加的内容")
        editor.moveCursor(5)
        
        // 保存编辑后的状态
        Memento editedMemento = editor.createMemento("编辑后状态")
        caretaker.addMemento(editedMemento)
        
        then:
        caretaker.getMementoCount() == 2
        caretaker.getMemento(0).getDescription() == "初始状态"
        caretaker.getMemento(1).getDescription() == "编辑后状态"
        
        when:
        // 恢复到初始状态
        editor.restoreMemento(caretaker.getMemento(0))
        
        then:
        editor.getContent() == "初始内容"
        editor.getCursorPosition() == 4 // 初始内容长度
    }
    
    def "负责人应该能够管理备忘录"() {
        given:
        Caretaker caretaker = new Caretaker(3)
        Memento memento1 = new Memento("state1", "描述1")
        Memento memento2 = new Memento("state2", "描述2")
        Memento memento3 = new Memento("state3", "描述3")
        Memento memento4 = new Memento("state4", "描述4")
        
        when:
        caretaker.addMemento(memento1)
        caretaker.addMemento(memento2)
        caretaker.addMemento(memento3)
        
        then:
        caretaker.getMementoCount() == 3
        caretaker.getMemento(0).getState() == "state1"
        caretaker.getMemento(1).getState() == "state2"
        caretaker.getMemento(2).getState() == "state3"
        
        when:
        // 添加第四个备忘录，应该移除最旧的
        caretaker.addMemento(memento4)
        
        then:
        caretaker.getMementoCount() == 3
        caretaker.getMemento(0).getState() == "state2" // 最旧的state1被移除
        caretaker.getMemento(1).getState() == "state3"
        caretaker.getMemento(2).getState() == "state4"
    }
    
    def "备忘录应该保存正确的状态信息"() {
        given:
        TextEditor editor = new TextEditor("测试内容")
        editor.moveCursor(2)
        
        when:
        Memento memento = editor.createMemento("测试状态")
        
        then:
        memento.getState().contains("测试内容")
        memento.getState().contains("|2") // 光标位置
        memento.getDescription() == "测试状态"
        memento.getTimestamp() > 0
    }
    
    def "备忘录模式应该支持多种原发器类型"() {
        given:
        TextEditor textEditor = new TextEditor("文本编辑器")
        GameCharacter character = new GameCharacter("游戏角色")
        Caretaker caretaker = new Caretaker()
        
        when:
        Memento textMemento = textEditor.createMemento("文本编辑器状态")
        Memento characterMemento = character.createMemento("游戏角色状态")
        caretaker.addMemento(textMemento)
        caretaker.addMemento(characterMemento)
        
        then:
        caretaker.getMementoCount() == 2
        caretaker.getMemento(0).getDescription() == "文本编辑器状态"
        caretaker.getMemento(1).getDescription() == "游戏角色状态"
    }
    
    def "负责人应该提供备忘录管理功能"() {
        given:
        Caretaker caretaker = new Caretaker()
        Memento memento1 = new Memento("state1", "描述1")
        Memento memento2 = new Memento("state2", "描述2")
        caretaker.addMemento(memento1)
        caretaker.addMemento(memento2)
        
        when:
        Memento latest = caretaker.getLatestMemento()
        List<Memento> all = caretaker.getAllMementos()
        Memento removed = caretaker.removeMemento(0)
        
        then:
        latest == memento2
        all.size() == 2
        all.get(0) == memento1
        all.get(1) == memento2
        removed == memento1
        caretaker.getMementoCount() == 1
    }
    
    def "备忘录模式应该保护封装性"() {
        given:
        TextEditor editor = new TextEditor("测试内容")
        editor.moveCursor(3)
        
        when:
        Memento memento = editor.createMemento("封装测试")
        
        then:
        // 备忘录的状态应该被封装，外部不能直接修改
        memento.getState() != null
        memento.getDescription() != null
        memento.getTimestamp() > 0
        
        when:
        // 尝试通过备忘录恢复状态
        editor.restoreMemento(memento)
        
        then:
        // 状态应该被正确恢复
        editor.getContent() == "测试内容"
        editor.getCursorPosition() == 3
    }
}