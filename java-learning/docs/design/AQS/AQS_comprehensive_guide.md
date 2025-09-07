# AbstractQueuedSynchronizer (AQS) 全面解析指南

## 1. 概述

AbstractQueuedSynchronizer（简称AQS）是Java并发包中的核心框架类，为实现依赖于先进先出（FIFO）等待队列的阻塞锁和相关同步器（信号量、事件等）提供了一个框架。

此类的设计目标是为大多数依赖单个原子int值来表示状态的同步器提供基础。子类通过实现保护方法来改变这个状态的含义，以及定义哪些状态变化会使得线程可以获取或释放同步器。AQS使用CLH队列锁的一个变体来管理线程排队，并采用了模板方法设计模式。

## 2. 核心设计思想

### 2.1 CLH队列锁变体
AQS使用CLH（Craig, Landin, and Hagersten）队列锁的一个变体来管理线程排队。CLH锁通常用于自旋锁，而AQS将其改为阻塞同步器，线程在等待时不会消耗CPU。

### 2.2 模板方法模式
AQS采用了模板方法设计模式，定义了同步器的骨架方法，而将具体的状态控制逻辑交给子类实现。

### 2.3 状态管理
AQS使用一个volatile int类型的state字段来表示同步状态，通过CAS操作保证状态变更的原子性。

## 3. 核心组件

### 3.1 Node节点类
Node是AQS中的内部类，用于构建同步队列和条件队列的节点。

节点状态说明：
- **SIGNAL(-1)**：后继节点正在等待，当前节点释放或取消时必须唤醒后继节点
- **CANCELLED(1)**：当前节点已取消，由于超时或中断
- **CONDITION(-2)**：当前节点在条件队列中等待
- **PROPAGATE(-3)**：共享模式下的释放传播
- **0**：初始状态或以上状态均不适用

### 3.2 同步队列
使用双向链表组织等待线程，支持高效的入队和出队操作。

### 3.3 数据结构
Node是AQS中的内部类，用于构建同步队列和条件队列的节点。

## 4. 核心方法

### 4.1 acquire(int arg)
获取独占锁的核心方法，包含获取失败时的入队逻辑。

### 4.2 release(int arg)
释放独占锁的核心方法，包含唤醒后继节点的逻辑。

### 4.3 acquireShared(int arg)
获取共享锁的核心方法。

### 4.4 releaseShared(int arg)
释放共享锁的核心方法。

## 5. 核心机制详解

### 5.1 独占模式获取锁机制

独占模式获取锁的详细流程图：

```mermaid
graph TD
    A[acquire] --> B{tryAcquire成功?}
    B -->|是| C[获取成功,返回]
    B -->|否| D[addWaiter创建节点]
    D --> E[enq入队操作]
    E --> F[acquireQueued循环处理]
    F --> G{当前节点是head的后继?}
    G -->|是| H{tryAcquire成功?}
    H -->|是| I[设置为head节点,返回]
    H -->|否| J[shouldParkAfterFailedAcquire检查]
    J --> K{前驱节点状态为SIGNAL?}
    K -->|是| L[parkAndCheckInterrupt阻塞线程]
    K -->|否| M{前驱节点状态为CANCELLED?}
    M -->|是| N[跳过取消节点,重新链接]
    M -->|否| O[设置前驱节点为SIGNAL]
    N --> F
    O --> F
    L --> P{线程被中断?}
    P -->|是| Q[标记中断状态,返回true]
    P -->|否| F
    I --> R[结束]
    Q --> R
```

主要步骤：
1. 调用tryAcquire尝试直接获取锁
2. 如果失败，创建Node节点并加入等待队列
3. 通过自旋和park/unpark机制实现线程阻塞和唤醒
4. 当被唤醒后重新尝试获取锁

### 5.2 独占模式释放锁机制

独占模式释放锁的详细流程图：

