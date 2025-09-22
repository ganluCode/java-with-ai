# 测试框架配置说明

## 已配置的测试框架

本项目已配置以下测试框架：

### 1. JUnit 5
- 默认的单元测试框架
- 用于Java测试类

### 2. Spock Framework
- 基于Groovy的测试框架
- 提供BDD风格的测试语法
- 依赖：
  - `spock-core:2.3-groovy-3.0`
  - `groovy:3.0.17`

### 3. Mockito
- Mock框架，用于创建和管理测试替身
- 依赖：
  - `mockito-core:5.7.0`
  - `mockito-junit-jupiter:5.7.0`

### 4. JaCoCo (Java Code Coverage)
- 代码覆盖率工具
- 用于测量测试代码的覆盖率
- 版本：`0.8.11`

## Maven配置

测试框架的依赖和插件已配置在根目录的`pom.xml`中，所有子项目都会继承这些配置：

```xml
<!-- 根目录pom.xml中的测试依赖管理 -->
<dependencyManagement>
    <dependencies>
        <!-- JUnit 5 -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.jupiter.version}</version>
            <scope>test</scope>
        </dependency>
        
        <!-- Spock测试框架 -->
        <dependency>
            <groupId>org.spockframework</groupId>
            <artifactId>spock-core</artifactId>
            <version>${spock.version}</version>
            <scope>test</scope>
        </dependency>
        
        <!-- Mockito框架 -->
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>${mockito.version}</version>
            <scope>test</scope>
        </dependency>
        
        <!-- Mockito JUnit Jupiter集成 -->
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-junit-jupiter</artifactId>
            <version>${mockito.version}</version>
            <scope>test</scope>
        </dependency>
        
        <!-- Groovy依赖（Spock需要） -->
        <dependency>
            <groupId>org.codehaus.groovy</groupId>
            <artifactId>groovy</artifactId>
            <version>${groovy.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- 所有子模块都继承的依赖 -->
<dependencies>
    <!-- 所有测试框架依赖 -->
</dependencies>

<!-- 插件管理 -->
<build>
    <pluginManagement>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0-M5</version>
            </plugin>
            
            <!-- Groovy编译插件 -->
            <plugin>
                <groupId>org.codehaus.gmavenplus</groupId>
                <artifactId>gmavenplus-plugin</artifactId>
                <version>3.0.2</version>
                <configuration>
                    <targetBytecode>17</targetBytecode>
                </configuration>
            </plugin>
            
            <!-- JaCoCo代码覆盖率插件 -->
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>${jacoco.version}</version>
            </plugin>
        </plugins>
    </pluginManagement>
    
    <!-- 所有子模块都继承的插件 -->
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
        </plugin>
        
        <!-- Groovy编译插件 -->
        <plugin>
            <groupId>org.codehaus.gmavenplus</groupId>
            <artifactId>gmavenplus-plugin</artifactId>
            <executions>
                <execution>
                    <goals>
                        <goal>compile</goal>
                        <goal>compileTests</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        
        <!-- JaCoCo代码覆盖率插件 -->
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <executions>
                <execution>
                    <id>default-prepare-agent</id>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                <execution>
                    <id>default-report</id>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
                <execution>
                    <id>default-check</id>
                    <goals>
                        <goal>check</goal>
                    </goals>
                    <configuration>
                        <rules>
                            <rule>
                                <element>BUNDLE</element>
                                <limits>
                                    <limit>
                                        <counter>COMPLEXITY</counter>
                                        <value>COVEREDRATIO</value>
                                        <minimum>0.0</minimum>
                                    </limit>
                                </limits>
                            </rule>
                        </rules>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## 使用示例

### Spock测试示例
```groovy
class RateLimiterSpockTest extends Specification {
    def "should create token bucket rate limiter"() {
        when:
        def rateLimiter = RateLimiter.create(10.0)

        then:
        rateLimiter != null
        rateLimiter.getRate() == 10.0
    }
}
```

### Mockito测试示例
```java
@ExtendWith(MockitoExtension.class)
class AQSRateLimiterMockitoTest {
    @Mock
    private RateLimiterAlgorithm mockAlgorithm;

    @Test
    void testAcquireWithMockedAlgorithm() throws InterruptedException {
        // Given
        AQSRateLimiter rateLimiter = new AQSRateLimiter(mockAlgorithm);
        when(mockAlgorithm.reserve(1)).thenReturn(0L);

        // When
        double waitTime = rateLimiter.acquire(1);

        // Then
        assertEquals(0.0, waitTime, 0.001);
        verify(mockAlgorithm).reserve(1);
    }
}
```

## 运行测试

可以使用以下命令运行测试：

```bash
# 运行所有测试
mvn test

# 强制更新依赖并运行测试
mvn clean test -U

# 或者分别编译和测试
mvn clean compile test-compile
mvn test
```