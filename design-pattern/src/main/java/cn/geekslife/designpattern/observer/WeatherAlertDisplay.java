package cn.geekslife.designpattern.observer;

/**
 * 高优先级的天气预警显示
 */
public class WeatherAlertDisplay implements PriorityObserver {
    private WeatherData weatherData;
    
    public WeatherAlertDisplay(WeatherData weatherData) {
        this.weatherData = weatherData;
        weatherData.attach(this);
    }
    
    @Override
    public void update(Object subject) {
        if (subject instanceof WeatherData) {
            WeatherData weatherData = (WeatherData) subject;
            float temp = weatherData.getTemperature();
            
            // 高温预警
            if (temp > 95) {
                System.out.println("【紧急预警】高温警报! 当前温度: " + temp + "°C");
            }
            // 低温预警
            else if (temp < 32) {
                System.out.println("【紧急预警】低温警报! 当前温度: " + temp + "°C");
            }
        }
    }
    
    @Override
    public int getPriority() {
        return 1; // 最高优先级
    }
}