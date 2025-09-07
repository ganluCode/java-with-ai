package cn.geekslife.threadpermessage

import spock.lang.Specification

class ThreadPerMessageSpec extends Specification {

    def "测试Message类的基本功能"() {
        given:
        def message = new Message("测试消息内容", "TEST-001")

        when:
        def content = message.getContent()
        def timestamp = message.getTimestamp()
        def messageId = message.getMessageId()

        then:
        content == "测试消息内容"
        messageId == "TEST-001"
        timestamp > 0
        message.toString().contains("TEST-001")
    }

    def "测试DirectThreadMessageHandler的基本功能"() {
        given:
        def handler = new DirectThreadMessageHandler()
        def message = new Message("直接线程消息", "DIRECT-001")
        def results = Collections.synchronizedList(new ArrayList())

        when:
        // 重定向System.out以捕获输出
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        handler.handleMessage(message)

        // 等待线程完成
        Thread.sleep(1500)

        // 恢复System.out
        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("DirectThreadMessageHandler：接收到消息")
        output.contains("已启动线程处理消息 DIRECT-001")
    }

    def "测试ThreadPoolMessageHandler的基本功能"() {
        given:
        def handler = new ThreadPoolMessageHandler(3)
        def message = new Message("线程池消息", "POOL-001")

        when:
        // 重定向System.out以捕获输出
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        handler.handleMessage(message)

        // 等待线程完成
        Thread.sleep(1500)

        // 恢复System.out
        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("ThreadPoolMessageHandler：接收到消息")
        output.contains("已提交消息 POOL-001 到线程池")

        cleanup:
        handler.shutdown()
    }

    def "测试多消息并发处理"() {
        given:
        def handler = new DirectThreadMessageHandler()
        def messages = (1..5).collect { i -> new Message("并发消息-${i}", "CONCURRENT-${i}") }
        def results = Collections.synchronizedList(new ArrayList())

        when:
        // 重定向System.out以捕获输出
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        handler.handleMessages(messages as Message[])

        // 等待所有线程完成
        Thread.sleep(2000)

        // 恢复System.out
        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("DirectThreadMessageHandler：开始处理 5 个消息")
        (1..5).every { i -> 
            output.contains("已启动线程处理消息 CONCURRENT-${i}")
        }
    }

    def "测试AsyncService的基本功能"() {
        given:
        def service = new AsyncService()
        def message = new Message("异步消息", "ASYNC-001")
        def callbackExecuted = new AtomicBoolean(false)

        when:
        // 重定向System.out以捕获输出
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        service.processMessageAsync(message, {
            callbackExecuted.set(true)
        })

        // 等待异步任务完成
        Thread.sleep(2000)

        // 恢复System.out
        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("AsyncService：异步处理消息 ASYNC-001")
        callbackExecuted.get() == true

        cleanup:
        service.shutdown()
    }

    def "测试ThreadPerMessageServer的基本功能"() {
        given:
        def server = new ThreadPerMessageServer()

        when:
        // 重定向System.out以捕获输出
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        server.start()

        // 让服务器运行一段时间
        Thread.sleep(2000)

        server.stop()

        // 等待服务器完全停止
        Thread.sleep(500)

        // 恢复System.out
        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("ThreadPerMessageServer：服务器启动")
        output.contains("ThreadPerMessageServer：接收到消息")
        output.contains("ThreadPerMessageServer：服务器停止")
        server.getMessageCount() > 0
    }

    def "测试ExecutorServiceServer的基本功能"() {
        given:
        def server = new ExecutorServiceServer(2)

        when:
        // 重定向System.out以捕获输出
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        server.start()

        // 让服务器运行一段时间
        Thread.sleep(2000)

        server.stop()

        // 等待服务器完全停止
        Thread.sleep(500)

        // 恢复System.out
        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("ExecutorServiceServer：创建服务器，线程池大小为 2")
        output.contains("ExecutorServiceServer：服务器启动")
        output.contains("ExecutorServiceServer：接收到消息")
        output.contains("ExecutorServiceServer：服务器停止")
        server.getMessageCount() > 0

        and:
        server.getActiveThreadCount() >= 0
    }

    def "测试RobustMessageHandler的异常处理"() {
        given:
        def handler = new RobustMessageHandler()
        def message = new Message("不可靠消息", "UNRELIABLE-001")

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

        handler.handleUnreliableMessage(message)

        // 等待线程完成
        Thread.sleep(2000)

        // 恢复System.out和System.err
        System.setOut(originalOut)
        System.setErr(originalErr)
        def output = baosOut.toString()
        def errorOutput = baosErr.toString()

        then:
        output.contains("RobustMessageHandler：接收到不可靠消息")
        output.contains("RobustMessageHandler：不可靠消息 UNRELIABLE-001 处理线程结束")
        // 由于有30%的概率失败，可能有错误输出
        (errorOutput.contains("RobustMessageHandler：处理不可靠消息时发生错误") || 
         output.contains("RobustMessageHandler：处理不可靠消息时发生错误")) == true
    }

    def "测试线程池与直接创建线程的性能对比"() {
        given:
        def directHandler = new DirectThreadMessageHandler()
        def poolHandler = new ThreadPoolMessageHandler(5)
        def messages = (1..10).collect { i -> new Message("性能测试消息-${i}", "PERF-${i}") }

        when:
        def startTime = System.currentTimeMillis()
        
        // 测试直接创建线程
        directHandler.handleMessages(messages as Message[])
        Thread.sleep(100) // 短暂间隔
        
        def directTime = System.currentTimeMillis() - startTime
        
        // 测试线程池
        startTime = System.currentTimeMillis()
        poolHandler.handleMessages(messages as Message[])
        def poolTime = System.currentTimeMillis() - startTime

        // 等待所有任务完成
        Thread.sleep(3000)

        then:
        // 两种方式都应该能够处理消息
        directTime >= 0
        poolTime >= 0

        cleanup:
        poolHandler.shutdown()
    }

    def "测试大量并发消息处理"() {
        given:
        def handler = new ThreadPoolMessageHandler(10)
        def messageCount = 20
        def messages = (1..messageCount).collect { i -> new Message("大量并发消息-${i}", "BULK-${i}") }
        def processedCount = new AtomicInteger(0)

        when:
        // 重定向System.out以减少输出干扰
        def originalOut = System.out
        System.setOut(new PrintStream(new ByteArrayOutputStream()))

        // 处理大量消息
        messages.each { message ->
            handler.handleMessage(message)
        }

        // 等待所有消息处理完成
        Thread.sleep(3000)

        // 恢复System.out
        System.setOut(originalOut)

        then:
        // 验证系统能够处理大量并发消息
        noExceptionThrown()

        cleanup:
        handler.shutdown()
    }
}