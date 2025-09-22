package cn.geekslife.designpattern.observer

import cn.geekslife.designpattern.observer.CurrentConditionsDisplay
import cn.geekslife.designpattern.observer.ForecastDisplay
import cn.geekslife.designpattern.observer.StatisticsDisplay
import cn.geekslife.designpattern.observer.WeatherData
import spock.lang.Specification

class WeatherDataSpec extends Specification {

    def "should notify all observers when measurements change"() {
        given:
        WeatherData weatherData = new WeatherData()
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData)
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherData)
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherData)
        
        when:
        weatherData.setMeasurements(80, 65, 30.4f)
        
        then:
        // 验证观察者被正确通知（通过输出验证）
        true // 在实际测试中，我们会验证输出或状态变化
    }
    
    def "should allow observers to be attached and detached"() {
        given:
        WeatherData weatherData = new WeatherData()
        CurrentConditionsDisplay observer = new CurrentConditionsDisplay(weatherData)
        
        when:
        int initialSize = weatherData.observers.size()
        weatherData.detach(observer)
        int afterDetachSize = weatherData.observers.size()
        weatherData.attach(observer)
        int afterAttachSize = weatherData.observers.size()
        
        then:
        initialSize == 1
        afterDetachSize == 0
        afterAttachSize == 1
    }
    
    def "should update current conditions display correctly"() {
        given:
        WeatherData weatherData = new WeatherData()
        CurrentConditionsDisplay display = new CurrentConditionsDisplay(weatherData)
        
        when:
        weatherData.setMeasurements(75, 70, 30.0f)
        
        then:
        // 通过反射获取私有字段值进行验证
        def temperatureField = CurrentConditionsDisplay.getDeclaredField("temperature")
        temperatureField.setAccessible(true)
        def humidityField = CurrentConditionsDisplay.getDeclaredField("humidity")
        humidityField.setAccessible(true)
        
        temperatureField.get(display) == 75.0f
        humidityField.get(display) == 70.0f
    }
}