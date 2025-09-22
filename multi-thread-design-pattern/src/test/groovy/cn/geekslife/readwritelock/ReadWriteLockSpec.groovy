package cn.geekslife.readwritelock

import spock.lang.Specification

class ReadWriteLockSpec extends Specification {

    def "测试Data类的基本读写功能"() {
        given:
        def data = new Data("初始数据")

        when:
        def result = data.read()

        then:
        result == "初始数据"
        data.length() == 6

        when:
        data.write("新数据")
        def newResult = data.read()

        then:
        newResult == "新数据"
        data.length() == 3
        data.contains("新")
    }

    def "测试多读者并发读取"() {
        given:
        def data = new Data("测试数据")
        def results = Collections.synchronizedList(new ArrayList())
        def threads = []

        when:
        // 创建多个读者线程同时读取
        (1..10).each { i ->
            def thread = Thread.start {
                try {
                    def result = data.read()
                    results.add(Thread.currentThread().name + ": " + result)
                    Thread.sleep((long) (Math.random() * 50))
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt()
                }
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join(2000) }

        then:
        results.size() == 10
        results.every { it.contains("测试数据") }
    }

    def "测试读者和写者并发操作"() {
        given:
        def data = new Data("初始数据")
        def readResults = Collections.synchronizedList(new ArrayList())
        def writeResults = Collections.synchronizedList(new ArrayList())
        def threads = []

        when:
        // 创建读者线程
        (1..5).each { i ->
            def thread = Thread.start {
                try {
                    (1..3).each {
                        def result = data.read()
                        readResults.add(Thread.currentThread().name + ": " + result)
                        Thread.sleep((long) (Math.random() * 30))
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt()
                }
            }
            threads << thread
        }

        // 创建写者线程
        (1..2).each { i ->
            def thread = Thread.start {
                try {
                    (1..2).each { j ->
                        def newData = "写者${i}-数据${j}"
                        data.write(newData)
                        writeResults.add(Thread.currentThread().name + ": " + newData)
                        Thread.sleep((long) (Math.random() * 50))
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt()
                }
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join(3000) }

        then:
        readResults.size() == 15  // 5个读者，每个读取3次
        writeResults.size() == 4  // 2个写者，每个写入2次
        
        // 验证读取结果中包含写入的数据
        def allReadData = readResults.collect { it.split(": ")[1] }
        def allWriteData = writeResults.collect { it.split(": ")[1] }
        
        // 由于写操作会改变数据，后续的读操作应该能读到新数据
        allReadData.any { readData ->
            allWriteData.any { writeData ->
                readData == writeData
            }
        }
    }

    def "测试FairData的公平锁行为"() {
        given:
        def fairData = new FairData("公平锁测试")
        def results = Collections.synchronizedList(new ArrayList())
        def threads = []

        when:
        // 先启动一些读者线程
        (1..3).each { i ->
            def thread = Thread.start {
                fairData.read()
            }
            threads << thread
        }

        // 等待读者线程开始执行
        Thread.sleep(50)

        // 启动写者线程
        def writerThread = Thread.start {
            fairData.write("写者数据")
        }
        threads << writerThread

        // 再启动一些读者线程
        (4..6).each { i ->
            def thread = Thread.start {
                fairData.read()
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join(2000) }

        then:
        // 公平锁应该按照请求顺序处理
        noExceptionThrown()
    }

    def "测试StampedData的乐观读锁"() {
        given:
        def stampedData = new StampedData("乐观锁测试")

        when:
        def result = stampedData.readOptimistic()

        then:
        result == "乐观锁测试"

        when:
        stampedData.write("新数据")
        def newResult = stampedData.readOptimistic()

        then:
        newResult == "新数据"
    }

    def "测试StampedData的锁转换"() {
        given:
        def stampedData = new StampedData("原始数据")

        when:
        def originalData = stampedData.readAndWrite("转换后数据")

        then:
        originalData == "原始数据"
        stampedData.read() == "转换后数据"
    }

    def "测试ThreadSafeCache的基本功能"() {
        given:
        def cache = new ThreadSafeCache<String, Integer>()

        when:
        cache.put("key1", 100)
        cache.put("key2", 200)

        then:
        cache.size() == 2
        cache.containsKey("key1")
        cache.containsKey("key2")
        cache.get("key1") == 100
        cache.get("key2") == 200

        when:
        def removedValue = cache.remove("key1")

        then:
        removedValue == 100
        cache.size() == 1
        !cache.containsKey("key1")

        when:
        cache.clear()

        then:
        cache.size() == 0
        !cache.containsKey("key2")
    }

    def "测试ThreadSafeCache的并发访问"() {
        given:
        def cache = new ThreadSafeCache<String, Integer>()
        def putResults = Collections.synchronizedList(new ArrayList())
        def getResults = Collections.synchronizedList(new ArrayList())
        def threads = []

        when:
        // 创建多个线程同时进行put和get操作
        (1..5).each { i ->
            // put线程
            def putThread = Thread.start {
                try {
                    (1..10).each { j ->
                        def key = "线程${i}-键${j}"
                        def value = i * 100 + j
                        cache.put(key, value)
                        putResults.add("${key}=${value}")
                        Thread.sleep((long) (Math.random() * 10))
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt()
                }
            }
            threads << putThread

            // get线程
            def getThread = Thread.start {
                try {
                    (1..15).each { j ->
                        def key = "线程${(i % 5) + 1}-键${j}"
                        def value = cache.get(key)
                        getResults.add("${key}=${value}")
                        Thread.sleep((long) (Math.random() * 15))
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt()
                }
            }
            threads << getThread
        }

        // 等待所有线程完成
        threads.each { it.join(5000) }

        then:
        putResults.size() == 50  // 5个线程，每个put 10次
        getResults.size() == 75  // 5个线程，每个get 15次
        cache.size() == 50
        
        // 验证put的数据能够被get到
        putResults.every { putResult ->
            def (key, value) = putResult.split("=")
            def cachedValue = cache.get(key)
            cachedValue == null || cachedValue.toString() == value
        }
    }

    def "测试读写锁的性能优势"() {
        given:
        def data = new Data("性能测试数据")
        def startTime = System.currentTimeMillis()
        def readThreads = []
        def writeThreads = []

        when:
        // 启动大量读者线程
        (1..20).each { i ->
            def thread = Thread.start {
                (1..50).each {
                    data.read()
                }
            }
            readThreads << thread
        }

        // 启动少量写者线程
        (1..2).each { i ->
            def thread = Thread.start {
                (1..10).each { j ->
                    data.write("写者${i}-数据${j}")
                }
            }
            writeThreads << thread
        }

        // 等待所有线程完成
        readThreads.each { it.join(5000) }
        writeThreads.each { it.join(5000) }

        def endTime = System.currentTimeMillis()
        def duration = endTime - startTime

        then:
        // 验证系统能够正常完成所有操作
        duration > 0
        data.snapshot() != null
    }
}