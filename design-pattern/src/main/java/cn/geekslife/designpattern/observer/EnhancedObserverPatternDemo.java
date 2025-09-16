package cn.geekslife.designpattern.observer;

/**
 * 观察者模式增强特性演示类
 */
public class EnhancedObserverPatternDemo {
    public static void main(String[] args) {
        System.out.println("=== 增强版观察者模式演示 ===");
        
        // 创建增强版天气数据主题
        EnhancedWeatherData enhancedWeatherData = new EnhancedWeatherData();
        
        // 创建各种观察者显示
        CurrentConditionsDisplay currentDisplay = new CurrentConditionsDisplay(enhancedWeatherData);
        StatisticsDisplay statisticsDisplay = new StatisticsDisplay(enhancedWeatherData);
        ForecastDisplay forecastDisplay = new ForecastDisplay(enhancedWeatherData);
        HeatIndexDisplay heatIndexDisplay = new HeatIndexDisplay(enhancedWeatherData);
        
        System.out.println("=== 第一次天气更新 ===");
        enhancedWeatherData.setMeasurements(80, 65, 30.4f);
        
        System.out.println("\n=== 第二次天气更新 ===");
        enhancedWeatherData.setMeasurements(82, 70, 29.2f);
        
        System.out.println("\n=== 第三次天气更新 ===");
        enhancedWeatherData.setMeasurements(78, 90, 29.2f);
        
        // 关闭线程池
        AsyncSubject.shutdown();
    }
}