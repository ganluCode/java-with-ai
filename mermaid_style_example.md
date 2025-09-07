# Mermaid Flowchart 节点样式语法示例

## 基本语法

在Mermaid flowchart中，可以使用`style`命令来设置节点的样式属性：

```mermaid
graph TD
    A[节点A] --> B[节点B]
    C[节点C] --> D[节点D]
    
    style A fill:#ff0000,stroke:#333,stroke-width:2px,color:#ffffff
    style B fill:#00ff00,stroke:#333,stroke-width:2px,color:#000000
    style C fill:#0000ff,stroke:#333,stroke-width:2px,color:#ffffff
    style D fill:#ffff00,stroke:#333,stroke-width:2px,color:#000000
```

## 样式属性说明

- `fill`: 节点的填充颜色，使用十六进制颜色码
- `stroke`: 节点边框的颜色，使用十六进制颜色码
- `stroke-width`: 节点边框的宽度
- `color`: 节点内文字的颜色，使用十六进制颜色码

## 使用classDef定义样式类

也可以使用`classDef`定义样式类，然后应用到节点上：

```mermaid
graph TD
    A[节点A] --> B[节点B]
    C[节点C] --> D[节点D]
    
    classDef red fill:#ff0000,stroke:#333,stroke-width:2px,color:#ffffff
    classDef green fill:#00ff00,stroke:#333,stroke-width:2px,color:#000000
    classDef blue fill:#0000ff,stroke:#333,stroke-width:2px,color:#ffffff
    classDef yellow fill:#ffff00,stroke:#333,stroke-width:2px,color:#000000
    
    class A red
    class B green
    class C blue
    class D yellow
```

## 简单示例图表

下面是一个简单的示例图表，展示了不同样式的节点：

```mermaid
graph TD
    Start[开始] --> Process[处理数据]
    Process --> Decision{条件判断}
    Decision -->|满足条件| Action1[执行操作1]
    Decision -->|不满足| Action2[执行操作2]
    Action1 --> End[结束]
    Action2 --> End
    
    style Start fill:#4CAF50,stroke:#388E3C,stroke-width:2px,color:#ffffff
    style Process fill:#2196F3,stroke:#0D47A1,stroke-width:2px,color:#ffffff
    style Decision fill:#FFC107,stroke:#FF8F00,stroke-width:2px,color:#000000
    style Action1 fill:#9C27B0,stroke:#4A148C,stroke-width:2px,color:#ffffff
    style Action2 fill:#9C27B0,stroke:#4A148C,stroke-width:2px,color:#ffffff
    style End fill:#F44336,stroke:#B71C1C,stroke-width:2px,color:#ffffff
```