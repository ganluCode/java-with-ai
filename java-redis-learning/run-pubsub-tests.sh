#!/bin/bash

# Redis Publish/Subscribe 测试运行脚本
# 用于运行Redis发布/订阅功能的测试

echo "==========================================="
echo "  Redis Publish/Subscribe 测试运行脚本"
echo "==========================================="

# 检查Redis服务器是否运行
echo "检查Redis服务器连接..."
if nc -z localhost 6379; then
    echo "✓ Redis服务器正在运行在 localhost:6379"
else
    echo "✗ Redis服务器未运行在 localhost:6379"
    echo "请先启动Redis服务器，然后重新运行此脚本"
    exit 1
fi

# 编译项目
echo ""
echo "编译项目..."
mvn compile -q
if [ $? -eq 0 ]; then
    echo "✓ 项目编译成功"
else
    echo "✗ 项目编译失败"
    exit 1
fi

# 运行Redis发布/订阅测试
echo ""
echo "运行Redis发布/订阅功能测试..."
echo "注意：以下测试需要Redis服务器运行在localhost:6379"
echo ""

# 运行基础发布/订阅测试
echo "1. 运行基础发布/订阅测试..."
mvn test -Dtest=RedisPubSubTest#testBasicPublishSubscribe -q
if [ $? -eq 0 ]; then
    echo "✓ 基础发布/订阅测试通过"
else
    echo "✗ 基础发布/订阅测试失败"
fi

echo ""
echo "2. 运行多订阅者测试..."
mvn test -Dtest=RedisPubSubTest#testMultipleSubscribers -q
if [ $? -eq 0 ]; then
    echo "✓ 多订阅者测试通过"
else
    echo "✗ 多订阅者测试失败"
fi

echo ""
echo "3. 运行取消订阅功能测试..."
mvn test -Dtest=RedisPubSubTest#testUnsubscribeFunctionality -q
if [ $? -eq 0 ]; then
    echo "✓ 取消订阅功能测试通过"
else
    echo "✗ 取消订阅功能测试失败"
fi

echo ""
echo "4. 运行并发发布/订阅测试..."
mvn test -Dtest=RedisPubSubTest#testConcurrentPublishSubscribe -q
if [ $? -eq 0 ]; then
    echo "✓ 并发发布/订阅测试通过"
else
    echo "✗ 并发发布/订阅测试失败"
fi

echo ""
echo "==========================================="
echo "  Redis Publish/Subscribe 测试完成"
echo "==========================================="

# 提供使用示例运行选项
echo ""
echo "如需运行使用示例，请执行："
echo "  mvn exec:java -Dexec.mainClass=\"com.example.redis.lock.RedisPubSubExample\""
echo ""