package cn.geekslife.singletonthreadedexecution

import spock.lang.Specification

class UnsafeBankAccountSpec extends Specification {

    def "测试不安全银行账户的基本操作"() {
        given:
        def unsafeAccount = new UnsafeBankAccount("测试账户", 1000)

        when:
        unsafeAccount.unsafeDeposit(500)
        unsafeAccount.unsafeWithdraw(200)

        then:
        unsafeAccount.getBalance() == 1300
    }

    def "演示不安全账户在并发环境下的问题"() {
        given:
        def unsafeAccount = new UnsafeBankAccount("并发测试账户", 0)
        def threadCount = 100
        def depositAmount = 10
        def threads = []

        when:
        // 创建多个线程同时进行存款操作
        (1..threadCount).each {
            def thread = Thread.start {
                unsafeAccount.unsafeDeposit(depositAmount)
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join() }

        then:
        // 由于竞态条件，最终余额通常不等于期望值
        // 这个测试可能会失败，证明了不安全操作的问题
        unsafeAccount.getBalance() != threadCount * depositAmount
        // 输出实际余额以观察竞态条件的影响
        System.out.println("期望余额: " + (threadCount * depositAmount))
        System.out.println("实际余额: " + unsafeAccount.getBalance())
    }
}