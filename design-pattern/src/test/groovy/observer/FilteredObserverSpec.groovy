package observer

import spock.lang.Specification

class FilteredObserverSpec extends Specification {

    def "should only update when filter condition is met"() {
        given:
        TestWeatherData weatherData = new TestWeatherData()
        TestFilteredObserver filteredObserver = new TestFilteredObserver(weatherData)
        
        when:
        // 第一次设置温度
        weatherData.setTemperature(25.0f)
        int firstUpdateCount = filteredObserver.updateCount
        
        // 设置相同温度
        weatherData.setTemperature(25.0f)
        int secondUpdateCount = filteredObserver.updateCount
        
        // 设置不同温度
        weatherData.setTemperature(30.0f)
        int thirdUpdateCount = filteredObserver.updateCount
        
        then:
        firstUpdateCount == 1   // 第一次应该更新
        secondUpdateCount == 1  // 相同温度不应该更新
        thirdUpdateCount == 2   // 不同温度应该更新
    }
    
    // 测试用的天气数据类
    static class TestWeatherData extends Subject {
        private float temperature
        
        void setTemperature(float temperature) {
            this.temperature = temperature
            notifyObservers()
        }
        
        float getTemperature() {
            return temperature
        }
    }
    
    // 测试用的过滤观察者
    static class TestFilteredObserver extends FilteredObserver {
        private float lastTemperature = Float.MIN_VALUE
        int updateCount = 0
        
        TestFilteredObserver(Subject subject) {
            super(subject)
        }
        
        @Override
        protected boolean shouldUpdate(Subject subject) {
            if (subject instanceof TestWeatherData) {
                TestWeatherData weather = (TestWeatherData) subject
                return weather.getTemperature() != lastTemperature
            }
            return false
        }
        
        @Override
        protected void doUpdate(Subject subject) {
            if (subject instanceof TestWeatherData) {
                TestWeatherData weather = (TestWeatherData) subject
                lastTemperature = weather.getTemperature()
                updateCount++
            }
        }
    }
}