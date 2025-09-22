package cn.geekslife.future

import spock.lang.Specification

import java.util.concurrent.*

class FutureSpec extends Specification {

    def "测试RealData的基本功能"() {
        given:
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        def realData = new RealData("测试数据")

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        realData.getContent().contains("测试数据")
        realData.isReady() == true
        output.contains("RealData：开始构造真实数据")
        output.contains("RealData：真实数据构造完成")
    }

    def "测试FutureData的基本功能"() {
        given:
        def futureData = new FutureData()
        def realData = new RealData("未来数据")

        when:
        futureData.setRealData(realData)
        def content = futureData.getContent()

        then:
        content.contains("未来数据")
        futureData.isReady() == true
    }

    def "测试FutureData的等待机制"() {
        given:
        def futureData = new FutureData()
        def results = Collections.synchronizedList(new ArrayList())

        when:
        // 启动线程获取数据（会等待）
        def getterThread = Thread.start {
            try {
                def content = futureData.getContent()
                results.add("获取到内容: " + content)
            } catch (Exception e) {
                results.add("异常: " + e.message)
            }
        }

        // 等待一段时间让getter线程进入等待状态
        Thread.sleep(100)

        // 设置真实数据
        def realData = new RealData("延迟数据")
        futureData.setRealData(realData)

        // 等待getter线程完成
        getterThread.join(2000)

        then:
        results.size() == 1
        results[0].contains("获取到内容")
        results[0].contains("延迟数据")
    }

    def "测试Host的Future模式实现"() {
        given:
        def host = new Host()
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        def future = host.request("主机请求数据")

        // 等待后台处理完成
        Thread.sleep(1500)

        def content = future.getContent()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        future.isReady() == true
        content.contains("主机请求数据")
        output.contains("Host：接收到请求")
        output.contains("RealData：开始构造真实数据")
    }

    def "测试CompletableFuture的基本功能"() {
        given:
        def completableFutureExample = new CompletableFutureExample()
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        def future = completableFutureExample.processAsync("异步数据")

        // 等待完成
        def result = future.get()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        result.contains("处理结果")
        result.contains("异步数据")
        output.contains("CompletableFutureExample：开始异步处理")
        output.contains("CompletableFutureExample：后台处理数据")
    }

    def "测试CompletableFuture的链式调用"() {
        given:
        def completableFutureExample = new CompletableFutureExample()
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        def future = completableFutureExample.processChain("链式数据")

        // 等待完成
        def result = future.get()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        result.contains("[前缀]")
        result.contains("[后缀]")
        result.contains("链式数据")
        output.contains("步骤1：转换为大写")
        output.contains("步骤2：添加前缀")
        output.contains("步骤3：添加后缀")
    }

    def "测试CompletableFuture的异常处理"() {
        given:
        def completableFutureExample = new CompletableFutureExample()
        def originalOut = System.out
        def baos = new ByteArrayOutputStream()
        def ps = new PrintStream(baos)
        System.setOut(ps)

        when:
        def future = completableFutureExample.processWithExceptionHandling("异常测试数据")

        // 等待完成
        def result = future.get()

        System.setOut(originalOut)
        def output = baos.toString()

        then:
        result != null
        output.contains("异常处理示例")
    }

    def "测试ExecutorServiceFutureExample的基本功能"() {
        given:
        def executorExample = new ExecutorServiceFutureExample()
        def task = {
            Thread.sleep(100)
            return "任务结果"
        } as Callable<String>

        when:
        def future = executorExample.submitTask(task)

        // 等待完成
        def result = future.get()

        then:
        result == "任务结果"
        future.isDone() == true

        cleanup:
        executorExample.shutdown()
    }

    def "测试ExecutorServiceFutureExample的批量任务"() {
        given:
        def executorExample = new ExecutorServiceFutureExample()
        def tasks = (1..3).collect { i ->
            {
                Thread.sleep(100)
                return "任务${i}结果"
            } as Callable<String>
        }

        when:
        def futures = executorExample.submitTasks(tasks)

        // 等待所有任务完成
        def results = futures.collect { it.get() }

        then:
        results.size() == 3
        results.contains("任务1结果")
        results.contains("任务2结果")
        results.contains("任务3结果")

        cleanup:
        executorExample.shutdown()
    }

    def "测试CustomFuture的基本功能"() {
        given:
        def customFuture = new CustomFuture<String>()

        when:
        // 在另一个线程中设置结果
        Thread.start {
            Thread.sleep(100)
            customFuture.setResult("自定义结果")
        }

        // 获取结果
        def result = customFuture.get()

        then:
        result == "自定义结果"
        customFuture.isDone() == true
        customFuture.isCancelled() == false
    }

    def "测试CustomFuture的超时功能"() {
        given:
        def customFuture = new CustomFuture<String>()

        when:
        // 尝试在超时时间内获取结果
        customFuture.get(500, TimeUnit.MILLISECONDS)

        then:
        def exception = thrown(TimeoutException)
        exception.message == "获取结果超时"
    }

    def "测试CustomFuture的取消功能"() {
        given:
        def customFuture = new CustomFuture<String>()

        when:
        def cancelled = customFuture.cancel(true)

        then:
        cancelled == true
        customFuture.isCancelled() == true
        customFuture.isDone() == false
    }

    def "测试Future模式的并发性能"() {
        given:
        def host = new Host()
        def futures = []
        def startTime = System.currentTimeMillis()

        when:
        // 同时发起多个异步请求
        (1..5).each { i ->
            def future = host.request("并发请求${i}")
            futures << future
        }

        // 获取所有结果
        def results = futures.collect { it.getContent() }
        def endTime = System.currentTimeMillis()
        def duration = endTime - startTime

        then:
        results.size() == 5
        results.every { it.contains("并发请求") }
        duration < 3000 // 应该比串行执行快
    }

    def "测试CompletableFuture的组合功能"() {
        given:
        def future1 = CompletableFuture.supplyAsync({
            Thread.sleep(100)
            return "结果1"
        })
        
        def future2 = CompletableFuture.supplyAsync({
            Thread.sleep(100)
            return "结果2"
        })

        when:
        def combinedFuture = future1.thenCombine(future2, { result1, result2 ->
            return result1 + " + " + result2
        })

        def result = combinedFuture.get()

        then:
        result == "结果1 + 结果2"
    }

    def "测试Future模式与传统同步调用的对比"() {
        given:
        def host = new Host()

        when:
        // Future模式 - 异步
        def startTime = System.currentTimeMillis()
        def future = host.request("异步测试")
        // 在等待期间可以做其他事情
        Thread.sleep(100)
        def asyncResult = future.getContent()
        def asyncDuration = System.currentTimeMillis() - startTime

        // 传统同步 - 直接创建RealData
        startTime = System.currentTimeMillis()
        def realData = new RealData("同步测试")
        def syncResult = realData.getContent()
        def syncDuration = System.currentTimeMillis() - startTime

        then:
        asyncResult.contains("异步测试")
        syncResult.contains("同步测试")
        // 异步模式应该更快或相近
        asyncDuration <= syncDuration + 200
    }
}