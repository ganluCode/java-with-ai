package cn.geekslife.designpattern.memento

import cn.geekslife.designpattern.memento.Caretaker
import cn.geekslife.designpattern.memento.TextEditor
import spock.lang.Specification

/**
 * 文本编辑器备忘录模式测试类
 */
class TextEditorMementoSpec extends Specification {
    
    def "文本编辑器应该能够保存和恢复复杂状态"() {
        given:
        TextEditor editor = new TextEditor("Hello World")
        Caretaker caretaker = new Caretaker()
        
        when:
        // 保存初始状态
        caretaker.addMemento(editor.createMemento("初始状态"))
        
        // 执行一系列编辑操作
        editor.type("!")
        editor.moveCursor(5)
        editor.type(" Beautiful")
        editor.moveCursor(editor.getContent().length())
        editor.type(" from Spock")
        
        // 保存编辑后的状态
        caretaker.addMemento(editor.createMemento("编辑后状态"))
        
        then:
        editor.getContent() == "Hello Beautiful World! from Spock"
        editor.getCursorPosition() == editor.getContent().length()
        caretaker.getMementoCount() == 2
        
        when:
        // 恢复到初始状态
        editor.restoreMemento(caretaker.getMemento(0))
        
        then:
        editor.getContent() == "Hello World"
        editor.getCursorPosition() == 11 // "Hello World"的长度
    }
    
    def "文本编辑器应该支持撤销操作"() {
        given:
        TextEditor editor = new TextEditor("初始")
        Caretaker caretaker = new Caretaker()
        
        when:
        // 保存检查点
        caretaker.addMemento(editor.createMemento("检查点1"))
        
        // 执行操作
        editor.type(" 内容1")
        caretaker.addMemento(editor.createMemento("检查点2"))
        
        editor.type(" 内容2")
        caretaker.addMemento(editor.createMemento("检查点3"))
        
        editor.delete(3) // 删除"内容2"
        caretaker.addMemento(editor.createMemento("检查点4"))
        
        then:
        editor.getContent() == "初始 内容1 "
        caretaker.getMementoCount() == 4
        
        when:
        // 撤销到检查点2
        editor.restoreMemento(caretaker.getMemento(1))
        
        then:
        editor.getContent() == "初始 内容1"
        editor.getCursorPosition() == editor.getContent().length()
    }
    
    def "文本编辑器应该支持替换操作的备忘录"() {
        given:
        TextEditor editor = new TextEditor("Hello World")
        Caretaker caretaker = new Caretaker()
        
        when:
        caretaker.addMemento(editor.createMemento("替换前"))
        editor.replace("World", "Spock")
        caretaker.addMemento(editor.createMemento("替换后"))
        
        then:
        editor.getContent() == "Hello Spock"
        caretaker.getMementoCount() == 2
        
        when:
        // 恢复到替换前
        editor.restoreMemento(caretaker.getMemento(0))
        
        then:
        editor.getContent() == "Hello World"
    }
    
    def "文本编辑器应该记录操作历史"() {
        given:
        TextEditor editor = new TextEditor()
        
        when:
        editor.type("初始")
        editor.type(" 添加")
        editor.delete(2)
        editor.moveCursor(0)
        editor.type("前")
        editor.clear()
        
        then:
        editor.getHistory().size() >= 6
        editor.getHistory().contains("输入: 初始")
        editor.getHistory().contains("输入:  添加")
        editor.getHistory().contains("删除: 添加")
        editor.getHistory().contains("光标移动到位置: 0")
        editor.getHistory().contains("输入: 前")
        editor.getHistory().contains("清空内容")
    }
    
    def "文本编辑器应该处理边界情况"() {
        given:
        TextEditor editor = new TextEditor()
        Caretaker caretaker = new Caretaker()
        
        when:
        // 空内容的备忘录
        caretaker.addMemento(editor.createMemento("初始空内容"))
        editor.type("测试")
        caretaker.addMemento(editor.createMemento("添加内容后"))
        editor.clear() // 清空内容
        caretaker.addMemento(editor.createMemento("清空后"))
        
        then:
        caretaker.getMementoCount() == 3
        caretaker.getMemento(0).getState().equals("|0") // 空内容+光标位置0
        !caretaker.getMemento(1).getState().equals("|0")
        
        when:
        // 恢复到清空后的状态
        editor.restoreMemento(caretaker.getMemento(2))
        
        then:
        editor.getContent() == ""
        editor.getCursorPosition() == 0
    }
    
    def "文本编辑器应该正确处理光标位置"() {
        given:
        TextEditor editor = new TextEditor("0123456789")
        Caretaker caretaker = new Caretaker()
        
        when:
        editor.moveCursor(5)
        caretaker.addMemento(editor.createMemento("光标在中间"))
        
        editor.moveCursor(0)
        caretaker.addMemento(editor.createMemento("光标在开始"))
        
        editor.moveCursor(10)
        caretaker.addMemento(editor.createMemento("光标在结束"))
        
        then:
        caretaker.getMementoCount() == 3
        
        when:
        // 恢复到光标在中间
        editor.restoreMemento(caretaker.getMemento(0))
        
        then:
        editor.getCursorPosition() == 5
        
        when:
        // 恢复到光标在开始
        editor.restoreMemento(caretaker.getMemento(1))
        
        then:
        editor.getCursorPosition() == 0
        
        when:
        // 恢复到光标在结束
        editor.restoreMemento(caretaker.getMemento(2))
        
        then:
        editor.getCursorPosition() == 10
    }
}