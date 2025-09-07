# Design Pattern Learning Module

This module is dedicated to learning and practicing design patterns in Java.

## 项目结构

```
design-pattern/
├── docs/                        # 设计模式文档
│   ├── overview.md             # 23种设计模式概览
│   ├── singleton/              # 单例模式详细文档
│   │   └── singleton-pattern.md # 单例模式详解
│   ├── factorymethod/          # 工厂方法模式详细文档
│   │   └── factory-method-pattern.md # 工厂方法模式详解
│   ├── abstractfactory/       # 抽象工厂模式详细文档
│   │   └── abstract-factory-pattern.md # 抽象工厂模式详解
│   └── builder/               # 建造者模式详细文档
│       └── builder-pattern.md # 建造者模式详解
├── src/
│   ├── main/
│   │   └── java/
│   │       └── cn/geekslife/designpattern/
│   │           ├── creational/     # 创建型模式
│   │           ├── structural/     # 结构型模式
│   │           ├── behavioral/     # 行为型模式
│   │           ├── singleton/      # 单例模式实现
│   │           ├── factorymethod/  # 工厂方法模式实现
│   │           ├── abstractfactory/ # 抽象工厂模式实现
│   │           └── builder/       # 建造者模式实现
│   └── test/
│       ├── java/
│       │   └── cn/geekslife/designpattern/
│       │       └── singleton/      # 单例模式测试
│       └── groovy/
│           └── cn/geekslife/designpattern/
│               ├── factorymethod/  # 工厂方法模式测试
│               ├── abstractfactory/ # 抽象工厂模式测试
│               └── builder/       # 建造者模式测试
└── pom.xml
```

## 设计模式分类

### 创建型模式 (Creational Patterns)
- Singleton (单例模式)
- Factory Method (工厂方法模式)
- Abstract Factory (抽象工厂模式)
- Builder (建造者模式)
- Prototype (原型模式)

### 结构型模式 (Structural Patterns)
- Adapter (适配器模式)
- Bridge (桥接模式)
- Composite (组合模式)
- Decorator (装饰模式)
- Facade (外观模式)
- Flyweight (享元模式)
- Proxy (代理模式)

### 行为型模式 (Behavioral Patterns)
- Chain of Responsibility (责任链模式)
- Command (命令模式)
- Interpreter (解释器模式)
- Iterator (迭代器模式)
- Mediator (中介者模式)
- Memento (备忘录模式)
- Observer (观察者模式)
- State (状态模式)
- Strategy (策略模式)
- Template Method (模板方法模式)
- Visitor (访问者模式)

## 使用说明

1. 编译项目:
   ```bash
   mvn clean compile
   ```

2. 运行测试:
   ```bash
   mvn test
   ```

3. 运行主程序:
   ```bash
   mvn exec:java -pl design-pattern -Dexec.mainClass="cn.geekslife.designpattern.DesignPatternMain"
   ```

## 文档

- [23种设计模式概览](docs/overview.md) - 包含所有23种常用设计模式的详细介绍和使用场景
- [单例模式详解](docs/singleton/singleton-pattern.md) - 单例模式的详细实现、UML图、时序图和最佳实践
- [工厂方法模式详解](docs/factorymethod/factory-method-pattern.md) - 工厂方法模式的详细实现、UML图、时序图和最佳实践
- [抽象工厂模式详解](docs/abstractfactory/abstract-factory-pattern.md) - 抽象工厂模式的详细实现、UML图、时序图和最佳实践
- [建造者模式详解](docs/builder/builder-pattern.md) - 建造者模式的详细实现、UML图、时序图和最佳实践