package cn.geekslife.producerconsumer

import spock.lang.Specification

class ProducerConsumerSpec extends Specification {

    def "测试BoundedBuffer的基本功能"() {
        given:
        def buffer = new BoundedBuffer(3)

        when:
        def item1 = new Item("数据1", 1)
        def item2 = new Item("数据2", 2)
        buffer.put(item1)
        buffer.put(item2)

        then:
        buffer.size() == 2
        !buffer.isEmpty()
        !buffer.isFull()

        when:
        def takenItem1 = buffer.take()
        def takenItem2 = buffer.take()

        then:
        takenItem1 == item1
        takenItem2 == item2
        buffer.isEmpty()
        !buffer.isFull()
    }

    def "测试BoundedBuffer的阻塞行为"() {
        given:
        def buffer = new BoundedBuffer(2)
        def results = Collections.synchronizedList(new ArrayList())

        when:
        // 填满缓冲区
        buffer.put(new Item("数据1", 1))
        buffer.put(new Item("数据2", 2))

        // 启动生产者线程，应该被阻塞
        def producerThread = Thread.start {
            try {
                buffer.put(new Item("数据3", 3))
                results.add("生产者完成")
            } catch (InterruptedException e) {
                results.add("生产者被中断")
                Thread.currentThread().interrupt()
            }
        }

        // 等待一段时间让消费者线程进入等待状态
        Thread.sleep(100)

        // 消费一个数据项，应该唤醒生产者
        def consumedItem = buffer.take()

        // 等待生产者线程完成
        producerThread.join(1000)

        then:
        consumedItem.getData() == "数据1"
        results.size() == 1
        results[0] == "生产者完成"
        buffer.size() == 2
    }

    def "测试BoundedBufferWithLock的功能"() {
        given:
        def buffer = new BoundedBufferWithLock(3)

        when:
        def item1 = new Item("数据1", 1)
        def item2 = new Item("数据2", 2)
        buffer.put(item1)
        buffer.put(item2)

        then:
        buffer.size() == 2
        !buffer.isEmpty()
        !buffer.isFull()

        when:
        def takenItem1 = buffer.take()
        def takenItem2 = buffer.take()

        then:
        takenItem1 == item1
        takenItem2 == item2
        buffer.isEmpty()
        !buffer.isFull()
    }

    def "测试BlockingQueueBuffer的功能"() {
        given:
        def buffer = new BlockingQueueBuffer(3)

        when:
        def item1 = new Item("数据1", 1)
        def item2 = new Item("数据2", 2)
        buffer.put(item1)
        buffer.put(item2)

        then:
        buffer.size() == 2
        !buffer.isEmpty()
        !buffer.isFull()

        when:
        def takenItem1 = buffer.take()
        def takenItem2 = buffer.take()

        then:
        takenItem1 == item1
        takenItem2 == item2
        buffer.isEmpty()
        !buffer.isFull()
    }

    def "测试BlockingQueueBuffer的try操作"() {
        given:
        def buffer = new BlockingQueueBuffer(2)

        when:
        def item1 = new Item("数据1", 1)
        def item2 = new Item("数据2", 2)
        def item3 = new Item("数据3", 3)
        
        def result1 = buffer.tryPut(item1)
        def result2 = buffer.tryPut(item2)
        def result3 = buffer.tryPut(item3)  // 应该失败

        then:
        result1 == true
        result2 == true
        result3 == false
        buffer.size() == 2

        when:
        def takenItem1 = buffer.tryTake()
        def takenItem2 = buffer.tryTake()
        def takenItem3 = buffer.tryTake()  // 应该返回null

        then:
        takenItem1 == item1
        takenItem2 == item2
        takenItem3 == null
        buffer.isEmpty()
    }

