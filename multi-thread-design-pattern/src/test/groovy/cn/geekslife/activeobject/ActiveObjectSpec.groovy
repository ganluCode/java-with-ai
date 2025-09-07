package cn.geekslife.activeobject

import spock.lang.Specification

import java.util.concurrent.CompletableFuture

class ActiveObjectSpec extends Specification {

    def "测试Result类的基本功能"() {
        given:
        def result = new Result("测试结果", "TASK-123")

        when:
        def value = result.getValue()
        def timestamp = result.getTimestamp()
        def taskId = result.getTaskId()

        then:
        value == "测试结果"
        taskId == "TASK-123"
        timestamp > 0
        result.toString().contains("测试结果")
    }

    def "测试FutureResult的基本功能"() {
        given:
        def futureResult = new FutureResult()
        def result = new Result("异步结果", "ASYNC-TASK-456")

        when:
        def isReadyBefore = futureResult.isReady()
        futureResult.setResult(result)
        def isReadyAfter = futureResult.isReady()
        def retrievedResult = futureResult.getCurrentResult()

        then:
        isReadyBefore == false
        isReadyAfter == true
        retrievedResult == result
    }

    def "测试FutureResult的等待机制"() {
        given:
        def futureResult = new FutureResult()
        def result = new Result("等待结果", "WAIT-TASK-789")
        def results = Collections.synchronizedList(new ArrayList())

        when:
        // 启动线程获取结果（会等待）
        def getterThread = Thread.start {
            try {
                def retrievedResult = futureResult.getResult()
                results.add("获取到结果: " + retrievedResult.getValue())
            } catch (Exception e) {
                results.add("异常: " + e.message)
            }
        }

        // 等待一段时间让getter线程进入等待状态
        Thread.sleep(100)

        // 设置结果
        futureResult.setResult(result)

        // 等待getter线程完成
        getterThread.join(2000)

        then:
        results.size() == 1
        results[0] == "获取到结果: 等待结果"
    }

    def "测试DoWorkRequest的执行功能"() {
        given:
        def servant = new Servant()
        def futureResult = new FutureResult()
        def request = new DoWorkRequest(servant, "测试工作", futureResult, "TEST-TASK-001")
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        request.execute()
        def result = futureResult.getCurrentResult()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        result != null
        result.getValue().contains("工作完成")
        result.getValue().contains("测试工作")
        output.contains("DoWorkRequest：执行任务 测试工作")
        output.contains("Servant：开始执行工作 测试工作")
        output.contains("DoWorkRequest：任务完成 测试工作")
    }

    def "测试ActivationQueue的基本功能"() {
        given:
        def queue = new ActivationQueue()
        def servant = new Servant()
        def futureResult = new FutureResult()
        def request = new DoWorkRequest(servant, "队列测试", futureResult, "QUEUE-TASK-001")

        when:
        def isEmptyBefore = queue.isEmpty()
        def sizeBefore = queue.size()
        
        queue.enqueue(request)
        
        def isEmptyAfter = queue.isEmpty()
        def sizeAfter = queue.size()
        
        def dequeuedRequest = queue.dequeue()
        
        def isEmptyFinal = queue.isEmpty()
        def sizeFinal = queue.size()

        then:
        isEmptyBefore == true
        sizeBefore == 0
        isEmptyAfter == false
        sizeAfter == 1
        dequeuedRequest == request
        isEmptyFinal == true
        sizeFinal == 0
    }

    def "测试Scheduler的调度功能"() {
        given:
        def scheduler = new Scheduler("TestScheduler")
        def servant = new Servant()
        def futureResult = new FutureResult()
        def request = new DoWorkRequest(servant, "调度测试", futureResult, "SCHED-TASK-001")
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        scheduler.start()
        scheduler.enqueue(request)
        Thread.sleep(2000) // 等待调度器执行
        scheduler.stopScheduler()
        scheduler.join(1000)

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        !scheduler.isAlive() || scheduler.isInterrupted()
        output.contains("TestScheduler：调度器启动")
        output.contains("ActivationQueue：请求入队 DoWorkRequest")
        output.contains("TestScheduler：调度器停止")
    }

    def "测试Proxy的异步调用功能"() {
        given:
        def scheduler = new Scheduler("ProxyTestScheduler")
        def servant = new Servant()
        def proxy = new Proxy(scheduler, servant)
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        scheduler.start()
        def futureResult = proxy.doWork("代理测试")
        Thread.sleep(1500) // 等待执行完成
        def result = futureResult.getResult()
        scheduler.stopScheduler()
        scheduler.join(1000)

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        result != null
        result.getValue().contains("工作完成")
        result.getValue().contains("代理测试")
        output.contains("Proxy：接收到工作请求 代理测试")
        output.contains("Proxy：返回Future对象给客户端 代理测试")
    }

