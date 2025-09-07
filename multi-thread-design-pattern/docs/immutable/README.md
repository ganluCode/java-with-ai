# Immutable 模式（不可变模式）

## 1. 模式介绍

Immutable（不可变）模式是一种重要的并发设计模式，通过创建不可变对象来避免线程安全问题。不可变对象一旦创建后，其状态就不能被修改，这使得它们在多线程环境中天然线程安全。

### 1.1 定义
Immutable模式通过确保对象状态不可变来实现线程安全，避免了同步机制的复杂性。

### 1.2 应用场景
- 共享配置信息
- 值对象（Value Objects）
- 缓存键值
- 消息传递对象
- 数学计算中的常量

## 2. UML类图

```mermaid
classDiagram
    class ImmutablePerson {
        - final name: String
        - final age: int
        - final address: ImmutableAddress
        + ImmutablePerson(name: String, age: int, address: ImmutableAddress)
        + getName(): String
        + getAge(): int
        + getAddress(): ImmutableAddress
        + withName(name: String): ImmutablePerson
        + withAge(age: int): ImmutablePerson
    }
    
    class ImmutableAddress {
        - final street: String
        - final city: String
        - final zipCode: String
        + ImmutableAddress(street: String, city: String, zipCode: String)
        + getStreet(): String
        + getCity(): String
        + getZipCode(): String
        + withStreet(street: String): ImmutableAddress
    }
    
    class ImmutablePersonBuilder {
        - name: String
        - age: int
        - address: ImmutableAddress
        + setName(name: String): ImmutablePersonBuilder
        + setAge(age: int): ImmutablePersonBuilder
        + setAddress(address: ImmutableAddress): ImmutablePersonBuilder
        + build(): ImmutablePerson
    }
    
    ImmutablePerson --> ImmutableAddress
    ImmutablePerson ..> ImmutablePersonBuilder
```

## 3. 流程图

```mermaid
flowchart TD
    A[创建Immutable对象] --> B[设置初始状态]
    B --> C[对象状态固化]
    C --> D[对象变为不可变]
    D --> E[多线程安全访问]
    E --> F[读取对象状态]
    F --> G[返回相同数据]
    
    D --> H[需要修改状态]
    H --> I[创建新对象]
    I --> J[复制原对象状态]
    J --> K[应用变更]
    K --> L[返回新对象]
```

## 4. 时序图

```mermaid
sequenceDiagram
    participant T1 as Thread1
    participant T2 as Thread2
    participant ImmutableObj as ImmutableObject
    participant NewObj as NewImmutableObject
    
    T1->>ImmutableObj: 读取属性
    T2->>ImmutableObj: 读取属性
    ImmutableObj->>T1: 返回值
    ImmutableObj->>T2: 返回值
    
    T1->>ImmutableObj: 需要修改
    ImmutableObj->>NewObj: 复制并修改
    NewObj->>T1: 返回新对象
    
    T2->>ImmutableObj: 继续读取
    ImmutableObj->>T2: 返回原值
```

## 5. 状态图

```mermaid
stateDiagram-v2
    [*] --> Creating: 构造函数调用
    Creating --> Initializing: 设置属性
    Initializing --> Immutable: 构造完成
    Immutable --> [*]: 对象可用
    
    note right of Creating
        在构造过程中
        对象状态可变
    end note
    
    note right of Immutable
        对象状态不可变
        线程安全
    end note
```

## 6. 数据结构图

```mermaid
graph TD
    A[Immutable Object] --> B[Final Fields]
    A --> C[No Mutator Methods]
    A --> D[Defensive Copying]
    
    B --> B1[name: final String]
    B --> B2[age: final int]
    B --> B3[address: final Object]
    
    D --> D1[Constructor Parameters]
    D --> D2[Getter Return Values]
    D --> D3[Method Parameters]
    
    subgraph 内存布局
        E[Memory Block]
        E --> F[Header]
        E --> G[Final Fields]
        E --> H[No Mutable References]
    endgraph
```

## 7. 实现原则

### 7.1 不可变性规则
1. 类声明为final，防止被继承
2. 所有字段声明为final
3. 不提供任何修改对象状态的方法（setter）
4. 不允许this引用在构造过程中逸出
5. 对于可变对象字段，返回防御性拷贝

### 7.2 创建方式
1. 构造函数初始化
2. Builder模式
3. 工厂方法
4. 拷贝构造函数

## 8. 常见问题和解决方案

### 8.1 性能问题
创建大量对象可能导致内存和GC压力。

**解决方案：**
- 对象池化
- 享元模式
- 延迟初始化

### 8.2 可变组件问题
包含可变对象引用时，需要防御性拷贝。

**解决方案：**
- 构造函数中拷贝参数
- Getter方法中返回拷贝
- 使用不可变集合

### 8.3 循环引用问题
复杂的不可变对象图可能导致构造困难。

**解决方案：**
- 分步构建
- Builder模式
- 延迟初始化

## 9. 最佳实践

1. 尽可能使用不可变对象
2. 使用final关键字保护字段
3. 提供withXxx方法创建修改版本
4. 对可变组件进行防御性拷贝
5. 考虑使用不可变集合框架
6. 文档化不可变性保证