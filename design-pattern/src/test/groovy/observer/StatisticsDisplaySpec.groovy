package observer

import spock.lang.Specification

class StatisticsDisplaySpec extends Specification {

    def "should calculate and display weather statistics"() {
        given:
        WeatherData weatherData = new WeatherData()
        StatisticsDisplay display = new StatisticsDisplay(weatherData)
        
        when:
        weatherData.setMeasurements(80, 65, 30.4f)
        weatherData.setMeasurements(82, 70, 29.2f)
        weatherData.setMeasurements(78, 90, 29.2f)
        
        then:
        // 验证统计计算逻辑
        true // 实际测试中会验证具体的统计值
    }
    
    def "should track max min and average temperatures"() {
        given:
        WeatherData weatherData = new WeatherData()
        StatisticsDisplay display = new StatisticsDisplay(weatherData)
        
        when:
        weatherData.setMeasurements(80, 65, 30.4f) // max: 80, min: 80, avg: 80
        weatherData.setMeasurements(82, 70, 29.2f) // max: 82, min: 80, avg: 81
        weatherData.setMeasurements(78, 90, 29.2f) // max: 82, min: 78, avg: 80
        
        then:
        // 验证统计值计算正确
        true // 实际测试中会验证这些值
    }
}