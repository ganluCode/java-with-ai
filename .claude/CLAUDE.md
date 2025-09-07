# CLAUDE.md

此文件为Claude Code (claude.ai/code)在处理此代码库时提供指导。

## 项目概述
这是一个带有AI辅助的Java学习项目。它使用Maven作为构建系统，采用多模块结构。

## 仓库结构
- 根模块: java-with-ai (父POM)
- 子模块: java-learning (主要学习模块)
- 包结构: cn.geekslife
- 标准Maven目录布局，包含src/main/java和src/test/java
- 文档目录结构：每个子模块下包含docs/design和docs/test目录

## 开发命令

### 构建和运行
- 编译项目: `mvn compile`
- 运行主应用程序: `mvn exec:java -Dexec.mainClass="cn.geekslife.App"`
- 打包应用程序: `mvn package`

### 测试
- 运行所有测试: `mvn test`
- 运行特定测试类: `mvn -Dtest=AppTest test`
- 静默模式运行测试: `mvn test -q`

### 其他有用的Maven命令
- 清理构建产物: `mvn clean`
- 清理并编译: `mvn clean compile`
- 安装到本地仓库: `mvn install`

## 代码架构
- 主应用程序类: cn.geekslife包中的App.java
- 测试类: cn.geekslife包中的AppTest.java (位于src/test/java下)
- Java版本: 17 (在父pom.xml中配置)
- 测试框架: JUnit Jupiter 5.8.1

## 开发工作流程
1. 修改src/main/java中的Java文件
2. 根据需要在src/test/java中添加/更新测试
3. 使用`mvn compile`编译
4. 使用`mvn test`运行测试
5. 准备就绪后使用`mvn package`打包