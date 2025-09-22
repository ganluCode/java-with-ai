package cn.geekslife.designpattern.observer;

/**
 * 观察者模式演示类
 */
public class ObserverPatternDemo {
    public static void main(String[] args) {
        // 创建天气数据主题
        WeatherData weatherData = new WeatherData();
        
        // 创建各种观察者显示
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(weatherData);
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay(weatherData);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherData);
        HeatIndexDisplay heatIndexDisplay = new HeatIndexDisplay(weatherData);
        
        System.out.println("=== 第一次天气更新 ===");
        weatherData.setMeasurements(80, 65, 30.4f);
        
        System.out.println("\n=== 第二次天气更新 ===");
        weatherData.setMeasurements(82, 70, 29.2f);
        
        System.out.println("\n=== 第三次天气更新 ===");
        weatherData.setMeasurements(78, 90, 29.2f);
    }
}