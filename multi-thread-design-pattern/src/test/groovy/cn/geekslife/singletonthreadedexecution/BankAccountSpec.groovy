package cn.geekslife.singletonthreadedexecution

import spock.lang.Specification

class BankAccountSpec extends Specification {

    def "测试银行账户的基本操作"() {
        given:
        def account = new BankAccount("测试账户", 1000)

        when:
        account.deposit(500)
        account.withdraw(200)

        then:
        account.getBalance() == 1300
    }

    def "测试转账操作"() {
        given:
        def account1 = new BankAccount("账户1", 1000)
        def account2 = new BankAccount("账户2", 500)

        when:
        account1.transfer(account2, 300)

        then:
        account1.getBalance() == 700
        account2.getBalance() == 800
    }

    def "测试并发存款操作的线程安全性"() {
        given:
        def account = new BankAccount("并发测试账户", 0)
        def threadCount = 10
        def depositAmount = 100
        def threads = []

        when:
        // 创建多个线程同时存款
        (1..threadCount).each {
            def thread = Thread.start {
                account.deposit(depositAmount)
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join() }

        then:
        // 验证最终余额是否正确
        account.getBalance() == threadCount * depositAmount
    }

    def "测试并发取款操作的线程安全性"() {
        given:
        def initialBalance = 10000
        def account = new BankAccount("并发取款测试账户", initialBalance)
        def threadCount = 5
        def withdrawAmount = 100
        def threads = []

        when:
        // 创建多个线程同时取款
        (1..threadCount).each {
            def thread = Thread.start {
                try {
                    account.withdraw(withdrawAmount)
                } catch (IllegalStateException e) {
                    // 余额不足时忽略异常
                    System.out.println("取款失败: " + e.getMessage())
                }
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join() }

        then:
        // 验证最终余额是否正确
        account.getBalance() == initialBalance - (threadCount * withdrawAmount)
    }

    def "测试并发转账操作的线程安全性"() {
        given:
        def account1 = new BankAccount("账户1", 10000)
        def account2 = new BankAccount("账户2", 5000)
        def account3 = new BankAccount("账户3", 3000)
        def threadCount = 5
        def transferAmount = 100
        def threads = []

        when:
        // 创建多个线程进行转账操作
        (1..threadCount).each { i ->
            def thread = Thread.start {
                if (i % 2 == 1) {
                    // 奇数线程: 账户1向账户2转账
                    account1.transfer(account2, transferAmount)
                } else {
                    // 偶数线程: 账户2向账户3转账
                    account2.transfer(account3, transferAmount)
                }
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join() }

        then:
        // 验证所有账户的总余额保持不变
        account1.getBalance() + account2.getBalance() + account3.getBalance() == 18000
    }

    def "测试不安全账户的并发问题"() {
        given:
        def unsafeAccount = new UnsafeBankAccount("不安全账户", 0)
        def threadCount = 100
        def depositAmount = 10
        def threads = []

        when:
        // 创建多个线程同时进行不安全的存款操作
        (1..threadCount).each {
            def thread = Thread.start {
                unsafeAccount.unsafeDeposit(depositAmount)
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join() }

        then:
        // 不安全账户的余额通常不会等于期望值，证明存在竞态条件
        unsafeAccount.getBalance() != threadCount * depositAmount
    }

    def "测试ReentrantLock实现的银行账户"() {
        given:
        def accountWithLock = new BankAccountWithLock("Lock账户", 1000)
        def threadCount = 10
        def depositAmount = 50
        def threads = []

        when:
        // 创建多个线程同时存款
        (1..threadCount).each {
            def thread = Thread.start {
                accountWithLock.deposit(depositAmount)
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join() }

        then:
        // 验证最终余额是否正确
        accountWithLock.getBalance() == 1000 + (threadCount * depositAmount)
    }

    def "测试ReentrantLock实现的转账操作"() {
        given:
        def account1 = new BankAccountWithLock("Lock账户1", 5000)
        def account2 = new BankAccountWithLock("Lock账户2", 3000)
        def threadCount = 5
        def transferAmount = 100
        def threads = []

        when:
        // 创建多个线程进行转账操作
        (1..threadCount).each { i ->
            def thread = Thread.start {
                if (i % 2 == 1) {
                    // 奇数线程: 账户1向账户2转账
                    account1.transfer(account2, transferAmount)
                } else {
                    // 偶数线程: 账户2向账户1转账
                    account2.transfer(account1, transferAmount)
                }
            }
            threads << thread
        }

        // 等待所有线程完成
        threads.each { it.join() }

        then:
        // 验证所有账户的总余额保持不变
        account1.getBalance() + account2.getBalance() == 8000
    }
}