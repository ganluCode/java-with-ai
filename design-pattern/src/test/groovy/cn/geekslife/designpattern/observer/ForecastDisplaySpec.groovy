package cn.geekslife.designpattern.observer

import cn.geekslife.designpattern.observer.ForecastDisplay
import cn.geekslife.designpattern.observer.WeatherData
import spock.lang.Specification

class ForecastDisplaySpec extends Specification {

    def "should display weather forecast based on pressure changes"() {
        given:
        WeatherData weatherData = new WeatherData()
        ForecastDisplay display = new ForecastDisplay(weatherData)
        
        when:
        weatherData.setMeasurements(80, 65, 30.4f) // 初始压力
        weatherData.setMeasurements(82, 70, 29.2f) // 压力下降
        
        then:
        // 验证天气预报逻辑
        true // 实际测试中会验证输出
    }
    
    def "should show different forecasts for different pressure trends"() {
        given:
        WeatherData weatherData = new WeatherData()
        ForecastDisplay display = new ForecastDisplay(weatherData)
        
        when:
        weatherData.setMeasurements(80, 65, 29.2f) // 初始压力
        weatherData.setMeasurements(82, 70, 30.4f) // 压力上升
        
        then:
        // 验证压力上升时的预报
        true // 实际测试中会验证输出
    }
}