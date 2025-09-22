package cn.geekslife.balking

import spock.lang.Specification
import spock.lang.TempDir

class DataSpec extends Specification {

    @TempDir
    File tempDir

    def "测试Data类的基本功能"() {
        given:
        def filename = new File(tempDir, "test.txt").absolutePath
        def data = new Data(filename, "初始内容")

        when:
        data.save()

        then:
        data.isChanged() == false

        when:
        data.change("新内容")

        then:
        data.isChanged() == true
        data.getContent() == "新内容"

        when:
        data.save()

        then:
        data.isChanged() == false
    }

    def "测试Balking模式的行为"() {
        given:
        def filename = new File(tempDir, "balking_test.txt").absolutePath
        def data = new Data(filename, "初始内容")
        def results = Collections.synchronizedList(new ArrayList())

        when:
        // 首先保存一次
        data.save()
        
        // 再次尝试保存（应该被放弃）
        data.save()

        then:
        // 验证第二次保存被放弃
        noExceptionThrown()
    }

    def "测试多线程环境下的Balking模式"() {
        given:
        def filename = new File(tempDir, "concurrent_test.txt").absolutePath
        def data = new Data(filename, "初始内容")
        def results = Collections.synchronizedList(new ArrayList())
        def threads = []

        when:
        // 首先修改数据
        data.change("并发测试内容")

        // 创建多个线程同时尝试保存
        (1..10).each { i ->
            def thread = Thread.start {
                try {
                    data.save()
                    results.add(Thread.currentThread().name + ": 保存成功")
                } catch (Exception e) {
                    results.add(Thread.currentThread().name + ": 保存失败 - " + e.message)
                }
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join(2000) }

        then:
        // 验证只有一个线程成功保存，其他线程放弃执行
        results.size() == 10
        results.count { it.contains("保存成功") } == 1
        results.count { it.contains("放弃保存操作") } == 9
    }

    def "测试SingleUseTool的Balking行为"() {
        given:
        def tool = new SingleUseTool()
        def results = []

        when:
        // 第一次使用
        def result1 = tool.use()
        
        // 第二次使用（应该被放弃）
        def result2 = tool.use()

        then:
        result1 == true
        result2 == false
        tool.isUsed() == true
    }

    def "测试InitializationManager的Balking行为"() {
        given:
        def manager = new InitializationManager()
        def results = Collections.synchronizedList(new ArrayList())

        when:
        // 第一次初始化
        def result1 = manager.initialize()
        
        // 第二次初始化（应该被放弃）
        def result2 = manager.initialize()

        then:
        result1 != null
        result2 == result1  // 返回相同的结果
        manager.isInitialized() == true
    }

    def "测试AtomicBalkingExample的线程安全性"() {
        given:
        def example = new AtomicBalkingExample()
        def results = Collections.synchronizedList(new ArrayList())
        def threads = []

        when:
        // 创建多个线程同时尝试初始化
        (1..10).each { i ->
            def thread = Thread.start {
                try {
                    def result = example.initialize()
                    results.add(Thread.currentThread().name + ": 初始化" + (result ? "成功" : "被放弃"))
                } catch (Exception e) {
                    results.add(Thread.currentThread().name + ": 初始化异常 - " + e.message)
                }
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join(2000) }

        then:
        // 验证只有一个线程成功初始化，其他线程放弃执行
        results.size() == 10
        results.count { it.contains("初始化成功") } == 1
        results.count { it.contains("已初始化，放弃执行") } == 9
        
        // 验证可以获取数据
        example.getData() != null
    }

    def "测试ExceptionBalkingExample的异常处理"() {
        given:
        def example = new ExceptionBalkingExample()

        when:
        example.activate()
        
        // 再次激活（应该被放弃）
        example.activate()

        then:
        example.isActive() == true
        notThrown(Exception)

        when:
        // 尝试执行操作
        example.execute()

        then:
        notThrown(Exception)
    }

    def "测试AutoSaveService的Balking行为"() {
        given:
        def filename = new File(tempDir, "autosave_test.txt").absolutePath
        def data = new Data(filename, "自动保存测试内容")
        def service = new AutoSaveService(data)

        when:
        // 启动服务
        service.start()
        
        // 再次启动（应该被放弃）
        service.start()

        then:
        service.isRunning() == true

        when:
        // 停止服务
        service.stop()
        
        // 再次停止（应该被放弃）
        service.stop()

        then:
        service.isRunning() == false
    }

    def "测试多线程环境下AutoSaveService的Balking行为"() {
        given:
        def filename = new File(tempDir, "concurrent_autosave_test.txt").absolutePath
        def data = new Data(filename, "并发自动保存测试内容")
        def service = new AutoSaveService(data)
        def results = Collections.synchronizedList(new ArrayList())
        def threads = []

        when:
        // 创建多个线程同时尝试启动服务
        (1..5).each { i ->
            def thread = Thread.start {
                service.start()
                results.add(Thread.currentThread().name + ": 启动服务")
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join(1000) }

        then:
        service.isRunning() == true

        when:
        // 创建多个线程同时尝试停止服务
        results.clear()
        (1..5).each { i ->
            def thread = Thread.start {
                service.stop()
                results.add(Thread.currentThread().name + ": 停止服务")
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join(1000) }

        then:
        service.isRunning() == false
    }
}