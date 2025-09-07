# Thread-Per-Message 模式（每消息一线程模式）

## 1. 模式介绍

Thread-Per-Message（每消息一线程）模式是一种并发设计模式，为每个请求或消息创建一个新线程来处理。这种模式可以提高系统的响应性，避免长时间运行的任务阻塞其他请求的处理。

### 1.1 定义
Thread-Per-Message模式通过为每个消息或请求创建独立的线程来实现并行处理，使得消息处理不会相互阻塞。

### 1.2 应用场景
- Web服务器请求处理
- GUI事件处理
- 网络通信服务器
- 邮件服务器
- 文件上传处理
- 异步任务处理

## 2. UML类图

```mermaid
classDiagram
    class ClientHandler {
        - Socket socket
        + handle() void
    }
    
    class ThreadPerMessageServer {
        - ServerSocket serverSocket
        - boolean running
        + start() void
        + stop() void
    }
    
    class MessageHandler {
        - Message message
        + run() void
    }
    
    class Message {
        - String content
        - long timestamp
        + process() void
    }
    
    class ThreadPoolHandler {
        - ExecutorService executor
        + handle(Message message) void
    }
    
    class AsyncService {
        - ExecutorService executor
        + processAsync(Runnable task) void
    }
    
    ThreadPerMessageServer --> ClientHandler
    ClientHandler --> MessageHandler
    MessageHandler --> Message
    ThreadPoolHandler --> Message
    AsyncService --> MessageHandler
```

## 3. 流程图

```mermaid
flowchart TD
    A[接收消息] --> B{创建线程?}
    B -->|是| C[创建新线程]
    B -->|使用线程池| D[从线程池获取线程]
    C --> E[在线程中处理消息]
    D --> E
    E --> F[执行业务逻辑]
    F --> G[处理完成]
    G --> H[线程结束/返回线程池]
    
    subgraph 线程创建方式
        B -->|直接创建| I[new Thread()]
        B -->|线程池| J[ExecutorService]
    endgraph
```

## 4. 时序图

```mermaid
sequenceDiagram
    participant Client1 as Client1
    participant Client2 as Client2
    participant Server as ThreadPerMessageServer
    participant Thread1 as MessageThread1
    participant Thread2 as MessageThread2
    participant Service as BusinessService
    
    Client1->>Server: 发送消息1
    Server->>Thread1: 创建新线程
    Thread1->>Service: 处理消息1
    Service->>Thread1: 返回结果
    
    Client2->>Server: 发送消息2
    Server->>Thread2: 创建新线程
    Thread2->>Service: 处理消息2
    Service->>Thread2: 返回结果
    
    note over Thread1,Thread2: 两个线程并行处理消息
    
    Thread1->>Server: 线程1完成
    Thread2->>Server: 线程2完成
```

## 5. 状态图

```mermaid
stateDiagram-v2
    [*] --> Idle: 服务器启动
    Idle --> Receiving: 接收消息
    Receiving --> CreatingThread: 创建处理线程
    CreatingThread --> Processing: 线程开始处理
    Processing --> BusinessLogic: 执行业务逻辑
    BusinessLogic --> Completing: 业务逻辑完成
    Completing --> Finished: 线程结束
    Finished --> Idle: 返回监听状态
    
    note right of CreatingThread
        可以使用以下方式创建线程：
        1. 直接创建Thread
        2. 使用线程池
        3. 使用ExecutorService
    end note
    
    note right of Processing
        每个消息在独立线程中处理
        不会阻塞其他消息的处理
    end note
```

## 6. 数据结构图

```mermaid
graph TD
    A[Thread-Per-Message架构] --> B[消息队列/网络接口]
    A --> C[线程管理器]
    A --> D[业务处理器]
    
    C --> E[Thread Factory]
    C --> F[Thread Pool]
    C --> G[Thread Lifecycle Manager]
    
    F --> H[Active Threads]
    F --> I[Idle Threads]
    F --> J[Thread Queue]
    
    subgraph 线程池结构
        ThreadPool[线程池] --> Active[活跃线程列表]
        ThreadPool --> Idle[空闲线程列表]
        ThreadPool --> Queue[线程队列]
    endgraph
    
    subgraph 消息处理流程
        MessageFlow[消息处理] --> CreateThread[创建线程]
        CreateThread --> AssignTask[分配任务]
        AssignTask --> ExecuteTask[执行任务]
        ExecuteTask --> ReturnThread[返回线程]
    endgraph
```

## 7. 实现方式

### 7.1 直接创建线程
- 为每个消息直接创建新线程
- 简单直接但资源消耗大

### 7.2 使用线程池
- 预先创建线程池
- 重用线程，提高性能

### 7.3 使用ExecutorService
- Java内置的线程池框架
- 提供更丰富的线程管理功能

## 8. 常见问题和解决方案

### 8.1 线程过多问题
为每个消息创建线程可能导致系统资源耗尽。

**解决方案：**
- 使用线程池限制并发线程数
- 实现消息队列进行缓冲
- 使用异步处理机制

### 8.2 资源竞争问题
多个线程同时访问共享资源可能导致竞争。

**解决方案：**
- 使用同步机制保护共享资源
- 减少共享状态
- 使用线程安全的数据结构

### 8.3 线程泄漏问题
线程未正确结束可能导致线程泄漏。

**解决方案：**
- 确保线程任务正确完成
- 使用try-finally确保资源释放
- 实现优雅的关闭机制

## 9. 与相关模式的区别

### 9.1 与Worker Thread模式
- Thread-Per-Message：为每个消息创建新线程
- Worker Thread：使用固定数量的工作线程处理任务队列

### 9.2 与Producer-Consumer模式
- Thread-Per-Message：关注消息的并行处理
- Producer-Consumer：关注生产者和消费者之间的解耦

### 9.3 与Future模式
- Thread-Per-Message：不关注处理结果的返回
- Future模式：关注异步计算结果的获取

## 10. 最佳实践

1. 评估消息处理的复杂度和频率
2. 选择合适的线程创建方式
3. 实现适当的线程池管理
4. 处理线程异常和中断
5. 监控线程使用情况
6. 提供优雅的关闭机制
7. 考虑使用异步编程模型
8. 避免长时间运行的线程任务