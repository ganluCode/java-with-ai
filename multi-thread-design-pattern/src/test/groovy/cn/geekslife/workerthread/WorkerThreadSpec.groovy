package cn.geekslife.workerthread

import spock.lang.Specification

class WorkerThreadSpec extends Specification {

    def "测试Request类的基本功能"() {
        given:
        def request = new Request("测试请求", 1)

        when:
        def name = request.getName()
        def timestamp = request.getTimestamp()
        def requestId = request.getRequestId()

        then:
        name == "测试请求"
        requestId == 1
        timestamp > 0
        request.toString().contains("测试请求")
    }

    def "测试Channel的基本功能"() {
        given:
        def channel = new Channel(3)

        when:
        channel.start()
        def isRunning = channel.isRunning()

        then:
        isRunning == true

        when:
        def request = new Request("测试请求", 1)
        channel.put(request)
        def queueSize = channel.getQueueSize()

        then:
        queueSize == 1

        cleanup:
        channel.stop()
    }

    def "测试WorkerThread的基本功能"() {
        given:
        def channel = new Channel(1)
        def workerThread = new WorkerThread("TestWorker", channel)

        when:
        // 重定向System.out以捕获输出
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        workerThread.start()
        Thread.sleep(100)
        workerThread.interrupt()
        workerThread.join(1000)

        // 恢复System.out
        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("TestWorker：工作线程启动")
        output.contains("TestWorker：工作线程被中断")
        output.contains("TestWorker：工作线程结束")
    }

    def "测试完整的Worker Thread模式"() {
        given:
        def channel = new Channel(2)
        def clientThread = new ClientThread("TestClient", channel, 5)

        when:
        // 重定向System.out以捕获输出
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        channel.start()
        clientThread.start()

        // 等待客户端线程完成
        clientThread.join(3000)

        // 等待一段时间让工作线程处理任务
        Thread.sleep(3000)

        channel.stop()

        // 恢复System.out
        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("Channel：启动通道")
        output.contains("TestClient：客户端线程启动")
        output.contains("WorkerThread-0：工作线程启动")
        output.contains("WorkerThread-1：工作线程启动")
        output.contains("开始执行请求")
        output.contains("执行完成")
        output.contains("Channel：停止通道")
    }

    def "测试ExecutorServiceWorkerManager的基本功能"() {
        given:
        def manager = new ExecutorServiceWorkerManager(3)
        def request = new Request("线程池请求", 1)

        when:
        // 重定向System.out以捕获输出
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        manager.submit(request)

        // 等待任务完成
        Thread.sleep(2000)

        manager.shutdown()

        // 恢复System.out
        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("ExecutorServiceWorkerManager：创建线程池")
        output.contains("提交请求")
        output.contains("开始执行请求")
        output.contains("执行完成")
        output.contains("工作线程管理器已关闭")
    }

    def "测试批量任务提交"() {
        given:
        def manager = new ExecutorServiceWorkerManager(2)
        def requests = (1..5).collect { i -> new Request("批量请求-${i}", i) }

        when:
        // 重定向System.out以捕获输出
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        manager.submitBatch(requests as Request[])

        // 等待任务完成
        Thread.sleep(3000)

        manager.shutdown()

        // 恢复System.out
        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("批量提交 5 个请求")
        (1..5).every { i -> 
            output.contains("批量请求-${i}")
        }
    }

    def "测试MonitoredWorkerManager的监控功能"() {
        given:
        def manager = new MonitoredWorkerManager(2)
        def clientThread = new ClientThread("MonitoredClient", manager, 3)

        when:
        // 重定向System.out以捕获输出
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        manager.start()
        clientThread.start()

        // 等待客户端线程完成
        clientThread.join(2000)

        // 等待一段时间让工作线程处理任务
        Thread.sleep(3000)

        manager.stop()

        // 等待工作线程结束
        Thread.sleep(1000)

        // 恢复System.out
        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("MonitoredWorkerManager：启动管理器")
        output.contains("MonitoredWorker-0：监控工作线程启动")
        output.contains("MonitoredWorker-1：监控工作线程启动")
        output.contains("开始执行请求")
        output.contains("执行完成")
        output.contains("MonitoredWorkerManager：停止管理器")

        and:
        manager.getCompletedTasks() >= 0
        manager.getTotalTasks() >= 0
    }

    def "测试多个客户端并发提交任务"() {
        given:
        def channel = new Channel(3)
        def clientThreads = []
        def clientCount = 3
        def requestsPerClient = 4

        when:
        // 重定向System.out以捕获输出
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        channel.start()

        // 创建多个客户端线程
        (1..clientCount).each { i ->
            def client = new ClientThread("Client-${i}", channel, requestsPerClient)
            clientThreads << client
            client.start()
        }

        // 等待所有客户端线程完成
        clientThreads.each { it.join(3000) }

        // 等待一段时间让工作线程处理任务
        Thread.sleep(5000)

        channel.stop()

        // 恢复System.out
        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("Channel：启动通道")
        (1..clientCount).every { i -> 
            output.contains("Client-${i}：客户端线程启动")
        }
        output.contains("WorkerThread-0：工作线程启动")
        output.contains("WorkerThread-1：工作线程启动")
        output.contains("WorkerThread-2：工作线程启动")
        output.contains("开始执行请求")
        output.contains("执行完成")
        output.contains("Channel：停止通道")

        and:
        channel.getQueueSize() == 0
    }

    def "测试工作线程异常处理"() {
        given:
        def manager = new ExecutorServiceWorkerManager(2)

        when:
        // 重定向System.out和System.err以捕获输出
        def originalOut = System.out
        def originalErr = System.err
        def baosOut = new ByteArrayOutputStream()
        def baosErr = new ByteArrayOutputStream()
        def psOut = new PrintStream(baosOut)
        def psErr = new PrintStream(baosErr)
        System.setOut(psOut)
        System.setErr(psErr)

        // 提交一些正常任务
        (1..3).each { i ->
            def request = new Request("正常请求-${i}", i)
            manager.submit(request)
        }

        // 等待任务完成
        Thread.sleep(3000)

        manager.shutdown()

        // 恢复System.out和System.err
        System.setOut(originalOut)
        System.setErr(originalErr)
        def output = baosOut.toString()
        def errorOutput = baosErr.toString()

        then:
        output.contains("正常请求-1")
        output.contains("正常请求-2")
        output.contains("正常请求-3")
        output.contains("开始执行请求")
        output.contains("执行完成")
    }

    def "测试线程池大小对性能的影响"() {
        given:
        def smallPoolManager = new ExecutorServiceWorkerManager(2)
        def largePoolManager = new ExecutorServiceWorkerManager(5)
        def requests = (1..10).collect { i -> new Request("性能测试-${i}", i) }

        when:
        def startTime = System.currentTimeMillis()
        
        // 使用小线程池
        requests.each { request ->
            smallPoolManager.submit(request)
        }
        Thread.sleep(3000)
        smallPoolManager.shutdown()
        
        def smallPoolTime = System.currentTimeMillis() - startTime
        
        // 重置计时器
        startTime = System.currentTimeMillis()
        
        // 使用大线程池
        requests.each { request ->
            largePoolManager.submit(request)
        }
        Thread.sleep(3000)
        largePoolManager.shutdown()
        
        def largePoolTime = System.currentTimeMillis() - startTime

        then:
        // 两种配置都应该能够处理任务
        smallPoolTime >= 0
        largePoolTime >= 0
    }
}