```mermaid
graph TD
    A[release] --> B{tryRelease成功?}
    B -->|是| C{head不为null?}
    B -->|否| D[返回false]
    C -->|是| E{head.waitStatus != 0?}
    C -->|否| F[返回true]
    E -->|是| G[unparkSuccessor唤醒后继节点]
    E -->|否| F
    G --> H[获取后继节点]
    H --> I{后继节点存在且未取消?}
    I -->|是| J[LockSupport.unpark唤醒线程]
    I -->|否| K[从尾部向前查找未取消节点]
    K --> L{找到有效节点?}
    L -->|是| J
    L -->|否| M[直接返回]
    J --> F
    M --> F
```

主要步骤：
1. 调用tryRelease释放锁
2. 如果释放成功且有等待线程，则调用unparkSuccessor唤醒后继节点
3. 后继节点被唤醒后重新尝试获取锁

### 5.3 共享模式获取锁机制

共享模式获取锁的流程图：

```mermaid
graph TD
    A[acquireShared] --> B{tryAcquireShared返回值 >= 0?}
    B -->|是| C[获取成功,返回]
    B -->|否| D[doAcquireShared处理]
    D --> E[addWaiter创建共享节点]
    E --> F[enq入队操作]
    F --> G[doAcquireShared循环处理]
    G --> H{当前节点是head的后继?}
    H -->|是| I{tryAcquireShared返回值 >= 0?}
    I -->|是| J[设置为head节点]
    I -->|否| K[shouldParkAfterFailedAcquire检查]
    J --> L{需要传播?}
    L -->|是| M[doReleaseShared传播]
    L -->|否| N[结束]
    M --> N
    K --> O{前驱节点状态为SIGNAL?}
    O -->|是| P[parkAndCheckInterrupt阻塞线程]
    O -->|否| Q{前驱节点状态为CANCELLED?}
    Q -->|是| R[跳过取消节点,重新链接]
    Q -->|否| S[设置前驱节点为SIGNAL]
    R --> G
    S --> G
    P --> T{线程被中断?}
    T -->|是| U[标记中断状态,返回true]
    T -->|否| G
    U --> N
```

主要步骤：
1. 调用tryAcquireShared尝试获取共享锁
2. 如果失败，创建共享模式Node节点并加入等待队列
3. 支持锁的传播机制，允许多个线程同时获取共享锁

### 5.4 共享模式释放锁机制

共享模式释放锁的流程图：

```mermaid
graph TD
    A[releaseShared] --> B{tryReleaseShared成功?}
    B -->|是| C[doReleaseShared传播]
    B -->|否| D[返回false]
    C --> E{head不为null且不等于tail?}
    E -->|是| F{head.waitStatus < 0?}
    E -->|否| G[返回true]
    F -->|是| H[unparkSuccessor唤醒后继节点]
    F -->|否| I[获取下一个节点]
    H --> J[继续循环检查]
    I --> K{下一个节点存在?}
    K -->|是| L{下一个节点waitStatus < 0?}
    K -->|否| E
    L -->|是| H
    L -->|否| I
    J --> E
    G --> M[结束]
```

主要步骤：
1. 调用tryReleaseShared释放共享锁
2. 如果释放成功，调用doReleaseShared进行传播
3. 唤醒多个等待线程以支持共享访问

### 5.5 条件队列机制

条件队列的await和signal流程图：

#### 条件等待(await)流程图

```mermaid
graph TD
    A[await] --> B{当前线程持有锁?}
    B -->|否| C[抛出IllegalMonitorStateException]
    B -->|是| D[添加到条件队列]
    D --> E[释放锁]
    E --> F[park阻塞线程]
    F --> G{被唤醒或中断?}
    G -->|是| H[检查中断模式]
    H --> I{中断发生在signal前?}
    I -->|是| J[抛出InterruptedException]
    I -->|否| K[重新中断]
    K --> L[重新获取锁]
    J --> M[结束]
    L --> M
```

#### 条件唤醒(signal)流程图

