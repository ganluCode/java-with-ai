package observer;

/**
 * 温度敏感显示 - 只在温度变化时更新
 */
public class TemperatureSensitiveDisplay extends FilteredObserver {
    private float lastTemperature = Float.MIN_VALUE;
    
    public TemperatureSensitiveDisplay(WeatherData weatherData) {
        super(weatherData);
    }
    
    @Override
    protected boolean shouldUpdate(Subject subject) {
        if (subject instanceof WeatherData) {
            WeatherData weatherData = (WeatherData) subject;
            return weatherData.getTemperature() != lastTemperature;
        }
        return false;
    }
    
    @Override
    protected void doUpdate(Subject subject) {
        if (subject instanceof WeatherData) {
            WeatherData weatherData = (WeatherData) subject;
            lastTemperature = weatherData.getTemperature();
            System.out.println("温度变化至: " + lastTemperature + "°C");
        }
    }
}