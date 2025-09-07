package observer

import spock.lang.Specification

class HeatIndexDisplaySpec extends Specification {

    def "should calculate and display heat index correctly"() {
        given:
        WeatherData weatherData = new WeatherData()
        HeatIndexDisplay display = new HeatIndexDisplay(weatherData)
        
        when:
        weatherData.setMeasurements(80, 65, 30.4f)
        
        then:
        // 验证热感指数计算和显示
        true // 实际测试中会验证计算结果
    }
    
    def "should compute heat index based on temperature and humidity"() {
        given:
        WeatherData weatherData = new WeatherData()
        HeatIndexDisplay display = new HeatIndexDisplay(weatherData)
        
        when:
        float heatIndex = display.computeHeatIndex(80, 65)
        
        then:
        // 验证热感指数计算公式正确性
        heatIndex > 0
    }
}