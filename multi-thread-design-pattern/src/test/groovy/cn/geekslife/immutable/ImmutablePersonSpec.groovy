package cn.geekslife.immutable

import spock.lang.Specification

class ImmutablePersonSpec extends Specification {

    def "测试ImmutablePerson的基本功能"() {
        given:
        def address = new ImmutableAddress("中山路123号", "上海市", "200000")
        def person = new ImmutablePerson("张三", 30, address)

        when:
        def name = person.getName()
        def age = person.getAge()
        def personAddress = person.getAddress()

        then:
        name == "张三"
        age == 30
        personAddress.getStreet() == "中山路123号"
        personAddress.getCity() == "上海市"
        personAddress.getZipCode() == "200000"
    }

    def "测试ImmutablePerson的with方法创建新对象"() {
        given:
        def address = new ImmutableAddress("中山路123号", "上海市", "200000")
        def person = new ImmutablePerson("张三", 30, address)

        when:
        def newPerson = person.withName("李四")

        then:
        person.getName() == "张三"  // 原对象未改变
        newPerson.getName() == "李四"  // 新对象有新值
        person.getAge() == newPerson.getAge()  // 其他属性相同
        person.getAddress() == newPerson.getAddress()  // 地址相同
    }

    def "测试ImmutablePerson的防御性拷贝"() {
        given:
        def originalAddress = new ImmutableAddress("中山路123号", "上海市", "200000")
        def person = new ImmutablePerson("张三", 30, originalAddress)

        when:
        def personAddress = person.getAddress()
        personAddress.withStreet("南京路456号")  // 尝试修改返回的地址

        then:
        person.getAddress().getStreet() == "中山路123号"  // 原对象地址未改变
        person.getAddress() != originalAddress  // 返回的是副本
    }

    def "测试ImmutablePerson的equals和hashCode"() {
        given:
        def address1 = new ImmutableAddress("中山路123号", "上海市", "200000")
        def address2 = new ImmutableAddress("中山路123号", "上海市", "200000")
        def person1 = new ImmutablePerson("张三", 30, address1)
        def person2 = new ImmutablePerson("张三", 30, address2)
        def person3 = new ImmutablePerson("李四", 30, address1)

        expect:
        person1 == person2  // 内容相同的对象应该相等
        person1.hashCode() == person2.hashCode()  // 相等的对象应该有相同的hashCode
        person1 != person3  // 内容不同的对象不应该相等
    }

    def "测试ImmutablePerson在多线程环境下的安全性"() {
        given:
        def address = new ImmutableAddress("中山路123号", "上海市", "200000")
        def person = new ImmutablePerson("张三", 30, address)
        def threadCount = 100
        def results = Collections.synchronizedList(new ArrayList())
        def threads = []

        when:
        // 创建多个线程同时访问同一个不可变对象
        (1..threadCount).each {
            def thread = Thread.start {
                // 每个线程都读取对象的属性
                def name = person.getName()
                def age = person.getAge()
                def street = person.getAddress().getStreet()
                results.add([name, age, street])
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join() }

        then:
        // 验证所有线程都得到了相同的结果
        results.size() == threadCount
        results.every { it == ["张三", 30, "中山路123号"] }
    }

    def "测试MutablePerson在多线程环境下的问题"() {
        given:
        def mutableAddress = new MutablePerson.MutableAddress("中山路123号", "上海市", "200000")
        def mutablePerson = new MutablePerson("张三", 30, mutableAddress)
        def threadCount = 100
        def results = Collections.synchronizedList(new ArrayList())
        def threads = []

        when:
        // 创建多个线程，其中一些线程会修改对象状态
        (1..threadCount).each { i ->
            def thread = Thread.start {
                if (i % 10 == 0) {
                    // 每10个线程中有一个线程修改对象状态
                    mutablePerson.setName("修改后的姓名" + i)
                    mutablePerson.setAge(i)
                    mutablePerson.getAddress().setStreet("修改后的街道" + i)
                }
                
                // 所有线程都读取对象的属性
                def name = mutablePerson.getName()
                def age = mutablePerson.getAge()
                def street = mutablePerson.getAddress().getStreet()
                results.add([name, age, street])
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join() }

        then:
        // 由于竞态条件，不同线程可能得到不同的结果
        results.size() == threadCount
        // 验证存在不一致的结果
        def uniqueResults = results.unique()
        uniqueResults.size() > 1  // 应该有多种不同的结果
    }

    def "测试ImmutablePersonWithBuilder的功能"() {
        when:
        def person = ImmutablePersonWithBuilder.builder()
                .setName("王五")
                .setAge(25)
                .setEmail("wangwu@example.com")
                .setPhoneNumber("13800138000")
                .build()

        then:
        person.getName() == "王五"
        person.getAge() == 25
        person.getEmail() == "wangwu@example.com"
        person.getPhoneNumber() == "13800138000"
    }

    def "测试ImmutablePersonWithHobbies的功能"() {
        given:
        def hobbies = ["读书", "游泳", "音乐"]
        def person = new ImmutablePersonWithHobbies("赵六", 28, hobbies)

        when:
        def personHobbies = person.getHobbies()

        then:
        person.getName() == "赵六"
        person.getAge() == 28
        personHobbies.contains("读书")
        personHobbies.contains("游泳")
        personHobbies.contains("音乐")

        when:
        // 尝试修改返回的爱好列表
        try {
            personHobbies.add("电影")
            false  // 如果没有抛出异常，测试失败
        } catch (UnsupportedOperationException e) {
            true  // 期望抛出此异常
        }

        and:
        // 使用addHobby方法创建新对象
        def newPerson = person.addHobby("电影")

        then:
        person.getHobbies().size() == 3  // 原对象未改变
        newPerson.getHobbies().size() == 4  // 新对象包含新爱好
        newPerson.getHobbies().contains("电影")
    }
}