    def "测试生产者-消费者并发操作"() {
        given:
        def buffer = new BoundedBuffer(5)
        def producedItems = Collections.synchronizedList(new ArrayList())
        def consumedItems = Collections.synchronizedList(new ArrayList())
        def producerThreads = []
        def consumerThreads = []

        when:
        // 创建生产者线程
        (1..3).each { i ->
            def thread = Thread.start {
                try {
                    (1..5).each { j ->
                        def item = new Item("生产者${i}-数据${j}", j)
                        buffer.put(item)
                        producedItems.add(item)
                        Thread.sleep((long) (Math.random() * 10))
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt()
                }
            }
            producerThreads << thread
        }

        // 创建消费者线程
        (1..2).each { i ->
            def thread = Thread.start {
                try {
                    (1..7).each { j ->
                        def item = buffer.take()
                        consumedItems.add(item)
                        Thread.sleep((long) (Math.random() * 15))
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt()
                }
            }
            consumerThreads << thread
        }

        // 等待所有线程完成
        producerThreads.each { it.join(5000) }
        consumerThreads.each { it.join(5000) }

        then:
        producedItems.size() == 15  // 3个生产者，每个生产5个数据项
        consumedItems.size() == 14  // 2个消费者，每个消费7个数据项
        buffer.size() == 1  // 剩余1个数据项在缓冲区中

        // 验证所有消费的数据项都在生产的数据项中
        consumedItems.every { consumed ->
            producedItems.any { produced ->
                produced == consumed
            }
        }
    }

    def "测试生产者消费者系统的正确性"() {
        given:
        def manager = new ProducerConsumerManager(10, 2, 4)
        def buffer = manager.getBuffer()

        when:
        // 启动系统
        manager.start(5, 8)
        
        // 等待一段时间让系统运行
        Thread.sleep(1000)
        
        // 关闭系统
        manager.shutdown()

        then:
        // 验证系统能够正常启动和关闭
        noExceptionThrown()
        
        // 验证缓冲区状态
        buffer.getCapacity() == 10
    }

    def "测试多线程环境下的数据一致性"() {
        given:
        def buffer = new BoundedBuffer(100)
        def producedItems = Collections.synchronizedSet(new HashSet())
        def consumedItems = Collections.synchronizedSet(new HashSet())
        def producerThreads = []
        def consumerThreads = []
        def errors = Collections.synchronizedList(new ArrayList())

        when:
        // 创建多个生产者线程
        (1..5).each { i ->
            def thread = Thread.start {
                try {
                    (1..20).each { j ->
                        def itemId = i * 100 + j
                        def item = new Item("数据${itemId}", itemId)
                        synchronized (producedItems) {
                            if (producedItems.contains(item)) {
                                errors.add("重复生产数据项: " + item)
                            }
                            producedItems.add(item)
                        }
                        buffer.put(item)
                    }
                } catch (Exception e) {
                    errors.add("生产者线程${i}异常: " + e.message)
                }
            }
            producerThreads << thread
        }

        // 创建多个消费者线程
        (1..3).each { i ->
            def thread = Thread.start {
                try {
                    (1..33).each { j ->
                        def item = buffer.take()
                        synchronized (consumedItems) {
                            if (consumedItems.contains(item)) {
                                errors.add("重复消费数据项: " + item)
                            }
                            consumedItems.add(item)
                        }
                    }
                } catch (Exception e) {
                    errors.add("消费者线程${i}异常: " + e.message)
                }
            }
            consumerThreads << thread
        }

        // 等待所有线程完成
        producerThreads.each { it.join(5000) }
        consumerThreads.each { it.join(5000) }

        then:
        // 验证没有错误
        errors.isEmpty()
        
        // 验证生产的数据项数量
        producedItems.size() == 100  // 5个生产者，每个生产20个数据项
        
        // 验证消费的数据项数量
        consumedItems.size() == 99  // 3个消费者，每个消费33个数据项
        
        // 验证所有消费的数据项都在生产的数据项中
        consumedItems.every { consumed ->
            producedItems.contains(consumed)
        }
    }
}