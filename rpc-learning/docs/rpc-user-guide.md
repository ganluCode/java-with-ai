# 自定义RPC框架使用文档

## 1. 简介

本RPC框架是一个基于Spring Boot的轻量级分布式服务框架，支持服务注册与发现、负载均衡、集群容错等功能。框架采用Nacos作为注册中心，基于Netty实现高性能网络通信。

## 2. 快速开始

### 2.1 环境要求

- JDK 8+
- Maven 3.6+
- Nacos 1.4+

### 2.2 添加依赖

在项目的pom.xml中添加以下依赖：

```xml
<dependency>
    <groupId>cn.geekslife</groupId>
    <artifactId>rpc-learning</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- Nacos依赖 -->
<dependency>
    <groupId>com.alibaba.nacos</groupId>
    <artifactId>nacos-client</artifactId>
    <version>1.4.2</version>
</dependency>
```

### 2.3 启动Nacos

请确保Nacos服务已经启动并运行在默认端口(8848)。

## 3. 服务提供者

### 3.1 定义服务接口

首先定义服务接口：

```java
public interface HelloService {
    String sayHello(String name);
}
```

### 3.2 实现服务接口

实现服务接口并使用@RpcService注解：

```java
import cn.geekslife.rpc.annotation.RpcService;

@RpcService
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
}
```

### 3.3 配置服务提供者

在application.yml中配置：

```yaml
server:
  port: 8080

rpc:
  protocol: rpc
  host: localhost
  port: 20880
```

### 3.4 启动服务提供者

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
```

## 4. 服务消费者

### 4.1 引用远程服务

在需要调用远程服务的类中使用@RpcReference注解：

```java
import cn.geekslife.rpc.annotation.RpcReference;
import org.springframework.stereotype.Service;

@Service
public class HelloController {
    
    @RpcReference
    private HelloService helloService;
    
    public String hello(String name) {
        return helloService.sayHello(name);
    }
}
```

### 4.2 配置服务消费者

在application.yml中配置：

```yaml
server:
  port: 8081

rpc:
  registry:
    address: nacos://localhost:8848
```

### 4.3 启动服务消费者

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
```

## 5. 配置说明

### 5.1 服务提供者配置

```yaml
rpc:
  protocol: rpc  # 协议类型
  host: localhost  # 服务绑定IP
  port: 20880  # 服务绑定端口
  registry:
    address: nacos://localhost:8848  # 注册中心地址
```

### 5.2 服务消费者配置

```yaml
rpc:
  registry:
    address: nacos://localhost:8848  # 注册中心地址
```

### 5.3 高级配置

#### 5.3.1 负载均衡配置

```java
@RpcReference(loadbalance = "random", retries = 3)
private HelloService helloService;
```

#### 5.3.2 服务分组和版本

```java
@RpcService(version = "1.0.0", group = "test")
public class HelloServiceImpl implements HelloService {
    // ...
}

@RpcReference(version = "1.0.0", group = "test")
private HelloService helloService;
```

## 6. 使用示例

### 6.1 完整的服务提供者示例

```java
// 服务接口
public interface UserService {
    User getUserById(Long id);
    List<User> listUsers();
}

// 服务实现
@RpcService(version = "1.0.0", group = "user")
public class UserServiceImpl implements UserService {
    
    @Override
    public User getUserById(Long id) {
        // 模拟数据库查询
        User user = new User();
        user.setId(id);
        user.setName("User" + id);
        return user;
    }
    
    @Override
    public List<User> listUsers() {
        List<User> users = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            User user = new User();
            user.setId(i);
            user.setName("User" + i);
            users.add(user);
        }
        return users;
    }
}

// 启动类
@SpringBootApplication
public class UserProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserProviderApplication.class, args);
    }
}
```

### 6.2 完整的服务消费者示例

```java
// 控制器
@RestController
@RequestMapping("/user")
public class UserController {
    
    @RpcReference(version = "1.0.0", group = "user")
    private UserService userService;
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    
    @GetMapping("/")
    public List<User> listUsers() {
        return userService.listUsers();
    }
}

// 启动类
@SpringBootApplication
public class UserConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserConsumerApplication.class, args);
    }
}
```

## 7. 集群容错

框架支持多种集群容错策略：

### 7.1 Failover（失败自动切换）

默认策略，当调用失败时会自动重试其他服务提供者。

```java
@RpcReference(retries = 2)
private HelloService helloService;
```

### 7.2 Failfast（快速失败）

只发起一次调用，失败立即报错。

```java
@RpcReference(cluster = "failfast")
private HelloService helloService;
```

## 8. 负载均衡

框架支持多种负载均衡策略：

### 8.1 Random（随机）

默认策略，按权重随机选择服务提供者。

```java
@RpcReference(loadbalance = "random")
private HelloService helloService;
```

### 8.2 其他负载均衡策略

框架还支持轮询、最少活跃调用数等负载均衡策略。

## 9. 监控和管理

### 9.1 服务监控

框架集成了基本的服务调用统计功能，可以通过日志查看调用情况。

### 9.2 服务管理

通过Nacos控制台可以查看和管理已注册的服务。

## 10. 最佳实践

### 10.1 服务接口设计

1. 服务接口应该保持稳定，避免频繁变更
2. 接口参数和返回值应该是可序列化的
3. 建议为服务接口定义明确的异常处理机制

### 10.2 性能优化

1. 合理设置线程池大小
2. 根据业务场景选择合适的负载均衡策略
3. 设置合适的超时时间和重试次数

### 10.3 容错处理

1. 为服务消费者设置合理的超时时间
2. 根据业务特点选择合适的集群容错策略
3. 做好服务降级和熔断的准备

## 11. 常见问题

### 11.1 服务无法注册

检查Nacos服务是否正常运行，网络连接是否正常。

### 11.2 服务无法发现

检查服务提供者是否正常启动，注册中心配置是否正确。

### 11.3 调用超时

可以适当调整超时时间配置，或者检查网络状况和服务性能。

## 12. 总结

本RPC框架提供了完整的分布式服务调用解决方案，具有以下特点：

1. **易用性**：基于Spring Boot，使用简单
2. **高性能**：基于Netty实现高性能网络通信
3. **高可用**：支持多种集群容错和负载均衡策略
4. **可扩展**：基于SPI机制，支持自定义扩展
5. **生态集成**：与Nacos等主流组件无缝集成

通过本框架，开发者可以快速构建分布式应用，实现服务间的高效通信。