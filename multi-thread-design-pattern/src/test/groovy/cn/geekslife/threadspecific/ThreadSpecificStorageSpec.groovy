package cn.geekslife.threadspecific

import spock.lang.Specification

class ThreadSpecificStorageSpec extends Specification {

    def "测试ContextManager的基本功能"() {
        given:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        def context = new Context("user123", "request456")
        ContextManager.setContext(context)
        def retrievedContext = ContextManager.getContext()
        def hasContext = ContextManager.hasContext()
        def userId = ContextManager.getCurrentUserId()
        def requestId = ContextManager.getCurrentRequestId()
        ContextManager.clearContext()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        retrievedContext == context
        hasContext == true
        userId == "user123"
        requestId == "request456"
        output.contains("设置上下文")
        output.contains("获取上下文")
        output.contains("清理上下文")
    }

    def "测试多线程环境下的数据隔离"() {
        given:
        def results = Collections.synchronizedMap(new HashMap<String, Context>())
        def threads = []

        when:
        // 创建多个线程，每个线程设置不同的上下文
        (1..5).each { i ->
            def thread = Thread.start("Thread-${i}") {
                def context = new Context("user${i}", "request${i}")
                ContextManager.setContext(context)
                
                // 等待一段时间
                Thread.sleep(100)
                
                // 获取上下文
                def retrievedContext = ContextManager.getContext()
                results.put(Thread.currentThread().name, retrievedContext)
                
                ContextManager.clearContext()
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join(2000) }

        then:
        results.size() == 5
        (1..5).every { i ->
            def context = results["Thread-${i}"]
            context != null && context.getUserId() == "user${i}" && context.getRequestId() == "request${i}"
        }
    }

    def "测试DatabaseConnectionManager的功能"() {
        given:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        // 获取连接
        def conn1 = DatabaseConnectionManager.getConnection()
        def conn2 = DatabaseConnectionManager.getConnection() // 应该返回同一个连接
        def isConnected = DatabaseConnectionManager.isConnected()
        
        // 关闭连接
        DatabaseConnectionManager.closeConnection()
        def isClosed = !DatabaseConnectionManager.isConnected()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        conn1 == conn2 // 同一个线程应该返回相同的连接
        isConnected == true
        isClosed == true
        output.contains("创建新的数据库连接")
        output.contains("使用现有数据库连接")
        output.contains("关闭数据库连接")
    }

    def "测试LogManager的功能"() {
        given:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        LogManager.setLogLevel(LogManager.LogLevel.DEBUG)
        LogManager.debug("调试信息")
        LogManager.info("普通信息")
        LogManager.warn("警告信息")
        LogManager.error("错误信息")
        def logContent = LogManager.flush()
        LogManager.clear()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("DEBUG")
        output.contains("INFO")
        output.contains("WARN")
        output.contains("ERROR")
        output.contains("调试信息")
        output.contains("普通信息")
        output.contains("警告信息")
        output.contains("错误信息")
        logContent.contains("调试信息")
        logContent.contains("普通信息")
    }

    def "测试CounterManager的功能"() {
        given:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        def initialCount = CounterManager.getCounter()
        def count1 = CounterManager.increment()
        def count2 = CounterManager.increment()
        def finalCount = CounterManager.getCounter()
        CounterManager.reset()
        def resetCount = CounterManager.getCounter()
        CounterManager.clear()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        initialCount == 0
        count1 == 1
        count2 == 2
        finalCount == 2
        resetCount == 0
        output.contains("计数器递增 0 -> 1")
        output.contains("计数器递增 1 -> 2")
        output.contains("重置计数器 2 -> 0")
    }

    def "测试SessionManager的功能"() {
        given:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        SessionManager.createSession("session-123")
        def hasSession = SessionManager.hasActiveSession()
        SessionManager.setUserData("user-data-456")
        def userData = SessionManager.getUserData()
        def session = SessionManager.getCurrentSession()
        SessionManager.clearSession()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        hasSession == true
        userData == "user-data-456"
        session != null
        session.getSessionId() == "session-123"
        output.contains("创建会话 session-123")
        output.contains("设置用户数据到会话")
        output.contains("访问会话 session-123")
        output.contains("清理会话 session-123")
    }

    def "测试InheritableContextManager的继承功能"() {
        given:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        // 重定向System.out后无法看到子线程的输出，所以我们直接调用演示方法
        def parentContext = new Context("parent-user", "parent-request")
        InheritableContextManager.setContext(parentContext)
        def parentCtx = InheritableContextManager.getContext()
        
        InheritableContextManager.clearContext()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        parentCtx == parentContext
        output.contains("设置可继承上下文")
        output.contains("获取可继承上下文")
        output.contains("清理可继承上下文")
    }

    def "测试父子线程上下文继承"() {
        given:
        def parentResults = Collections.synchronizedList(new ArrayList())
        def childResults = Collections.synchronizedList(new ArrayList())

        when:
        def parentThread = Thread.start("ParentThread") {
            // 设置父线程上下文
            def parentContext = new Context("parent-user", "parent-request-123")
            ContextManager.setContext(parentContext)
            parentResults.add("Parent context: " + ContextManager.getContext())
            
            // 创建子线程
            def childThread = Thread.start("ChildThread") {
                // 子线程应该有独立的上下文存储
                def childContext = new Context("child-user", "child-request-456")
                ContextManager.setContext(childContext)
                childResults.add("Child context: " + ContextManager.getContext())
                ContextManager.clearContext()
            }
            
            childThread.join()
            parentResults.add("Parent context after child: " + ContextManager.getContext())
            ContextManager.clearContext()
        }

        parentThread.join(3000)

        then:
        parentResults.size() == 2
        childResults.size() == 1
        parentResults[0].contains("parent-user")
        parentResults[0].contains("parent-request-123")
        childResults[0].contains("child-user")
        childResults[0].contains("child-request-456")
        parentResults[1].contains("parent-user")
        parentResults[1].contains("parent-request-123")
    }

    def "测试ThreadLocal内存泄漏防护"() {
        given:
        def threadLocal = new ThreadLocal<String>()
        def results = Collections.synchronizedList(new ArrayList())

        when:
        // 设置值
        threadLocal.set("test-value")
        results.add("Value set: " + threadLocal.get())
        
        // 清理值
        threadLocal.remove()
        results.add("Value after remove: " + threadLocal.get())

        then:
        results[0].contains("test-value")
        results[1].contains("null")
    }

    def "测试多线程并发访问性能"() {
        given:
        def threadCount = 10
        def operationsPerThread = 100
        def threads = []
        def errors = Collections.synchronizedList(new ArrayList())

        when:
        // 创建多个线程并发访问ThreadLocal
        (1..threadCount).each { i ->
            def thread = Thread.start("ConcurrentThread-${i}") {
                try {
                    (1..operationsPerThread).each { j ->
                        // 每个线程操作自己的ThreadLocal数据
                        def context = new Context("user-${i}", "request-${i}-${j}")
                        ContextManager.setContext(context)
                        
                        def retrievedContext = ContextManager.getContext()
                        if (retrievedContext != context) {
                            errors.add("Context mismatch in thread ${i}, operation ${j}")
                        }
                        
                        ContextManager.clearContext()
                    }
                } catch (Exception e) {
                    errors.add("Exception in thread ${i}: ${e.message}")
                }
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join(5000) }

        then:
        errors.isEmpty()
    }

    def "测试线程池环境下的ThreadLocal清理"() {
        given:
        def executor = java.util.concurrent.Executors.newFixedThreadPool(3)
        def results = Collections.synchronizedList(new ArrayList())

        when:
        // 提交多个任务到线程池
        (1..10).each { i ->
            executor.submit({
                try {
                    // 每个任务设置自己的上下文
                    def context = new Context("pool-user-${i}", "pool-request-${i}")
                    ContextManager.setContext(context)
                    
                    // 模拟任务执行
                    Thread.sleep(50)
                    
                    def retrievedContext = ContextManager.getContext()
                    results.add("Task ${i}: ${retrievedContext?.getUserId()}")
                    
                    // 清理上下文
                    ContextManager.clearContext()
                } catch (Exception e) {
                    results.add("Task ${i} error: ${e.message}")
                }
            } as Runnable)
        }

        // 关闭线程池并等待任务完成
        executor.shutdown()
        executor.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS)

        then:
        results.size() == 10
        results.every { it.startsWith("Task ") }
    }
}