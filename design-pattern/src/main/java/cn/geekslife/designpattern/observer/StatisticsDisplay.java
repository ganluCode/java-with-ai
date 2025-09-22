package cn.geekslife.designpattern.observer;

/**
 * 具体观察者类 - 天气统计显示
 */
public class StatisticsDisplay implements Observer {
    private float maxTemp = 0.0f;
    private float minTemp = 200;
    private float tempSum = 0.0f;
    private int numReadings = 0;
    private Subject weatherData;
    
    public StatisticsDisplay(Subject weatherData) {
        this.weatherData = weatherData;
        weatherData.attach(this);
    }
    
    @Override
    public void update(Object subject) {
        if (subject instanceof WeatherData) {
            WeatherData weatherData = (WeatherData) subject;
            float temp = weatherData.getTemperature();
            tempSum += temp;
            numReadings++;
            
            if (temp > maxTemp) {
                maxTemp = temp;
            }
            
            if (temp < minTemp) {
                minTemp = temp;
            }
            
            display();
        }
    }
    
    public void display() {
        System.out.println("天气统计: 平均温度 " + (tempSum / numReadings) 
            + "°C, 最高温度 " + maxTemp + "°C, 最低温度 " + minTemp + "°C");
    }
}