```mermaid
graph TD
    A[signal] --> B{当前线程持有锁?}
    B -->|否| C[抛出IllegalMonitorStateException]
    B -->|是| D[获取第一个等待节点]
    D --> E{节点存在?}
    E -->|否| F[返回]
    E -->|是| G[从条件队列转移到同步队列]
    G --> H[设置节点状态为0]
    H --> I{前驱节点状态为CANCELLED?}
    I -->|是| J[跳过取消节点]
    I -->|否| K{前驱节点状态 >= 0?}
    K -->|是| L[CAS设置前驱节点为SIGNAL]
    K -->|否| M[唤醒节点线程]
    J --> K
    L --> M
    M --> F
```

主要步骤：
1. 调用await将线程加入条件队列并释放锁
2. 线程在条件队列中等待直到被signal唤醒
3. 调用signal将线程从条件队列转移到同步队列
4. 被唤醒的线程重新竞争获取锁

### 5.6 Node节点入队和出队机制

Node节点的入队和出队过程：

#### 入队过程(enq)流程图

```mermaid
graph TD
    A[enq入队] --> B{tail不为null?}
    B -->|是| C[CAS设置tail.next为新节点]
    B -->|否| D[初始化空队列]
    C --> E{CAS成功?}
    E -->|是| F[更新tail指向新节点]
    E -->|否| B
    D --> G[创建哨兵节点作为head]
    G --> H[设置tail为head]
    H --> B
    F --> I[返回前驱节点]
```

#### 出队过程(取消节点)流程图

```mermaid
graph TD
    A[取消节点处理] --> B{节点状态设置为CANCELLED}
    B --> C{节点是head?}
    C -->|是| D[unparkSuccessor唤醒后继]
    C -->|否| E{节点有前驱?}
    E -->|是| F{节点有后继?}
    E -->|否| G[结束]
    F -->|是| H[重新链接前驱和后继]
    F -->|否| I[更新tail]
    H --> J[CAS更新前驱的next指针]
    J --> K{CAS成功?}
    K -->|是| L[更新后继的prev指针]
    K -->|否| E
    L --> G
    I --> G
    G --> M[结束]
```

主要特点：
1. 使用CAS操作保证入队的原子性
2. 采用自旋方式重试入队操作
3. 支持节点取消和清理机制

### 5.7 中断处理机制

AQS提供多种中断处理方式：
1. acquireInterruptibly：响应中断并抛出InterruptedException
2. acquire：不响应中断，但记录中断状态
3. 支持线程在等待过程中被中断的处理

### 5.8 超时机制

AQS提供超时获取锁的支持：
1. tryAcquireNanos：支持超时的独占模式获取
2. tryAcquireSharedNanos：支持超时的共享模式获取
3. 使用parkNanos实现带超时的阻塞等待

## 6. 设计模式应用

### 6.1 模板方法模式
AQS定义了同步器的骨架方法，而将具体的状态控制逻辑交给子类实现。

需要子类实现的方法：
- tryAcquire(int arg) - 独占式获取同步状态
- tryRelease(int arg) - 独占式释放同步状态
- tryAcquireShared(int arg) - 共享式获取同步状态
- tryReleaseShared(int arg) - 共享式释放同步状态
- isHeldExclusively() - 当前同步器是否在独占模式下被线程占用

### 6.2 观察者模式
通过ConditionObject内部类实现观察者模式，支持线程的条件等待和通知。

### 6.3 CAS无锁编程模式
大量使用CAS操作实现无锁化的并发控制，提高性能。

## 7. 最佳实践

1. 合理设计state字段的含义
2. 使用CAS操作保证状态变更的原子性
3. 尽量减少线程阻塞和唤醒的次数
4. 在try方法中避免进行耗时操作
5. 正确处理中断和超时情况
6. 确保异常情况下状态的一致性

## 8. 总结

AQS作为Java并发包的核心框架，通过精巧的设计实现了高效的同步机制。其核心思想是使用CLH队列变体管理等待线程，结合CAS操作实现无锁化状态管理，并通过模板方法模式将具体实现交给子类。理解AQS的工作原理对于深入掌握Java并发编程具有重要意义。