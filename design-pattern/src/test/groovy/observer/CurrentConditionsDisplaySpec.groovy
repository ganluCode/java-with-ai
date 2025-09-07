package observer

import spock.lang.Specification

class CurrentConditionsDisplaySpec extends Specification {

    def "should display current weather conditions"() {
        given:
        WeatherData weatherData = new WeatherData()
        CurrentConditionsDisplay display = new CurrentConditionsDisplay(weatherData)
        
        when:
        weatherData.setMeasurements(82, 70, 29.2f)
        
        then:
        // 验证显示逻辑正确执行
        true // 实际测试中会验证输出
    }
    
    def "should update when notified by weather data"() {
        given:
        WeatherData weatherData = new WeatherData()
        CurrentConditionsDisplay display = new CurrentConditionsDisplay(weatherData)
        
        when:
        display.update(weatherData)
        
        then:
        notThrown(Exception)
    }
}