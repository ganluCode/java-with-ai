package cn.geekslife.guardedsuspension

import spock.lang.Specification

class RequestQueueSpec extends Specification {

    def "测试RequestQueue的基本功能"() {
        given:
        def queue = new RequestQueue()

        when:
        def request1 = new Request("请求1")
        def request2 = new Request("请求2")
        queue.putRequest(request1)
        queue.putRequest(request2)

        then:
        queue.size() == 2
        !queue.isEmpty()

        when:
        def retrievedRequest1 = queue.getRequest()
        def retrievedRequest2 = queue.getRequest()

        then:
        retrievedRequest1 == request1
        retrievedRequest2 == request2
        queue.isEmpty()
    }

    def "测试RequestQueue的Guarded Suspension行为"() {
        given:
        def queue = new RequestQueue()
        def results = Collections.synchronizedList(new ArrayList())
        def consumerThread = Thread.start {
            try {
                // 消费者线程会等待直到有请求可用
                def request = queue.getRequest()
                results.add("获取到请求: " + request.getName())
            } catch (InterruptedException e) {
                results.add("线程被中断")
                Thread.currentThread().interrupt()
            }
        }

        when:
        // 等待一段时间让消费者线程进入等待状态
        Thread.sleep(100)
        // 生产者添加请求
        def request = new Request("延迟请求")
        queue.putRequest(request)
        // 等待消费者线程完成
        consumerThread.join(1000)

        then:
        results.size() == 1
        results[0] == "获取到请求: 延迟请求"
    }

    def "测试RequestQueueWithLock的功能"() {
        given:
        def queue = new RequestQueueWithLock()

        when:
        def request1 = new Request("请求1")
        def request2 = new Request("请求2")
        queue.putRequest(request1)
        queue.putRequest(request2)

        then:
        queue.size() == 2
        !queue.isEmpty()

        when:
        def retrievedRequest1 = queue.getRequest()
        def retrievedRequest2 = queue.getRequest()

        then:
        retrievedRequest1 == request1
        retrievedRequest2 == request2
        queue.isEmpty()
    }

    def "测试RequestQueueWithBlockingQueue的功能"() {
        given:
        def queue = new RequestQueueWithBlockingQueue()

        when:
        def request1 = new Request("请求1")
        def request2 = new Request("请求2")
        queue.putRequest(request1)
        queue.putRequest(request2)

        then:
        queue.size() == 2
        !queue.isEmpty()

        when:
        def retrievedRequest1 = queue.getRequest()
        def retrievedRequest2 = queue.getRequest()

        then:
        retrievedRequest1 == request1
        retrievedRequest2 == request2
        queue.isEmpty()
    }

    def "测试TimeoutRequestQueue的超时功能"() {
        given:
        def queue = new TimeoutRequestQueue(2)
        def results = Collections.synchronizedList(new ArrayList())

        when:
        // 填满队列
        queue.putRequest(new Request("请求1"))
        queue.putRequest(new Request("请求2"))

        // 尝试在超时时间内添加请求
        def addResult = queue.putRequest(new Request("请求3"), 500)

        then:
        !addResult  // 添加应该失败，因为队列已满且超时

        when:
        // 清空队列
        queue.getRequest()
        queue.getRequest()

        // 尝试在超时时间内获取请求
        def getResult = queue.getRequest(500)

        then:
        getResult == null  // 获取应该返回null，因为队列为空且超时
    }

    def "测试GuardedObject的基本功能"() {
        given:
        def guardedObject = new GuardedObject()
        def results = Collections.synchronizedList(new ArrayList())

        when:
        // 启动获取数据的线程
        def getterThread = Thread.start {
            try {
                def data = guardedObject.get()
                results.add("获取到数据: " + data)
            } catch (InterruptedException e) {
                results.add("线程被中断")
                Thread.currentThread().interrupt()
            }
        }

        // 等待一段时间让getter线程进入等待状态
        Thread.sleep(100)

        // 设置数据
        guardedObject.put("测试数据")

        // 等待getter线程完成
        getterThread.join(1000)

        then:
        results.size() == 1
        results[0] == "获取到数据: 测试数据"
        guardedObject.isReady()
    }

    def "测试生产者-消费者模式的并发安全性"() {
        given:
        def queue = new RequestQueue(10)
        def producedRequests = Collections.synchronizedList(new ArrayList())
        def consumedRequests = Collections.synchronizedList(new ArrayList())
        def producerThreads = []
        def consumerThreads = []

        when:
        // 创建生产者线程
        (1..5).each { i ->
            def thread = Thread.start {
                (1..10).each { j ->
                    try {
                        def request = new Request("生产者${i}-请求${j}")
                        queue.putRequest(request)
                        producedRequests.add(request)
                        // 模拟生产耗时
                        Thread.sleep(10)
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt()
                    }
                }
            }
            producerThreads << thread
        }

        // 创建消费者线程
        (1..3).each { i ->
            def thread = Thread.start {
                (1..16).each { j ->
                    try {
                        def request = queue.getRequest()
                        consumedRequests.add(request)
                        // 模拟消费耗时
                        Thread.sleep(5)
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt()
                    }
                }
            }
            consumerThreads << thread
        }

        // 等待所有线程完成
        producerThreads.each { it.join(5000) }
        consumerThreads.each { it.join(5000) }

        then:
        producedRequests.size() == 50  // 5个生产者，每个生产10个请求
        consumedRequests.size() == 48  // 3个消费者，每个消费16个请求
        queue.size() == 2  // 剩余2个请求在队列中

        // 验证所有消费的请求都在生产的请求中
        consumedRequests.every { consumed ->
            producedRequests.any { produced ->
                produced == consumed
            }
        }
    }

    def "测试虚假唤醒问题的防护"() {
        given:
        def queue = new RequestQueue()
        def results = Collections.synchronizedList(new ArrayList())

        when:
        // 启动多个消费者线程
        def threads = (1..5).collect { i ->
            Thread.start {
                try {
                    // 使用while循环防护虚假唤醒
                    def request = queue.getRequest()
                    results.add("线程${i}获取到请求: " + request.getName())
                } catch (InterruptedException e) {
                    results.add("线程${i}被中断")
                    Thread.currentThread().interrupt()
                }
            }
        }

        // 等待让消费者线程进入等待状态
        Thread.sleep(100)

        // 添加一个请求
        queue.putRequest(new Request("单个请求"))

        // 等待所有线程完成
        threads.each { it.join(1000) }

        then:
        // 只有一个线程应该成功获取到请求
        results.size() == 1
        results[0].startsWith("线程")
        results[0].endsWith("获取到请求: 单个请求")
    }
}