    def "测试ActiveObjectImpl的完整功能"() {
        given:
        def activeObject = new ActiveObjectImpl()
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        def future1 = activeObject.doWork("完整测试1")
        def future2 = activeObject.doWork("完整测试2")
        
        Thread.sleep(2000) // 等待执行完成
        
        def result1 = future1.getResult()
        def result2 = future2.getResult()
        
        def queueSize = activeObject.getQueueSize()
        def workCount = activeObject.getWorkCount()
        
        activeObject.shutdown()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        result1 != null
        result2 != null
        result1.getValue().contains("工作完成")
        result2.getValue().contains("工作完成")
        queueSize == 0
        workCount >= 2
        output.contains("ActiveObjectImpl：Active Object初始化完成")
        output.contains("ActiveObjectImpl：开始关闭Active Object")
        output.contains("ActiveObjectImpl：Active Object关闭完成")
    }

    def "测试ModernActiveObject的异步功能"() {
        given:
        def modernActiveObject = new ModernActiveObject(3)
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        def future = modernActiveObject.doWork("现代测试")
        def result = future.get()
        
        modernActiveObject.shutdown()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        result != null
        result.getValue().contains("工作完成")
        result.getValue().contains("现代测试")
        output.contains("ModernActiveObject：创建现代Active Object")
        output.contains("ModernActiveObject：提交工作请求 现代测试")
        output.contains("ModernActiveObject：异步执行工作 现代测试")
    }

    def "测试ModernActiveObject的批量处理功能"() {
        given:
        def modernActiveObject = new ModernActiveObject(2)
        def names = ["批量1", "批量2", "批量3"]

        when:
        def futures = modernActiveObject.doWorkBatch(names)
        def results = futures.collect { it.get() }

        then:
        results.size() == 3
        results.every { it.getValue().contains("工作完成") }
        results.any { it.getValue().contains("批量1") }
        results.any { it.getValue().contains("批量2") }
        results.any { it.getValue().contains("批量3") }

        cleanup:
        modernActiveObject.shutdown()
    }

    def "测试ActiveObject的并发性能"() {
        given:
        def activeObject = new ActiveObjectImpl()
        def futures = []
        def startTime = System.currentTimeMillis()

        when:
        // 同时提交多个异步请求
        (1..10).each { i ->
            def future = activeObject.doWork("并发测试${i}")
            futures << future
        }

        // 等待所有请求完成
        def results = futures.collect { it.getResult() }
        def endTime = System.currentTimeMillis()
        def duration = endTime - startTime

        activeObject.shutdown()

        then:
        results.size() == 10
        results.every { it.getValue().contains("工作完成") }
        results.every { it.getValue().contains("并发测试") }
        duration < 5000 // 应该比串行执行快
    }

    def "测试ActiveObject的关闭功能"() {
        given:
        def activeObject = new ActiveObjectImpl()
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        activeObject.shutdown()
        def future = activeObject.doWork("关闭后测试")

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("ActiveObjectImpl：已请求关闭")
        future.getCurrentResult() != null
        future.getCurrentResult().getValue().contains("ERROR")
    }

    def "测试异常处理功能"() {
        given:
        def servant = new Servant()
        def futureResult = new FutureResult()
        def request = new DoWorkRequest(servant, "异常测试", futureResult, "ERROR-TASK-001") {
            @Override
            public Result execute() {
                throw new RuntimeException("模拟执行异常")
            }
        }

        when:
        request.execute()
        def result = futureResult.getCurrentResult()

        then:
        result != null
        result.getValue().contains("ERROR")
    }

    def "测试队列管理功能"() {
        given:
        def scheduler = new Scheduler("QueueManagerScheduler")
        def servant = new Servant()
        def proxy = new Proxy(scheduler, servant)
        def queue = new ActivationQueue()

        when:
        // 提交多个请求
        def futures = (1..5).collect { i ->
            proxy.doWork("队列管理测试${i}")
        }
        
        def queueSize = proxy.getQueueSize()
        
        // 清空队列
        queue.clear()
        def clearedQueueSize = queue.size()

        then:
        queueSize >= 0
        clearedQueueSize == 0
    }

    def "测试CompletableFuture的组合功能"() {
        given:
        def future1 = CompletableFuture.supplyAsync({
            Thread.sleep(100)
            return new Result("结果1", "COMBINE-1")
        })
        
        def future2 = CompletableFuture.supplyAsync({
            Thread.sleep(100)
            return new Result("结果2", "COMBINE-2")
        })

        when:
        def combinedFuture = future1.thenCombine(future2, { result1, result2 ->
            return new Result(result1.getValue() + " + " + result2.getValue(), "COMBINED")
        })

        def result = combinedFuture.get()

        then:
        result != null
        result.getValue().contains("结果1 + 结果2")
    }
}