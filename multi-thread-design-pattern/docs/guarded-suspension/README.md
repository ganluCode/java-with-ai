# Guarded Suspension 模式（保护性暂停模式）

## 1. 模式介绍

Guarded Suspension（保护性暂停）模式是一种重要的并发设计模式，用于处理线程间协调问题。当线程请求某个条件不满足时，该模式会让线程等待直到条件满足，而不是直接失败或抛出异常。

### 1.1 定义
Guarded Suspension模式通过在条件不满足时挂起线程，并在条件满足时唤醒线程，来实现线程间的协调和同步。

### 1.2 应用场景
- 生产者-消费者问题
- 线程间状态协调
- 条件等待和通知
- 异步任务处理
- 资源池管理

## 2. UML类图

```mermaid
classDiagram
    class GuardedObject {
        - Object lock
        - Object data
        + get() Object
        + put(Object data) void
    }
    
    class Producer {
        - GuardedObject guardedObject
        + run() void
    }
    
    class Consumer {
        - GuardedObject guardedObject
        + run() void
    }
    
    class Request {
        - String name
        + Request(String name)
        + getName() String
        + toString() String
    }
    
    class RequestQueue {
        - Queue<Request> queue
        - Object lock
        + getRequest() Request
        + putRequest(Request request) void
    }
    
    Producer --> GuardedObject
    Consumer --> GuardedObject
    Producer --> Request
    Consumer --> Request
    Producer --> RequestQueue
    Consumer --> RequestQueue
```

## 3. 流程图

```mermaid
flowchart TD
    A[线程请求执行] --> B{条件满足?}
    B -->|是| C[执行操作]
    B -->|否| D[进入等待状态]
    D --> E[释放锁]
    E --> F[加入等待队列]
    F --> G[等待通知]
    G --> H{被唤醒?}
    H -->|否| G
    H -->|是| I[重新获取锁]
    I --> J[重新检查条件]
    J --> B
    C --> K[操作完成]
```

## 4. 时序图

```mermaid
sequenceDiagram
    participant T1 as Producer Thread
    participant Queue as RequestQueue
    participant T2 as Consumer Thread
    
    T1->>Queue: putRequest(request)
    Queue->>Queue: 添加请求到队列
    Queue->>T2: notify()唤醒消费者
    T1->>T1: 继续执行
    
    T2->>Queue: getRequest()
    Queue->>Queue: 检查队列是否为空
    Queue->>T2: 返回请求
    
    note over T1,T2: 如果队列为空时消费者请求数据
    
    T2->>Queue: getRequest()
    Queue->>Queue: 队列为空
    Queue->>T2: wait()进入等待
    T2->>T2: 释放锁并等待
    
    T1->>Queue: putRequest(request)
    Queue->>Queue: 添加请求到队列
    Queue->>T2: notify()唤醒等待的消费者
    T2->>Queue: 重新获取锁
    Queue->>T2: 返回请求
```

## 5. 状态图

```mermaid
stateDiagram-v2
    [*] --> Running: 线程开始执行
    Running --> Checking: 检查条件
    Checking --> Executing: 条件满足
    Checking --> Waiting: 条件不满足
    Waiting --> Notified: 收到通知
    Notified --> Checking: 重新检查条件
    Executing --> Completed: 执行完成
    Completed --> [*]: 线程结束
    
    note right of Waiting
        线程在等待队列中
        释放了对象锁
        等待其他线程的通知
    end note
```

## 6. 数据结构图

```mermaid
graph TD
    A[Monitor/Object] --> B[Entry Set]
    A --> C[Wait Set]
    A --> D[Owner Thread]
    
    B --> B1[Blocked Thread 1]
    B --> B2[Blocked Thread 2]
    
    C --> C1[Waiting Thread 1]
    C --> C2[Waiting Thread 2]
    C --> C3[Waiting Thread 3]
    
    subgraph 等待机制
        WaitSet[Wait Set] --> WaitThread1[Thread A - wait()]
        WaitSet --> WaitThread2[Thread B - wait()]
        WaitSet --> WaitThread3[Thread C - wait()]
    endgraph
    
    subgraph 通知机制
        Notify[notify()/notifyAll()] --> WakeUp[唤醒等待线程]
        WakeUp --> MoveToEntry[移动到Entry Set]
    endgraph
```

## 7. 实现方式

### 7.1 使用wait/notify机制
- Object.wait()：使当前线程等待
- Object.notify()：唤醒一个等待线程
- Object.notifyAll()：唤醒所有等待线程

### 7.2 使用Condition接口
- ReentrantLock配合Condition
- 更灵活的等待和通知机制
- 支持多个条件队列

### 7.3 使用BlockingQueue
- 内置的阻塞队列实现
- 简化生产者-消费者模式实现

## 8. 常见问题和解决方案

### 8.1 虚假唤醒问题
线程可能在没有收到通知的情况下被唤醒。

**解决方案：**
- 总是在循环中检查条件
- 使用while循环而不是if语句

### 8.2 死锁问题
线程间相互等待导致死锁。

**解决方案：**
- 确保锁的获取顺序一致
- 使用超时机制
- 避免嵌套锁

### 8.3 性能问题
频繁的等待和唤醒影响性能。

**解决方案：**
- 减少锁的竞争
- 使用更高效的并发数据结构
- 合理设置等待超时

### 8.4 通知丢失问题
通知可能在等待之前发送，导致线程永远等待。

**解决方案：**
- 使用状态标志位
- 确保先等待后通知的顺序
- 使用超时机制

## 9. 最佳实践

1. 总是在循环中检查条件
2. 确保在同步块中调用wait/notify
3. 使用notifyAll()而不是notify()（除非确定只需要唤醒一个线程）
4. 避免在持有锁时执行长时间操作
5. 考虑使用java.util.concurrent包中的高级同步工具
6. 合理处理InterruptedException
7. 注意锁的粒度和范围