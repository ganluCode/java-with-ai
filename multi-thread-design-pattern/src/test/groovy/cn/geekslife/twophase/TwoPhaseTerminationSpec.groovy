package cn.geekslife.twophase

import spock.lang.Specification

class TwoPhaseTerminationSpec extends Specification {

    def "测试GracefulThread的基本功能"() {
        given:
        def thread = new GracefulThread()
        thread.setName("TestThread")

        when:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        thread.start()
        Thread.sleep(200)
        thread.shutdown()
        thread.join(1000)

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        !thread.isAlive()
        output.contains("TestThread：收到终止请求")
        output.contains("TestThread：捕获到中断异常")
        output.contains("TestThread：执行清理工作")
        output.contains("TestThread：线程安全终止")
    }

    def "测试Worker线程的功能"() {
        given:
        def worker = new Worker()
        worker.setName("WorkerThread")

        when:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        worker.start()
        Thread.sleep(1000)
        worker.shutdown()
        worker.join(2000)

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        !worker.isAlive()
        output.contains("WorkerThread：收到终止请求")
        output.contains("WorkerThread：执行工作")
        output.contains("WorkerThread：执行清理工作")
        output.contains("WorkerThread：总共完成了")
        worker.getCount() > 0
    }

    def "测试TaskProcessor的任务处理功能"() {
        given:
        def taskProcessor = new TaskProcessor()
        taskProcessor.setName("TaskProcessorThread")
        def taskResults = Collections.synchronizedList(new ArrayList())

        when:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        taskProcessor.start()

        // 提交几个任务
        (1..3).each { i ->
            taskProcessor.submit({
                taskResults.add("任务${i}执行完成")
                println "执行任务${i}"
            } as Runnable)
        }

        Thread.sleep(2000)
        taskProcessor.shutdown()
        taskProcessor.join(3000)

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        !taskProcessor.isAlive()
        taskResults.size() == 3
        taskResults.contains("任务1执行完成")
        taskResults.contains("任务2执行完成")
        taskResults.contains("任务3执行完成")
        output.contains("TaskProcessorThread：提交任务到队列")
        output.contains("TaskProcessorThread：处理任务")
        output.contains("TaskProcessorThread：任务处理完成")
    }

    def "测试Service的启动和停止功能"() {
        given:
        def service = new Service()

        when:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        service.start()
        Thread.sleep(500)
        
        // 提交一些任务
        service.submitTask({
            println "服务任务执行"
        } as Runnable)
        
        Thread.sleep(500)
        service.stop()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        !service.isRunning()
        output.contains("Service：启动服务")
        output.contains("Service：停止服务")
        output.contains("WorkerThread：收到终止请求")
        output.contains("TaskProcessorThread：收到终止请求")
        output.contains("Service：服务已停止")
    }

    def "测试TimeoutGracefulThread的超时功能"() {
        given:
        def timeoutThread = new TimeoutGracefulThread(1000) // 1秒超时
        timeoutThread.setName("TimeoutThread")

        when:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        timeoutThread.start()
        Thread.sleep(1500) // 超过超时时间
        timeoutThread.shutdown()
        timeoutThread.join(2000)

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        !timeoutThread.isAlive()
        output.contains("TimeoutThread：收到终止请求")
        output.contains("TimeoutThread：检测到超时")
        output.contains("TimeoutThread：由于超时被强制终止")
        timeoutThread.isForceTerminated()
    }

    def "测试DataSaveThread的数据保存功能"() {
        given:
        def dataSaveThread = new DataSaveThread("测试数据", "test.txt")
        dataSaveThread.setName("DataSaveThread")

        when:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        dataSaveThread.start()
        Thread.sleep(1000)
        dataSaveThread.shutdown()
        dataSaveThread.join(3000)

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        !dataSaveThread.isAlive()
        output.contains("DataSaveThread：收到终止请求")
        output.contains("DataSaveThread：开始保存数据")
        output.contains("DataSaveThread：数据保存到 test.txt 完成")
        output.contains("DataSaveThread：数据保存线程清理完成")
        dataSaveThread.isDataSaved()
    }

    def "测试优雅关闭钩子功能"() {
        given:
        def service = new Service()
        def hook = new GracefulShutdownHook(service)

        when:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        // 模拟关闭钩子执行
        hook.run()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("GracefulShutdownHook：收到JVM关闭信号")
        output.contains("Service：停止服务")
        output.contains("GracefulShutdownHook：优雅关闭完成")
    }

    def "测试线程终止后的任务提交"() {
        given:
        def taskProcessor = new TaskProcessor()
        taskProcessor.setName("TestTaskProcessor")

        when:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        taskProcessor.start()
        taskProcessor.shutdown()
        taskProcessor.join(1000)

        // 尝试在终止后提交任务
        taskProcessor.submit({
            println "这个任务不应该被执行"
        } as Runnable)

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("TestTaskProcessor：已请求终止，无法提交新任务")
    }

    def "测试多个线程的并发终止"() {
        given:
        def workers = (1..5).collect { i ->
            def worker = new Worker()
            worker.setName("Worker-${i}")
            worker.start()
            return worker
        }

        when:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        // 同时终止所有线程
        workers.each { it.shutdown() }

        // 等待所有线程终止
        workers.each { it.join(2000) }

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        workers.every { !it.isAlive() }
        (1..5).every { i ->
            output.contains("Worker-${i}：收到终止请求") &&
            output.contains("Worker-${i}：线程安全终止")
        }
    }

    def "测试终止超时处理"() {
        given:
        def service = new Service()

        when:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        service.start()
        Thread.sleep(100)

        // 测试停止服务的超时处理
        service.stop()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        output.contains("Service：启动服务")
        output.contains("Service：停止服务")
        output.contains("Service：服务已停止")
    }

    def "测试InterruptedException的正确处理"() {
        given:
        def thread = new GracefulThread() {
            @Override
            protected void doWork() throws InterruptedException {
                // 模拟长时间运行的任务
                Thread.sleep(5000)
            }
        }
        thread.setName("InterruptTestThread")

        when:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        thread.start()
        Thread.sleep(100)
        thread.shutdown() // 这会调用interrupt()
        thread.join(2000)

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        !thread.isAlive()
        output.contains("InterruptTestThread：收到终止请求")
        output.contains("InterruptTestThread：捕获到中断异常")
        output.contains("InterruptTestThread：执行清理工作")
        output.contains("InterruptTestThread：线程安全终止")
    }
}