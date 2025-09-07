package com.example.memento

import spock.lang.Specification

/**
 * 文档编辑器备忘录模式测试类
 */
class DocumentEditorMementoSpec extends Specification {
    
    def "文档编辑器应该能够保存和恢复复杂文档状态"() {
        given:
        DocumentEditor document = new DocumentEditor("项目报告", "张三")
        Caretaker docSaves = new Caretaker()
        
        when:
        // 创建初始版本
        document.addChapter("引言")
        document.addChapter("需求分析")
        document.editContent("这是项目报告的内容...")
        docSaves.addMemento(document.createMemento("初稿完成"))
        
        // 继续编辑
        document.addChapter("设计方案")
        document.addChapter("实现细节")
        document.setFontSize(14)
        document.setBold(true)
        document.saveVersion("v1.0")
        docSaves.addMemento(document.createMemento("第二稿"))
        
        then:
        document.getChapters().size() == 4
        document.getFontSize() == 14
        document.isBold() == true
        document.getVersionHistory().size() == 1
        docSaves.getMementoCount() == 2
        
        when:
        // 恢复到初稿
        document.restoreMemento(docSaves.getMemento(0))
        
        then:
        document.getChapters().size() == 2
        document.getFontSize() == 12
        document.isBold() == false
        document.getContent() == "这是项目报告的内容..."
    }
    
    def "文档编辑器应该支持版本控制"() {
        given:
        DocumentEditor document = new DocumentEditor("技术文档", "李四")
        Caretaker versionControl = new Caretaker(10)
        
        when:
        // 创建多个版本
        document.editContent("# 技术文档\n\n## 简介\n这是技术文档...")
        document.saveVersion("initial")
        versionControl.addMemento(document.createMemento("版本1"))
        
        document.editContent("# 技术文档\n\n## 简介\n这是技术文档...\n\n## 安装\n安装步骤...")
        document.applyFormat("bold")
        document.saveVersion("with-install")
        versionControl.addMemento(document.createMemento("版本2"))
        
        document.addChapter("配置说明")
        document.addChapter("使用指南")
        document.setItalic(true)
        document.saveVersion("full-version")
        versionControl.addMemento(document.createMemento("版本3"))
        
        then:
        versionControl.getMementoCount() == 3
        versionControl.getMemento(0).getDescription() == "版本1"
        versionControl.getMemento(1).getDescription() == "版本2"
        versionControl.getMemento(2).getDescription() == "版本3"
        
        when:
        // 比较不同版本
        DocumentEditor version1 = new DocumentEditor()
        version1.restoreMemento(versionControl.getMemento(0))
        
        DocumentEditor version3 = new DocumentEditor()
        version3.restoreMemento(versionControl.getMemento(2))
        
        then:
        version1.getChapters().size() == 0
        version3.getChapters().size() == 2
        version1.isBold() == false
        version3.isItalic() == true
    }
    
    def "文档编辑器应该处理格式状态的备忘录"() {
        given:
        DocumentEditor document = new DocumentEditor("格式测试", "王五")
        Caretaker formatSaves = new Caretaker()
        
        when:
        // 应用多种格式
        document.setFontFamily("Times New Roman")
        document.setFontSize(16)
        document.setBold(true)
        document.setItalic(true)
        formatSaves.addMemento(document.createMemento("格式设置1"))
        
        // 更改格式
        document.setFontFamily("Arial")
        document.setFontSize(12)
        document.setBold(false)
        formatSaves.addMemento(document.createMemento("格式设置2"))
        
        then:
        document.getFontFamily() == "Arial"
        document.getFontSize() == 12
        document.isBold() == false
        document.isItalic() == true
        formatSaves.getMementoCount() == 2
        
        when:
        // 恢复到格式设置1
        document.restoreMemento(formatSaves.getMemento(0))
        
        then:
        document.getFontFamily() == "Times New Roman"
        document.getFontSize() == 16
        document.isBold() == true
        document.isItalic() == true
    }
    
    def "文档编辑器应该支持撤销复杂操作"() {
        given:
        DocumentEditor document = new DocumentEditor("撤销测试", "赵六")
        Caretaker undoStack = new Caretaker(20)
        
        when:
        // 执行一系列操作并保存检查点
        undoStack.addMemento(document.createMemento("初始状态"))
        
        document.editContent("第一段内容")
        document.addChapter("第一章")
        undoStack.addMemento(document.createMemento("添加第一章"))
        
        document.editContent("第一段内容\n第二段内容")
        document.addChapter("第二章")
        document.applyFormat("bold")
        undoStack.addMemento(document.createMemento("添加第二章并加粗"))
        
        document.setFontSize(18)
        document.setItalic(true)
        document.saveVersion("draft1")
        undoStack.addMemento(document.createMemento("草稿1完成"))
        
        then:
        document.getContent().contains("第二段内容")
        document.getChapters().size() == 2
        document.isBold() == true
        document.isItalic() == true
        document.getFontSize() == 18
        document.getVersionHistory().size() == 1
        undoStack.getMementoCount() == 4
        
        when:
        // 逐步撤销操作
        document.restoreMemento(undoStack.getMemento(2))
        
        then:
        document.isBold() == true
        document.isItalic() == false
        document.getFontSize() == 12
        
        when:
        document.restoreMemento(undoStack.getMemento(1))
        
        then:
        document.getChapters().size() == 1
        document.isBold() == false
        
        when:
        document.restoreMemento(undoStack.getMemento(0))
        
        then:
        document.getContent() == ""
        document.getChapters().size() == 0
    }
    
    def "文档编辑器应该正确处理元数据"() {
        given:
        DocumentEditor document = new DocumentEditor("元数据测试", "测试用户")
        Caretaker metadataSaves = new Caretaker()
        String initialTime = document.getLastModified()
        
        when:
        // 保存初始状态
        metadataSaves.addMemento(document.createMemento("初始"))
        
        // 等待一段时间后修改文档
        Thread.sleep(10) // 确保时间戳不同
        document.setTitle("新标题")
        document.editContent("新内容")
        metadataSaves.addMemento(document.createMemento("修改后"))
        
        then:
        document.getTitle() == "新标题"
        document.getAuthor() == "测试用户"
        document.getLastModified() != initialTime
        
        when:
        // 恢复到初始状态
        document.restoreMemento(metadataSaves.getMemento(0))
        
        then:
        document.getTitle() == "元数据测试"
        document.getAuthor() == "测试用户"
        // 注意：时间戳会在恢复时更新，这是正常行为
    }
}